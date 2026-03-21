package com.yxx.ai.entity;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxx.common.core.domain.BaseEditDTOEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;
import lombok.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

/**
 * 发票信息EditDTO对象 ai_invoice_info
 *
 * @author yxx
 * @date 2026-01-27
 */
@Schema(description = "发票信息编辑实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
public class AiInvoiceInfoEditDTO extends BaseEditDTOEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "发票号码")
    @NotBlank(message = "发票号码不能为空")
    private String invoiceNumber;

    @Schema(description = "发票金额")
    @NotNull(message = "发票金额不能为空")
    private BigDecimal amount;

    @Schema(description = "开票日期")
    @NotNull(message = "开票日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime invoiceDate;

}
