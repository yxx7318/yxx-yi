package com.yxx.pay.core.entity.vo.refund;

import com.yxx.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Schema(description = "支付退款 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayRefundRespVO extends PayRefundBaseVO {

    @Schema(description = "退款编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @Excel(name = "退款编号")
    private Long refundId;

    @Schema(description = "退款单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "REF202401010001")
    @Excel(name = "退款单号")
    private String refundNo;

    @Schema(description = "渠道编号", example = "1")
    private Long channelId;

    @Schema(description = "渠道编码", example = "wx_pub")
    @Excel(name = "渠道编码")
    private String channelCode;

    @Schema(description = "渠道订单号", example = "4200001234567890")
    private String channelOrderNo;

    @Schema(description = "渠道退款单号", example = "5000001234567890")
    private String channelRefundNo;

    @Schema(description = "原支付金额(分)")
    @Excel(name = "原支付金额")
    private Integer payPrice;

    @Schema(description = "退款状态(0待退款 10成功 20失败)", requiredMode = Schema.RequiredMode.REQUIRED)
    @Excel(name = "退款状态")
    private Integer refundStatus;

    @Schema(description = "退款成功时间")
    @Excel(name = "退款成功时间")
    private LocalDateTime successTime;

    @Schema(description = "渠道错误码")
    private String channelErrorCode;

    @Schema(description = "渠道错误信息")
    private String channelErrorMsg;

    @Schema(description = "创建时间")
    @Excel(name = "创建时间")
    private LocalDateTime createTime;

}
