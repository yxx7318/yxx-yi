package com.yxx.pay.api.refund;

import com.yxx.pay.core.entity.dto.refund.PayRefundCreateReqDTO;
import com.yxx.pay.core.entity.dto.refund.PayRefundRespDTO;
import jakarta.validation.Valid;

public interface PayRefundApi {

    Long createRefund(@Valid PayRefundCreateReqDTO reqDTO);

    PayRefundRespDTO getRefund(Long id);

    PayRefundRespDTO getRefundByMerchantRefundId(String merchantRefundId);

}
