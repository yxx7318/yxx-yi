package com.yxx.pay.core.domain.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yxx.common.core.domain.BaseColumnEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "支付订单DO")
@TableName("pay_order")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderDO extends BaseColumnEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单编号")
    @TableId(value = "order_id")
    private Long orderId;

    @Schema(description = "支付订单号")
    @TableField("order_no")
    private String orderNo;

    @Schema(description = "用户编号")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "商户订单编号")
    @TableField("merchant_order_id")
    private String merchantOrderId;

    @Schema(description = "商品标题")
    @TableField("subject")
    private String subject;

    @Schema(description = "商品描述")
    @TableField("body")
    private String body;

    @Schema(description = "渠道编号")
    @TableField("channel_id")
    private Long channelId;

    @Schema(description = "渠道编码")
    @TableField("channel_code")
    private String channelCode;

    @Schema(description = "渠道用户编号(openid等)")
    @TableField("channel_user_id")
    private String channelUserId;

    @Schema(description = "渠道订单号")
    @TableField("channel_order_no")
    private String channelOrderNo;

    @Schema(description = "支付金额(分)")
    @TableField("pay_price")
    private Integer payPrice;

    @Schema(description = "渠道手续费率")
    @TableField("channel_fee_rate")
    private BigDecimal channelFeeRate;

    @Schema(description = "渠道手续费(分)")
    @TableField("channel_fee_price")
    private Integer channelFeePrice;

    @Schema(description = "支付状态(0待支付 10成功 20退款 30关闭)")
    @TableField("order_status")
    private Integer orderStatus;

    @Schema(description = "订单失效时间")
    @TableField("expire_time")
    private LocalDateTime expireTime;

    @Schema(description = "支付成功时间")
    @TableField("success_time")
    private LocalDateTime successTime;

    @Schema(description = "成功扩展单编号")
    @TableField("extension_id")
    private Long extensionId;

    @Schema(description = "已退款金额(分)")
    @TableField("refund_price")
    private Integer refundPrice;

}
