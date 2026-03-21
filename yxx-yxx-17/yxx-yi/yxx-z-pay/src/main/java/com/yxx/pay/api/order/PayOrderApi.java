package com.yxx.pay.api.order;

import com.yxx.pay.core.entity.dto.order.PayOrderCreateReqDTO;
import com.yxx.pay.core.entity.dto.order.PayOrderRespDTO;
import jakarta.validation.Valid;

public interface PayOrderApi {

    Long createOrder(@Valid PayOrderCreateReqDTO reqDTO);

    PayOrderRespDTO getOrder(Long id);

    PayOrderRespDTO getOrderByMerchantOrderId(String merchantOrderId);

}
