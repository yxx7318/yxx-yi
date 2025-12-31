package com.yxx.pay.api.order;

import com.yxx.pay.core.domain.order.PayOrderDO;
import com.yxx.pay.core.entity.dto.order.PayOrderCreateReqDTO;
import com.yxx.pay.core.entity.dto.order.PayOrderRespDTO;
import com.yxx.pay.service.order.PayOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


/**
 * 支付单 API 实现类
 *
 * @author yxx
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class PayOrderApiImpl implements PayOrderApi {

    private final PayOrderService payOrderService;

    public Long createOrder(PayOrderCreateReqDTO reqDTO) {
        return payOrderService.createOrder(reqDTO);
    }

    @Override
    public PayOrderRespDTO getOrder(Long id) {
        PayOrderDO order = payOrderService.getOrder(id);
//        return PayOrderConvert.INSTANCE.convert2(order);
        return null;
    }

    @Override
    public void updatePayOrderPrice(Long id, Integer payPrice) {
        payOrderService.updatePayOrderPrice(id, payPrice);
    }

}
