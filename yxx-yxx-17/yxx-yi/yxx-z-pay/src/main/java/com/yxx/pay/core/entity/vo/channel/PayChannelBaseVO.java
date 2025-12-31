package com.yxx.pay.core.entity.vo.channel;

import com.yxx.common.enums.CommonStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
* 支付渠道 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class PayChannelBaseVO {

    @Schema(description = "开启状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "开启状态不能为空")
    private CommonStatusEnum status;

    @Schema(description = "备注", example = "我是小备注")
    private String remark;

    @Schema(description = "渠道费率，单位：百分比", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "渠道费率，单位：百分比不能为空")
    private Double feeRate;

    @Schema(description = "应用编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "应用编号不能为空")
    private Long appId;

}
