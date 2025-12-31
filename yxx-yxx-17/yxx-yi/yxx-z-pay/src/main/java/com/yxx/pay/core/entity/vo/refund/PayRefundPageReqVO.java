package com.yxx.pay.core.entity.vo.refund;

import com.yxx.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 退款订单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PayRefundPageReqVO extends BaseEntity {

    @Schema(description = "应用编号", example = "1024")
    private Long appId;

    @Schema(description = "渠道编码", example = "wx_app")
    private String channelCode;

    @Schema(description = "商户支付单号", example = "10")
    private String merchantOrderId;

    @Schema(description = "商户退款单号", example = "20")
    private String merchantRefundId;

    @Schema(description = "渠道支付单号", example = "30")
    private String channelOrderNo;

    @Schema(description = "渠道退款单号", example = "40")
    private String channelRefundNo;

    @Schema(description = "退款状态", example = "0")
    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
//    private LocalDateTime[] createTime;
    private LocalDateTime[] createTimes;

}
