package com.yxx.pay.core.entity.vo.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "支付渠道更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayChannelUpdateReqVO extends PayChannelCreateReqVO {

    @Schema(description = "渠道编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "渠道编号不能为空")
    private Long channelId;

}
