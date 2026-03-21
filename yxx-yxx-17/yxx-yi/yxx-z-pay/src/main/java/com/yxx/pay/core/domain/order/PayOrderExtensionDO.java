package com.yxx.pay.core.domain.order;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.yxx.common.core.domain.BaseColumnEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.util.Map;

@Schema(description = "支付订单扩展DO")
@TableName(value = "pay_order_extension", autoResultMap = true)
@KeySequence("pay_order_extension_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderExtensionDO extends BaseColumnEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "扩展编号")
    @TableId(value = "extension_id")
    private Long extensionId;

    @Schema(description = "订单编号")
    @TableField("order_id")
    private Long orderId;

    @Schema(description = "支付订单号")
    @TableField("order_no")
    private String orderNo;

    @Schema(description = "渠道编号")
    @TableField("channel_id")
    private Long channelId;

    @Schema(description = "渠道编码")
    @TableField("channel_code")
    private String channelCode;

    @Schema(description = "用户IP")
    @TableField("user_ip")
    private String userIp;

    @Schema(description = "支付状态")
    @TableField("order_status")
    private Integer orderStatus;

    @Schema(description = "支付渠道的额外参数")
    @TableField(value = "channel_extras", typeHandler = JacksonTypeHandler.class)
    private Map<String, String> channelExtras;

    @Schema(description = "渠道回调数据")
    @TableField("channel_notify_data")
    private String channelNotifyData;

    @Schema(description = "渠道错误码")
    @TableField("channel_error_code")
    private String channelErrorCode;

    @Schema(description = "渠道错误信息")
    @TableField("channel_error_msg")
    private String channelErrorMsg;

}
