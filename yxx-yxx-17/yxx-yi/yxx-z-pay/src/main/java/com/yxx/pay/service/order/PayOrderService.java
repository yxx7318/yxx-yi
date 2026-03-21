package com.yxx.pay.service.order;

import com.yxx.common.core.domain.PageResult;
import com.yxx.common.core.service.IServicePlus;
import com.yxx.pay.client.dto.order.PayOrderRespDTO;
import com.yxx.pay.core.domain.order.PayOrderDO;
import com.yxx.pay.core.domain.order.PayOrderExtensionDO;
import com.yxx.pay.core.entity.dto.order.PayOrderCreateReqDTO;
import com.yxx.pay.core.entity.vo.order.PayOrderPageReqVO;
import com.yxx.pay.core.entity.vo.order.PayOrderSubmitReqVO;
import com.yxx.pay.core.entity.vo.order.PayOrderSubmitRespVO;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

public interface PayOrderService extends IServicePlus<PayOrderDO> {

    PayOrderDO getOrder(Long orderId);

    PayOrderDO getOrderByNo(String orderNo);

    PayOrderDO getOrderByMerchantOrderId(String merchantOrderId);

    List<PayOrderDO> getOrderList(List<Long> orderIds);

    PageResult<PayOrderDO> getOrderPage(PayOrderPageReqVO pageReqVO);

    Long createOrder(@Valid PayOrderCreateReqDTO reqDTO);

    PayOrderSubmitRespVO submitOrder(@Valid PayOrderSubmitReqVO reqVO, String userIp);

    void notifyOrder(Long channelId, PayOrderRespDTO notify);

    void updateOrderRefundPrice(Long orderId, Integer incrRefundPrice);

    PayOrderExtensionDO getOrderExtension(Long extensionId);

    PayOrderExtensionDO getOrderExtensionByNo(String no);

    int syncOrder(LocalDateTime minCreateTime);

    void syncOrderQuietly(Long orderId);

    int expireOrder();

}
