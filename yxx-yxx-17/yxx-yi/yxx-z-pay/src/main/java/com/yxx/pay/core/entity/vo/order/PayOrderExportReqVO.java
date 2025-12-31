package com.yxx.pay.core.entity.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 支付订单 Excel 导出 Request VO，参数和 PayOrderPageReqVO 是一致的")
@Data
public class PayOrderExportReqVO {

    @Schema(description = "应用编号", example = "1024")
    private Long appId;

    @Schema(description = "渠道编码", example = "wx_app")
    private String channelCode;

    @Schema(description = "商户订单编号", example = "4096")
    private String merchantOrderId;

    @Schema(description = "渠道编号", example = "1888")
    private String channelOrderNo;

    @Schema(description = "支付单号", example = "2014888")
    private String no;

    @Schema(description = "支付状态", example = "0")
    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime[] createTime;

}
