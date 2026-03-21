package com.yxx.pay.core.entity.vo.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "支付渠道创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayChannelCreateReqVO extends PayChannelBaseVO {

    @Schema(description = "渠道配置(JSON格式)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "渠道配置不能为空")
    private String channelConfig;

}
