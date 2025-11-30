package com.yxx.ai.entity;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.yxx.common.annotation.Excel;
import com.yxx.common.core.domain.BaseColumnEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;
import lombok.*;

/**
 * 发票信息DO对象 ai_invoice_info
 *
 * @author yxx
 * @date 2025-11-30
 */
@Schema(description = "发票信息DO实体")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Data
@TableName(value = "ai_invoice_info", autoResultMap = true)
public class AiInvoiceInfoDO extends BaseColumnEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "发票id")
    @TableId(value = "invoice_info_id", type = IdType.AUTO)
    private Long invoiceInfoId;

    @Schema(description = "发票号码")
    @TableField("invoice_number")
    @Excel(name = "发票号码")
    private String invoiceNumber;

    @Schema(description = "发票金额")
    @TableField("amount")
    @Excel(name = "发票金额")
    private BigDecimal amount;

    @Schema(description = "开票日期")
    @TableField("invoice_date")
    @Excel(name = "开票日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime invoiceDate;

}
