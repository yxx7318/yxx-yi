package com.yxx.ai.domain.tools;

import com.yxx.common.utils.LocalDateUtils;
import org.springframework.ai.tool.annotation.ToolParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InvoiceToolEditDTO {

    @ToolParam(required = false, description = "发票号码")
    private String invoiceNumber;

    @ToolParam(required = false, description = "发票金额")
    private String amount;

    @ToolParam(required = false, description = "开票日期 格式`yyyy-MM-dd HH:mm:ss`")
    private String invoiceDate;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getAmount() {
        return new BigDecimal(amount);
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public LocalDateTime getInvoiceDate() {
        return LocalDateUtils.parseToLocalDateTime(invoiceDate);
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
}