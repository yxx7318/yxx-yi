package com.yxx.pay.core.entity.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "支付订单 Response DTO")
@Data
public class PayOrderRespDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long orderId;

    @Schema(description = "支付订单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "PAY202401010001")
    private String orderNo;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long userId;

    @Schema(description = "商户订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "ORDER_202401010001")
    private String merchantOrderId;

    @Schema(description = "商品标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "iPhone 15 Pro")
    private String subject;

    @Schema(description = "商品描述", example = "iPhone 15 Pro 256GB 暗夜紫")
    private String body;

    @Schema(description = "渠道编号", example = "1")
    private Long channelId;

    @Schema(description = "渠道编码", example = "wx_pub")
    private String channelCode;

    @Schema(description = "渠道用户编号(openid等)", example = "oUpF8uMuAJ...")
    private String channelUserId;

    @Schema(description = "渠道订单号", example = "4200001234567890")
    private String channelOrderNo;

    @Schema(description = "支付金额(分)", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Integer payPrice;

    @Schema(description = "渠道手续费率")
    private BigDecimal channelFeeRate;

    @Schema(description = "渠道手续费(分)")
    private Integer channelFeePrice;

    @Schema(description = "支付状态(0待支付 10成功 20退款 30关闭)", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer orderStatus;

    @Schema(description = "订单失效时间")
    private LocalDateTime expireTime;

    @Schema(description = "支付成功时间")
    private LocalDateTime successTime;

    @Schema(description = "成功扩展单编号")
    private Long extensionId;

    @Schema(description = "已退款金额(分)")
    private Integer refundPrice;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
