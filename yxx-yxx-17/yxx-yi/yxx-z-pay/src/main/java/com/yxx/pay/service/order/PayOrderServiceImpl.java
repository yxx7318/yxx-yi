package com.yxx.pay.service.order;

import cn.hutool.core.util.IdUtil;
import com.yxx.common.core.domain.PageResult;
import com.yxx.common.core.service.impl.ServiceImplPlus;
import com.yxx.common.core.utils.JacksonUtils;
import com.yxx.common.exception.ServiceException;
import com.yxx.common.core.redis.RedisLockSimple;
import com.yxx.pay.client.PayClient;
import com.yxx.pay.client.dto.order.PayOrderRespDTO;
import com.yxx.pay.client.dto.order.PayOrderUnifiedReqDTO;
import com.yxx.pay.core.domain.channel.PayChannelDO;
import com.yxx.pay.core.domain.order.PayOrderDO;
import com.yxx.pay.core.domain.order.PayOrderExtensionDO;
import com.yxx.pay.core.entity.dto.order.PayOrderCreateReqDTO;
import com.yxx.pay.core.entity.vo.order.PayOrderPageReqVO;
import com.yxx.pay.core.entity.vo.order.PayOrderSubmitReqVO;
import com.yxx.pay.core.entity.vo.order.PayOrderSubmitRespVO;
import com.yxx.pay.enums.PayChannelEnum;
import com.yxx.pay.enums.order.PayOrderStatusEnum;
import com.yxx.pay.mapper.PayOrderExtensionMapper;
import com.yxx.pay.mapper.PayOrderMapper;
import com.yxx.pay.service.channel.PayChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.yxx.pay.enums.ErrorCodeConstants.*;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class PayOrderServiceImpl extends ServiceImplPlus<PayOrderMapper, PayOrderDO> implements PayOrderService {

    private static final String ORDER_NOTIFY_LOCK_KEY_PREFIX = "pay:order:notify:lock:";
    private static final int ORDER_EXPIRE_TIME_MINUTES = 30;

    private final PayOrderMapper payOrderMapper;
    private final PayOrderExtensionMapper payOrderExtensionMapper;
    private final PayChannelService payChannelService;
    private final RedisLockSimple redisLockSimple;
    private final PayOrderService self;

    @Override
    public PayOrderDO getOrder(Long orderId) {
        return payOrderMapper.selectByOrderId(orderId);
    }

    @Override
    public PayOrderDO getOrderByNo(String orderNo) {
        return payOrderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public PayOrderDO getOrderByMerchantOrderId(String merchantOrderId) {
        return payOrderMapper.selectByMerchantOrderId(merchantOrderId);
    }

    @Override
    public List<PayOrderDO> getOrderList(List<Long> orderIds) {
        return payOrderMapper.selectBatchIds(orderIds);
    }

    @Override
    public PageResult<PayOrderDO> getOrderPage(PayOrderPageReqVO pageReqVO) {
        startPage();
        List<PayOrderDO> list = payOrderMapper.selectOrderPageList(pageReqVO);
        clearPage();
        return getMyBatisPageResult(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(PayOrderCreateReqDTO reqDTO) {
        PayOrderDO existOrder = payOrderMapper.selectByMerchantOrderId(reqDTO.getMerchantOrderId());
        if (existOrder != null) {
            if (PayOrderStatusEnum.isSuccessOrRefund(existOrder.getOrderStatus())) {
                throw new ServiceException(PAY_ORDER_STATUS_IS_SUCCESS);
            }
            if (PayOrderStatusEnum.isWaiting(existOrder.getOrderStatus())) {
                if (existOrder.getExpireTime().isAfter(LocalDateTime.now())) {
                    return existOrder.getOrderId();
                }
            }
        }
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(ORDER_EXPIRE_TIME_MINUTES);
        PayOrderDO order = PayOrderDO.builder()
                .orderNo(generateOrderNo())
                .userId(reqDTO.getUserId())
                .merchantOrderId(reqDTO.getMerchantOrderId())
                .subject(reqDTO.getSubject())
                .body(reqDTO.getBody())
                .payPrice(reqDTO.getPayPrice())
                .orderStatus(PayOrderStatusEnum.WAITING.getStatus())
                .expireTime(expireTime)
                .refundPrice(0)
                .build();
        order.fieldFillInsert();
        payOrderMapper.insert(order);
        return order.getOrderId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayOrderSubmitRespVO submitOrder(PayOrderSubmitReqVO reqVO, String userIp) {
        PayOrderDO order = validateOrderCanSubmit(reqVO.getOrderId());
        PayChannelDO channel = payChannelService.validPayChannel(reqVO.getChannelCode());
        String orderNo = generateExtensionNo();
        PayOrderExtensionDO extension = PayOrderExtensionDO.builder()
                .orderId(order.getOrderId())
                .orderNo(orderNo)
                .channelId(channel.getChannelId())
                .channelCode(channel.getChannelCode())
                .userIp(userIp)
                .orderStatus(PayOrderStatusEnum.WAITING.getStatus())
                .channelExtras(reqVO.getChannelExtras())
                .build();
        extension.fieldFillInsert();
        payOrderExtensionMapper.insert(extension);
        PayClient client = payChannelService.getPayClient(channel.getChannelId());
        PayOrderUnifiedReqDTO unifiedReqDTO = buildUnifiedOrderReqDTO(order, extension, userIp);
        PayOrderRespDTO respDTO;
        try {
            respDTO = client.unifiedOrder(unifiedReqDTO);
        } catch (Exception e) {
            log.error("[submitOrder][订单({}) 渠道({}) 发起支付异常]", order.getOrderId(), channel.getChannelCode(), e);
            extension.setOrderStatus(PayOrderStatusEnum.CLOSED.getStatus());
            extension.setChannelErrorCode("SYSTEM_ERROR");
            extension.setChannelErrorMsg(e.getMessage());
            payOrderExtensionMapper.updateById(extension);
            throw new ServiceException(PAY_ORDER_SUBMIT_CHANNEL_ERROR, "SYSTEM_ERROR", e.getMessage());
        }
        extension.setOrderStatus(PayOrderStatusEnum.WAITING.getStatus());
        payOrderExtensionMapper.updateById(extension);
        return PayOrderSubmitRespVO.builder()
                .status(order.getOrderStatus())
                .displayMode(respDTO.getDisplayMode())
                .displayContent(respDTO.getDisplayContent())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notifyOrder(Long channelId, PayOrderRespDTO notify) {
        String lockKey = ORDER_NOTIFY_LOCK_KEY_PREFIX + notify.getOutTradeNo();
        boolean locked = redisLockSimple.tryLock(lockKey, Duration.ofSeconds(10).toMillis());
        if (!locked) {
            log.warn("[notifyOrder][订单({}) 获取锁失败，可能正在处理中]", notify.getOutTradeNo());
            return;
        }
        try {
            self.notifyOrder(channelId, notify);
        } finally {
            redisLockSimple.unlock(lockKey);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void doNotifyOrder(Long channelId, PayOrderRespDTO notify) {
        PayOrderExtensionDO extension = payOrderExtensionMapper.selectByOrderNo(notify.getOutTradeNo());
        if (extension == null) {
            log.error("[doNotifyOrder][扩展单({}) 不存在]", notify.getOutTradeNo());
            return;
        }
        if (!PayOrderStatusEnum.isWaiting(extension.getOrderStatus())) {
            log.info("[doNotifyOrder][扩展单({}) 状态不是待支付，跳过处理]", extension.getExtensionId());
            return;
        }
        PayOrderDO order = payOrderMapper.selectByOrderId(extension.getOrderId());
        if (order == null) {
            log.error("[doNotifyOrder][订单({}) 不存在]", extension.getOrderId());
            return;
        }
        if (!PayOrderStatusEnum.isWaiting(order.getOrderStatus())) {
            log.info("[doNotifyOrder][订单({}) 状态不是待支付，跳过处理]", order.getOrderId());
            return;
        }
        extension.setChannelNotifyData(JacksonUtils.toJsonString(notify.getRawData()));
        extension.setOrderStatus(PayOrderStatusEnum.SUCCESS.getStatus());
        payOrderExtensionMapper.updateById(extension);
        PayOrderDO updateOrder = PayOrderDO.builder()
                .orderStatus(PayOrderStatusEnum.SUCCESS.getStatus())
                .channelId(channelId)
                .channelCode(extension.getChannelCode())
                .channelUserId(notify.getChannelUserId())
                .channelOrderNo(notify.getChannelOrderNo())
                .successTime(LocalDateTime.now())
                .extensionId(extension.getExtensionId())
                .channelFeeRate(payChannelService.getChannel(channelId).getChannelFeeRate())
                .build();
        int rows = payOrderMapper.updateByIdAndStatus(order.getOrderId(), PayOrderStatusEnum.WAITING.getStatus(), updateOrder);
        if (rows == 0) {
            log.warn("[doNotifyOrder][订单({}) 更新失败，可能已被其他线程处理]", order.getOrderId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderRefundPrice(Long orderId, Integer incrRefundPrice) {
        payOrderMapper.updateRefundPrice(orderId, incrRefundPrice);
    }

    @Override
    public PayOrderExtensionDO getOrderExtension(Long extensionId) {
        return payOrderExtensionMapper.selectByExtensionId(extensionId);
    }

    @Override
    public PayOrderExtensionDO getOrderExtensionByNo(String no) {
        return payOrderExtensionMapper.selectByOrderNo(no);
    }

    @Override
    public int syncOrder(LocalDateTime minCreateTime) {
        List<PayOrderExtensionDO> extensions = payOrderExtensionMapper.selectListByStatusAndCreateTimeGe(
                PayOrderStatusEnum.WAITING.getStatus(), minCreateTime);
        int count = 0;
        for (PayOrderExtensionDO extension : extensions) {
            try {
                self.syncOrderQuietly(extension.getOrderId());
                count++;
            } catch (Exception e) {
                log.error("[syncOrder][扩展单({}) 同步失败]", extension.getExtensionId(), e);
            }
        }
        return count;
    }

    @Override
    public void syncOrderQuietly(Long orderId) {
        try {
            PayOrderDO order = payOrderMapper.selectByOrderId(orderId);
            if (order == null || !PayOrderStatusEnum.isWaiting(order.getOrderStatus())) {
                return;
            }
            List<PayOrderExtensionDO> extensions = payOrderExtensionMapper.selectListByOrderIdAndStatus(
                    orderId, PayOrderStatusEnum.WAITING.getStatus());
            if (extensions.isEmpty()) {
                return;
            }
            PayOrderExtensionDO extension = extensions.get(0);
            PayClient client = payChannelService.getPayClient(extension.getChannelId());
            PayOrderRespDTO respDTO = client.getOrder(extension.getOrderNo());
            if (respDTO == null || !PayOrderStatusEnum.isSuccess(respDTO.getStatus())) {
                return;
            }
            notifyOrder(extension.getChannelId(), respDTO);
        } catch (Exception e) {
            log.error("[syncOrderQuietly][订单({}) 同步失败]", orderId, e);
        }
    }

    @Override
    public int expireOrder() {
        List<PayOrderDO> orders = payOrderMapper.selectListByStatusAndExpireTimeLt(
                PayOrderStatusEnum.WAITING.getStatus(), LocalDateTime.now());
        int count = 0;
        for (PayOrderDO order : orders) {
            try {
                PayOrderDO updateOrder = PayOrderDO.builder()
                        .orderStatus(PayOrderStatusEnum.CLOSED.getStatus())
                        .build();
                int rows = payOrderMapper.updateByIdAndStatus(order.getOrderId(), PayOrderStatusEnum.WAITING.getStatus(), updateOrder);
                if (rows > 0) {
                    count++;
                }
            } catch (Exception e) {
                log.error("[expireOrder][订单({}) 关闭失败]", order.getOrderId(), e);
            }
        }
        return count;
    }

    private PayOrderDO validateOrderCanSubmit(Long orderId) {
        PayOrderDO order = payOrderMapper.selectByOrderId(orderId);
        if (order == null) {
            throw new ServiceException(PAY_ORDER_NOT_FOUND);
        }
        if (!PayOrderStatusEnum.isWaiting(order.getOrderStatus())) {
            throw new ServiceException(PAY_ORDER_STATUS_IS_NOT_WAITING);
        }
        if (order.getExpireTime().isBefore(LocalDateTime.now())) {
            throw new ServiceException(PAY_ORDER_IS_EXPIRED);
        }
        return order;
    }

    private String generateOrderNo() {
        return "PAY" + IdUtil.getSnowflakeNextIdStr();
    }

    private String generateExtensionNo() {
        return "EXT" + IdUtil.getSnowflakeNextIdStr();
    }

    private PayOrderUnifiedReqDTO buildUnifiedOrderReqDTO(PayOrderDO order, PayOrderExtensionDO extension, String userIp) {
        PayOrderUnifiedReqDTO reqDTO = new PayOrderUnifiedReqDTO();
        reqDTO.setOutTradeNo(extension.getOrderNo());
        reqDTO.setSubject(order.getSubject());
        reqDTO.setBody(order.getBody());
        reqDTO.setPrice(order.getPayPrice());
        reqDTO.setUserIp(userIp);
        reqDTO.setChannelExtras(extension.getChannelExtras());
        reqDTO.setExpireTime(order.getExpireTime());
        return reqDTO;
    }

}
