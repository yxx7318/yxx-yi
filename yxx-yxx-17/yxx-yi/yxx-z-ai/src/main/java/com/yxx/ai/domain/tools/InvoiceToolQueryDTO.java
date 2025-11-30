package com.yxx.ai.domain.tools;

import com.yxx.common.utils.LocalDateUtils;
import org.springframework.ai.tool.annotation.ToolParam;

import java.time.LocalDateTime;

public class InvoiceToolQueryDTO {

    @ToolParam(required = false, description = "发票归属人名称")
    private String createByName;

    @ToolParam(required = false, description = "发票录入开始时间 格式`yyyy-MM-dd HH:mm:ss`")
    private LocalDateTime beginCreateTime;

    @ToolParam(required = false, description = "发票录入结束时间 格式`yyyy-MM-dd HH:mm:ss`")
    private LocalDateTime endCreateTime;

    @ToolParam(required = false, description = "排序字段")
    private Sort sort = new Sort();

    public static class Sort {

        @ToolParam(required = false, description = "排序字段：创建时间`create_time`或者金额`amount`")
        private String orderByColumn;

        @ToolParam(required = false, description = "排序的方向：倒序`desc`或者正序`asc`")
        private String isAsc;

        public String getOrderByColumn() {
            return orderByColumn;
        }

        public void setOrderByColumn(String orderByColumn) {
            this.orderByColumn = orderByColumn;
        }

        public String getIsAsc() {
            return isAsc;
        }

        public void setIsAsc(String isAsc) {
            this.isAsc = isAsc;
        }
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public LocalDateTime getBeginCreateTime() {
        return beginCreateTime;
    }

    public void setBeginCreateTime(String beginCreateTime) {
        this.beginCreateTime = LocalDateUtils.parseToLocalDateTime(beginCreateTime);
    }

    public LocalDateTime getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = LocalDateUtils.parseToLocalDateTime(endCreateTime);
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }
}