package com.yxx.pay.service.refund;

import com.yxx.common.core.domain.PageResult;
import com.yxx.common.core.service.IServicePlus;
import com.yxx.pay.client.dto.refund.PayRefundRespDTO;
import com.yxx.pay.core.domain.refund.PayRefundDO;
import com.yxx.pay.core.entity.dto.refund.PayRefundCreateReqDTO;
import com.yxx.pay.core.entity.vo.refund.PayRefundPageReqVO;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

public interface PayRefundService extends IServicePlus<PayRefundDO> {

    PayRefundDO getRefund(Long refundId);

    PayRefundDO getRefundByNo(String refundNo);

    PayRefundDO getRefundByMerchantRefundId(String merchantRefundId);

    List<PayRefundDO> getRefundListByOrderId(Long orderId);

    PageResult<PayRefundDO> getRefundPage(PayRefundPageReqVO pageReqVO);

    Long createRefund(@Valid PayRefundCreateReqDTO reqDTO);

    void notifyRefund(Long channelId, PayRefundRespDTO notify);

    int syncRefund(LocalDateTime minCreateTime);

    void syncRefundQuietly(Long refundId);

}
