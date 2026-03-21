package com.yxx.pay.core.entity.vo.order;

import com.yxx.common.core.domain.PageQueryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Schema(description = "支付订单分页查询 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayOrderPageReqVO extends PageQueryEntity {

    @Schema(description = "支付订单号", example = "PAY202401010001")
    private String orderNo;

    @Schema(description = "用户编号", example = "1")
    private Long userId;

    @Schema(description = "商户订单编号", example = "ORDER_202401010001")
    private String merchantOrderId;

    @Schema(description = "渠道编码", example = "wx_pub")
    private String channelCode;

    @Schema(description = "支付状态(0待支付 10成功 20退款 30关闭)", example = "0")
    private Integer orderStatus;

    @Schema(description = "创建时间-开始")
    private LocalDateTime createTimeBegin;

    @Schema(description = "创建时间-结束")
    private LocalDateTime createTimeEnd;

}
