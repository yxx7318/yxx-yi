package com.yxx.pay.core.entity.dto.refund;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

@Schema(description = "支付退款创建 Request DTO")
@Data
public class PayRefundCreateReqDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "用户编号不能为空")
    private Long userId;

    @Schema(description = "用户IP", example = "127.0.0.1")
    private String userIp;

    @Schema(description = "商户订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "ORDER_202401010001")
    @NotEmpty(message = "商户订单编号不能为空")
    private String merchantOrderId;

    @Schema(description = "商户退款单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "REFUND_202401010001")
    @NotEmpty(message = "商户退款单号不能为空")
    private String merchantRefundId;

    @Schema(description = "退款原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "用户申请退款")
    @NotEmpty(message = "退款原因不能为空")
    @Length(max = 256, message = "退款原因不能超过256个字符")
    private String reason;

    @Schema(description = "退款金额(分)", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @NotNull(message = "退款金额不能为空")
    @Min(value = 1, message = "退款金额必须大于0")
    private Integer price;

}
