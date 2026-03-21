package com.yxx.pay.core.entity.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Schema(description = "支付订单 Base VO")
@Data
public class PayOrderBaseVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "用户编号不能为空")
    private Long userId;

    @Schema(description = "商户订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "ORDER_202401010001")
    @NotEmpty(message = "商户订单编号不能为空")
    private String merchantOrderId;

    @Schema(description = "商品标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "iPhone 15 Pro")
    @NotEmpty(message = "商品标题不能为空")
    @Length(max = 32, message = "商品标题不能超过32个字符")
    private String subject;

    @Schema(description = "商品描述", example = "iPhone 15 Pro 256GB 暗夜紫")
    @Length(max = 128, message = "商品描述不能超过128个字符")
    private String body;

    @Schema(description = "支付金额(分)", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @NotNull(message = "支付金额不能为空")
    @Min(value = 1, message = "支付金额必须大于0")
    private Integer payPrice;

    @Schema(description = "订单失效时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "订单失效时间不能为空")
    private LocalDateTime expireTime;

}
