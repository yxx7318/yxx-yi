package com.yxx.pay.core.domain.channel;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.yxx.common.core.domain.BaseColumnEntity;
import com.yxx.pay.client.PayClientConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;

@Schema(description = "支付渠道DO")
@TableName(value = "pay_channel", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayChannelDO extends BaseColumnEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "渠道编号")
    @TableId(value = "channel_id")
    private Long channelId;

    @Schema(description = "渠道编码")
    @TableField("channel_code")
    private String channelCode;

    @Schema(description = "渠道名称")
    @TableField("channel_name")
    private String channelName;

    @Schema(description = "渠道配置(JSON格式)")
    @TableField(value = "channel_config", typeHandler = Fastjson2TypeHandler.class)
    private PayClientConfig channelConfig;

    @Schema(description = "渠道手续费率(百分比)")
    @TableField("channel_fee_rate")
    private BigDecimal channelFeeRate;

    @Schema(description = "状态(0正常 1停用)")
    @TableField("status")
    private Integer status;

}
