package com.yxx.common.core.domain;

import com.yxx.common.constant.HttpStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 分页数据传输对象，用于封装分页查询的结果
 *
 * @param <T> 实际的数据类型
 */
@Schema(description = "分页结果实体类")
public class PageResult<T> {

    @Schema(description = "总记录数")
    private Long total;

    @Schema(description = "当前页的数据列表")
    private List<T> rows;

    @Schema(description = "状态码", example = "200")
    private Integer code = HttpStatus.SUCCESS;

    @Schema(description = "消息", example = "查询成功")
    private String msg = "查询成功";

    @Schema(description = "页码", example = "1")
    private Integer pageNum;

    @Schema(description = "页大小", example = "10")
    private Integer pageSize;

    @Schema(description = "是否查询全部", example = "false")
    private Boolean allData;

    public Long getTotal() {
        return total;
    }

    public PageResult<T> setTotal(Long total) {
        this.total = total;
        return this;
    }

    public List<T> getRows() {
        return rows;
    }

    public PageResult<T> setRows(List<T> rows) {
        this.rows = rows;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public PageResult<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public PageResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public PageResult<T> setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public PageResult<T> setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Boolean getAllData() {
        return allData;
    }

    public PageResult<T> setAllData(Boolean allData) {
        this.allData = allData;
        return this;
    }
}