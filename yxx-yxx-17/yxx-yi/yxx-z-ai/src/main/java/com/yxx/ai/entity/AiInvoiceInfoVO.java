package com.yxx.ai.entity;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxx.common.annotation.Excel;
import com.yxx.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;
import lombok.*;

/**
 * 发票信息VO对象 ai_invoice_info
 *
 * @author yxx
 * @date 2025-11-30
 */
@Schema(description = "发票信息VO实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
public class AiInvoiceInfoVO extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "发票id")
    private Long invoiceInfoId;

    @Schema(description = "发票号码")
    @Excel(name = "发票号码")
    private String invoiceNumber;

    @Schema(description = "发票金额")
    @Excel(name = "发票金额")
    private BigDecimal amount;

    @Schema(description = "开票日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开票日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime invoiceDate;

}
