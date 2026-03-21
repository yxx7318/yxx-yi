package com.yxx.pay.core.entity.vo.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "支付渠道 Base VO")
@Data
public class PayChannelBaseVO {

    @Schema(description = "渠道编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "wx_pub")
    @NotEmpty(message = "渠道编码不能为空")
    private String channelCode;

    @Schema(description = "渠道名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "微信JSAPI支付")
    @NotEmpty(message = "渠道名称不能为空")
    private String channelName;

    @Schema(description = "渠道手续费率(百分比)", requiredMode = Schema.RequiredMode.REQUIRED, example = "0.60")
    @NotNull(message = "渠道手续费率不能为空")
    private BigDecimal channelFeeRate;

    @Schema(description = "状态(0正常 1停用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "备注", example = "微信公众号支付")
    private String remark;

}
