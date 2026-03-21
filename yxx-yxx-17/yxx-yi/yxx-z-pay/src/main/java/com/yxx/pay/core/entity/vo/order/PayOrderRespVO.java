package com.yxx.pay.core.entity.vo.order;

import com.yxx.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "支付订单 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayOrderRespVO extends PayOrderBaseVO {

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @Excel(name = "订单编号")
    private Long orderId;

    @Schema(description = "支付订单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "PAY202401010001")
    @Excel(name = "支付订单号")
    private String orderNo;

    @Schema(description = "渠道编号", example = "1")
    private Long channelId;

    @Schema(description = "渠道编码", example = "wx_pub")
    @Excel(name = "渠道编码")
    private String channelCode;

    @Schema(description = "渠道用户编号(openid等)", example = "oUpF8uMuAJ...")
    private String channelUserId;

    @Schema(description = "渠道订单号", example = "4200001234567890")
    private String channelOrderNo;

    @Schema(description = "渠道手续费率")
    private BigDecimal channelFeeRate;

    @Schema(description = "渠道手续费(分)")
    private Integer channelFeePrice;

    @Schema(description = "支付状态(0待支付 10成功 20退款 30关闭)", requiredMode = Schema.RequiredMode.REQUIRED)
    @Excel(name = "支付状态")
    private Integer orderStatus;

    @Schema(description = "支付成功时间")
    @Excel(name = "支付成功时间")
    private LocalDateTime successTime;

    @Schema(description = "成功扩展单编号")
    private Long extensionId;

    @Schema(description = "已退款金额(分)")
    @Excel(name = "已退款金额")
    private Integer refundPrice;

    @Schema(description = "创建时间")
    @Excel(name = "创建时间")
    private LocalDateTime createTime;

}
