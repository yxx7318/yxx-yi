package com.yxx.pay.core.entity.vo.refund;

import com.yxx.common.core.domain.PageQueryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Schema(description = "支付退款分页查询 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayRefundPageReqVO extends PageQueryEntity {

    @Schema(description = "退款单号", example = "REF202401010001")
    private String refundNo;

    @Schema(description = "用户编号", example = "1")
    private Long userId;

    @Schema(description = "订单编号", example = "1")
    private Long orderId;

    @Schema(description = "商户订单编号", example = "ORDER_202401010001")
    private String merchantOrderId;

    @Schema(description = "商户退款单号", example = "REFUND_202401010001")
    private String merchantRefundId;

    @Schema(description = "渠道编码", example = "wx_pub")
    private String channelCode;

    @Schema(description = "退款状态(0待退款 10成功 20失败)", example = "0")
    private Integer refundStatus;

    @Schema(description = "创建时间-开始")
    private LocalDateTime createTimeBegin;

    @Schema(description = "创建时间-结束")
    private LocalDateTime createTimeEnd;

}
