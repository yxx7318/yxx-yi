package com.yxx.pay.core.domain.refund;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yxx.common.core.domain.BaseColumnEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.time.LocalDateTime;

@Schema(description = "支付退款DO")
@TableName("pay_refund")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayRefundDO extends BaseColumnEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "退款编号")
    @TableId(value = "refund_id")
    private Long refundId;

    @Schema(description = "退款单号")
    @TableField("refund_no")
    private String refundNo;

    @Schema(description = "订单编号")
    @TableField("order_id")
    private Long orderId;

    @Schema(description = "用户编号")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "商户订单编号")
    @TableField("merchant_order_id")
    private String merchantOrderId;

    @Schema(description = "商户退款单号")
    @TableField("merchant_refund_id")
    private String merchantRefundId;

    @Schema(description = "渠道编号")
    @TableField("channel_id")
    private Long channelId;

    @Schema(description = "渠道编码")
    @TableField("channel_code")
    private String channelCode;

    @Schema(description = "渠道订单号")
    @TableField("channel_order_no")
    private String channelOrderNo;

    @Schema(description = "渠道退款单号")
    @TableField("channel_refund_no")
    private String channelRefundNo;

    @Schema(description = "原支付金额(分)")
    @TableField("pay_price")
    private Integer payPrice;

    @Schema(description = "退款金额(分)")
    @TableField("refund_price")
    private Integer refundPrice;

    @Schema(description = "退款原因")
    @TableField("refund_reason")
    private String refundReason;

    @Schema(description = "退款状态(0待退款 10成功 20失败)")
    @TableField("refund_status")
    private Integer refundStatus;

    @Schema(description = "退款成功时间")
    @TableField("success_time")
    private LocalDateTime successTime;

    @Schema(description = "用户IP")
    @TableField("user_ip")
    private String userIp;

    @Schema(description = "渠道错误码")
    @TableField("channel_error_code")
    private String channelErrorCode;

    @Schema(description = "渠道错误信息")
    @TableField("channel_error_msg")
    private String channelErrorMsg;

    @Schema(description = "渠道回调数据")
    @TableField("channel_notify_data")
    private String channelNotifyData;

}
