package com.yxx.pay.api.order;

import com.yxx.common.utils.bean.BeanUtils;
import com.yxx.pay.core.domain.order.PayOrderDO;
import com.yxx.pay.core.entity.dto.order.PayOrderCreateReqDTO;
import com.yxx.pay.core.entity.dto.order.PayOrderRespDTO;
import com.yxx.pay.service.order.PayOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class PayOrderApiImpl implements PayOrderApi {

    private final PayOrderService payOrderService;

    @Override
    public Long createOrder(PayOrderCreateReqDTO reqDTO) {
        return payOrderService.createOrder(reqDTO);
    }

    @Override
    public PayOrderRespDTO getOrder(Long id) {
        PayOrderDO order = payOrderService.getOrder(id);
        return BeanUtils.convertBean(order, PayOrderRespDTO.class);
    }

    @Override
    public PayOrderRespDTO getOrderByMerchantOrderId(String merchantOrderId) {
        PayOrderDO order = payOrderService.getOrderByMerchantOrderId(merchantOrderId);
        return BeanUtils.convertBean(order, PayOrderRespDTO.class);
    }

}
