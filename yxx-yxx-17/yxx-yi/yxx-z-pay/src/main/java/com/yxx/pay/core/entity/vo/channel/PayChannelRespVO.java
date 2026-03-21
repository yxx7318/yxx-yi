package com.yxx.pay.core.entity.vo.channel;

import com.yxx.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Schema(description = "支付渠道 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayChannelRespVO extends PayChannelBaseVO {

    @Schema(description = "渠道编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @Excel(name = "渠道编号")
    private Long channelId;

    @Schema(description = "渠道配置(JSON格式)")
    private String channelConfig;

    @Schema(description = "创建时间")
    @Excel(name = "创建时间")
    private LocalDateTime createTime;

}
