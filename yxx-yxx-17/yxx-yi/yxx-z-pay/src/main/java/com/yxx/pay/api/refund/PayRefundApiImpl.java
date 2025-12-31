package com.yxx.pay.api.refund;

import com.yxx.common.utils.bean.BeanUtils;
import com.yxx.pay.core.domain.refund.PayRefundDO;
import com.yxx.pay.core.entity.dto.refund.PayRefundCreateReqDTO;
import com.yxx.pay.core.entity.dto.refund.PayRefundRespDTO;
import com.yxx.pay.service.refund.PayRefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 退款单 API 实现类
 *
 * @author yxx
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class PayRefundApiImpl implements PayRefundApi {

    private final PayRefundService payRefundService;

    @Override
    public Long createRefund(PayRefundCreateReqDTO reqDTO) {
        return payRefundService.createRefund(reqDTO);
    }

    @Override
    public PayRefundRespDTO getRefund(Long id) {
        PayRefundDO refund = payRefundService.getRefund(id);
        return BeanUtils.convertBean(refund, PayRefundRespDTO.class);
    }

}
