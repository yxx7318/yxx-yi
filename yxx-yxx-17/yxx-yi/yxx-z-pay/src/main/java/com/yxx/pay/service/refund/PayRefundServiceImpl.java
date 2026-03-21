package com.yxx.pay.service.refund;

import cn.hutool.core.util.IdUtil;
import com.yxx.common.core.domain.PageResult;
import com.yxx.common.core.redis.RedisLockSimple;
import com.yxx.common.core.service.impl.ServiceImplPlus;
import com.yxx.common.core.utils.JacksonUtils;
import com.yxx.common.exception.ServiceException;
import com.yxx.pay.client.PayClient;
import com.yxx.pay.client.dto.refund.PayRefundRespDTO;
import com.yxx.pay.client.dto.refund.PayRefundUnifiedReqDTO;
import com.yxx.pay.core.domain.channel.PayChannelDO;
import com.yxx.pay.core.domain.order.PayOrderDO;
import com.yxx.pay.core.domain.refund.PayRefundDO;
import com.yxx.pay.core.entity.dto.refund.PayRefundCreateReqDTO;
import com.yxx.pay.core.entity.vo.refund.PayRefundPageReqVO;
import com.yxx.pay.enums.order.PayOrderStatusEnum;
import com.yxx.pay.enums.refund.PayRefundStatusEnum;
import com.yxx.pay.mapper.PayRefundMapper;
import com.yxx.pay.service.channel.PayChannelService;
import com.yxx.pay.service.order.PayOrderService;
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

import static com.yxx.pay.enums.ErrorCodeConstants.*;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class PayRefundServiceImpl extends ServiceImplPlus<PayRefundMapper, PayRefundDO> implements PayRefundService {

    private static final String REFUND_NOTIFY_LOCK_KEY_PREFIX = "pay:refund:notify:lock:";

    private final PayRefundMapper payRefundMapper;
    private final PayOrderService payOrderService;
    private final PayChannelService payChannelService;
    private final RedisLockSimple redisLockSimple;
    private final PayRefundService self;

    @Override
    public PayRefundDO getRefund(Long refundId) {
        return payRefundMapper.selectByRefundId(refundId);
    }

    @Override
    public PayRefundDO getRefundByNo(String refundNo) {
        return payRefundMapper.selectByRefundNo(refundNo);
    }

    @Override
    public PayRefundDO getRefundByMerchantRefundId(String merchantRefundId) {
        return payRefundMapper.selectByMerchantRefundId(merchantRefundId);
    }

    @Override
    public List<PayRefundDO> getRefundListByOrderId(Long orderId) {
        return payRefundMapper.selectListByOrderId(orderId);
    }

    @Override
    public PageResult<PayRefundDO> getRefundPage(PayRefundPageReqVO pageReqVO) {
        startPage();
        List<PayRefundDO> list = payRefundMapper.selectRefundPageList(pageReqVO);
        clearPage();
        return getMyBatisPageResult(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRefund(PayRefundCreateReqDTO reqDTO) {
        PayOrderDO order = payOrderService.getOrderByMerchantOrderId(reqDTO.getMerchantOrderId());
        if (order == null) {
            throw new ServiceException(PAY_ORDER_NOT_FOUND);
        }
        if (!PayOrderStatusEnum.isSuccessOrRefund(order.getOrderStatus())) {
            throw new ServiceException(PAY_ORDER_REFUND_FAIL_STATUS_ERROR);
        }
        PayRefundDO existRefund = payRefundMapper.selectByMerchantRefundId(reqDTO.getMerchantRefundId());
        if (existRefund != null) {
            throw new ServiceException(REFUND_EXISTS);
        }
        List<PayRefundDO> refundingList = payRefundMapper.selectListByOrderIdAndStatus(order.getOrderId(), PayRefundStatusEnum.WAITING.getStatus());
        if (!refundingList.isEmpty()) {
            throw new ServiceException(REFUND_HAS_REFUNDING);
        }
        int refundPrice = order.getRefundPrice() + reqDTO.getPrice();
        if (refundPrice > order.getPayPrice()) {
            throw new ServiceException(REFUND_PRICE_EXCEED);
        }
        String refundNo = generateRefundNo();
        PayRefundDO refund = PayRefundDO.builder()
                .refundNo(refundNo)
                .orderId(order.getOrderId())
                .userId(reqDTO.getUserId())
                .merchantOrderId(reqDTO.getMerchantOrderId())
                .merchantRefundId(reqDTO.getMerchantRefundId())
                .channelId(order.getChannelId())
                .channelCode(order.getChannelCode())
                .channelOrderNo(order.getChannelOrderNo())
                .payPrice(order.getPayPrice())
                .refundPrice(reqDTO.getPrice())
                .refundReason(reqDTO.getReason())
                .refundStatus(PayRefundStatusEnum.WAITING.getStatus())
                .userIp(reqDTO.getUserIp())
                .build();
        refund.fieldFillInsert();
        payRefundMapper.insert(refund);
        PayClient client = payChannelService.getPayClient(order.getChannelId());
        PayRefundUnifiedReqDTO unifiedReqDTO = new PayRefundUnifiedReqDTO();
        unifiedReqDTO.setOutRefundNo(refundNo);
        unifiedReqDTO.setOutTradeNo(order.getChannelOrderNo());
        unifiedReqDTO.setRefundPrice(reqDTO.getPrice());
        unifiedReqDTO.setPayPrice(order.getPayPrice());
        unifiedReqDTO.setReason(reqDTO.getReason());
        try {
            PayRefundRespDTO respDTO = client.unifiedRefund(unifiedReqDTO);
            refund.setChannelRefundNo(respDTO.getChannelRefundNo());
            refund.setRefundStatus(PayRefundStatusEnum.SUCCESS.getStatus());
            refund.setSuccessTime(LocalDateTime.now());
            payRefundMapper.updateById(refund);
            payOrderService.updateOrderRefundPrice(order.getOrderId(), reqDTO.getPrice());
        } catch (Exception e) {
            log.error("[createRefund][退款单({}) 发起退款异常]", refund.getRefundId(), e);
            refund.setRefundStatus(PayRefundStatusEnum.FAILURE.getStatus());
            refund.setChannelErrorCode("SYSTEM_ERROR");
            refund.setChannelErrorMsg(e.getMessage());
            payRefundMapper.updateById(refund);
            throw new ServiceException(REFUND_STATUS_IS_NOT_WAITING);
        }
        return refund.getRefundId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notifyRefund(Long channelId, PayRefundRespDTO notify) {
        String lockKey = REFUND_NOTIFY_LOCK_KEY_PREFIX + notify.getOutRefundNo();
        boolean locked = redisLockSimple.tryLock(lockKey, Duration.ofSeconds(10).toMillis());
        if (!locked) {
            log.warn("[notifyRefund][退款单({}) 获取锁失败，可能正在处理中]", notify.getOutRefundNo());
            return;
        }
        try {
            self.notifyRefund(channelId, notify);
        } finally {
            redisLockSimple.unlock(lockKey);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void doNotifyRefund(Long channelId, PayRefundRespDTO notify) {
        PayRefundDO refund = payRefundMapper.selectByRefundNo(notify.getOutRefundNo());
        if (refund == null) {
            log.error("[doNotifyRefund][退款单({}) 不存在]", notify.getOutRefundNo());
            return;
        }
        if (!PayRefundStatusEnum.isFailure(refund.getRefundStatus()) && !PayRefundStatusEnum.isSuccess(refund.getRefundStatus())) {
            log.info("[doNotifyRefund][退款单({}) 状态不是待退款，跳过处理]", refund.getRefundId());
            return;
        }
        refund.setChannelNotifyData(JacksonUtils.toJsonString(notify.getRawData()));
        if (PayRefundStatusEnum.isSuccess(notify.getStatus())) {
            refund.setRefundStatus(PayRefundStatusEnum.SUCCESS.getStatus());
            refund.setSuccessTime(LocalDateTime.now());
            refund.setChannelRefundNo(notify.getChannelRefundNo());
            payRefundMapper.updateById(refund);
            payOrderService.updateOrderRefundPrice(refund.getOrderId(), refund.getRefundPrice());
        } else {
            refund.setRefundStatus(PayRefundStatusEnum.FAILURE.getStatus());
            refund.setChannelErrorCode(notify.getChannelErrorCode());
            refund.setChannelErrorMsg(notify.getChannelErrorMsg());
            payRefundMapper.updateById(refund);
        }
    }

    @Override
    public int syncRefund(LocalDateTime minCreateTime) {
        List<PayRefundDO> refunds = payRefundMapper.selectListByStatusAndCreateTimeGe(
                PayRefundStatusEnum.WAITING.getStatus(), minCreateTime);
        int count = 0;
        for (PayRefundDO refund : refunds) {
            try {
                self.syncRefundQuietly(refund.getRefundId());
                count++;
            } catch (Exception e) {
                log.error("[syncRefund][退款单({}) 同步失败]", refund.getRefundId(), e);
            }
        }
        return count;
    }

    @Override
    public void syncRefundQuietly(Long refundId) {
        try {
            PayRefundDO refund = payRefundMapper.selectByRefundId(refundId);
            if (refund == null || !PayRefundStatusEnum.isFailure(refund.getRefundStatus())) {
                return;
            }
            PayClient client = payChannelService.getPayClient(refund.getChannelId());
            PayRefundRespDTO respDTO = client.getRefund(refund.getChannelOrderNo(), refund.getRefundNo());
            if (respDTO == null || !PayRefundStatusEnum.isSuccess(respDTO.getStatus())) {
                return;
            }
            notifyRefund(refund.getChannelId(), respDTO);
        } catch (Exception e) {
            log.error("[syncRefundQuietly][退款单({}) 同步失败]", refundId, e);
        }
    }

    private String generateRefundNo() {
        return "REF" + IdUtil.getSnowflakeNextIdStr();
    }

}
