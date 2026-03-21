package com.yxx.pay.core.entity.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "支付订单提交 Response VO")
@Data
@Builder
public class PayOrderSubmitRespVO {

    @Schema(description = "支付状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status;

    @Schema(description = "展示模式(url/qr_code/form/app)", requiredMode = Schema.RequiredMode.REQUIRED, example = "qr_code")
    private String displayMode;

    @Schema(description = "展示内容(支付URL或二维码内容)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String displayContent;

}
