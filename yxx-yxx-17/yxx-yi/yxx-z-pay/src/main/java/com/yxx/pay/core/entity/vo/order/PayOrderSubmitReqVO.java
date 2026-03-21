package com.yxx.pay.core.entity.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Schema(description = "支付订单提交 Request VO")
@Data
public class PayOrderSubmitReqVO {

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "订单编号不能为空")
    private Long orderId;

    @Schema(description = "渠道编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "wx_pub")
    @NotEmpty(message = "渠道编码不能为空")
    private String channelCode;

    @Schema(description = "用户IP", example = "127.0.0.1")
    private String userIp;

    @Schema(description = "渠道额外参数", example = "{\"openid\":\"oUpF8uMuAJ...\"}")
    private Map<String, String> channelExtras;

}
