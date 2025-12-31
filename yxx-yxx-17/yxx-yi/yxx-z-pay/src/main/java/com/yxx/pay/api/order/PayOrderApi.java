package com.yxx.pay.api.order;


import com.yxx.pay.core.entity.dto.order.PayOrderCreateReqDTO;
import com.yxx.pay.core.entity.dto.order.PayOrderRespDTO;
import jakarta.validation.Valid;

/**
 * 支付单 API 接口
 *
 * @author yxx
 */
public interface PayOrderApi {

    /**
     * 创建支付单
     *
     * @param reqDTO 创建请求
     * @return 支付单编号
     */
    Long createOrder(@Valid PayOrderCreateReqDTO reqDTO);

    /**
     * 获得支付单
     *
     * @param id 支付单编号
     * @return 支付单
     */
    PayOrderRespDTO getOrder(Long id);

    /**
     * 更新支付订单价格
     *
     * @param id 支付单编号
     * @param payPrice   支付单价格
     */
    void updatePayOrderPrice(Long id, Integer payPrice);

}
