package com.yxx.common.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yxx.common.core.utils.PageDomainUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询条件对象，用于封装分页请求中的参数
 */
@Data
public class PageQueryEntity implements Serializable
{

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码，默认为1
     */
    @TableField(exist = false)
    @Schema(description = "当前页码，默认为1")
    @JsonIgnore
    private Integer pageNum = 1;

    /**
     * 每页显示的记录数，默认为10
     */
    @TableField(exist = false)
    @Schema(description = "每页显示的记录数，默认为10")
    @JsonIgnore
    private Integer pageSize = 10;

    /**
     * 排序字段名称
     */
    @TableField(exist = false)
    @Schema(description = "排序字段名称")
    @JsonIgnore
    private String orderByColumn;

    /**
     * 排序的方向desc或者asc
     */
    @TableField(exist = false)
    @Schema(description = "排序的方向desc或者asc")
    @JsonIgnore
    private String isAsc = "asc";

    @TableField(exist = false)
    @Schema(description = "是否获取全量数据，默认为false，当为true时，忽略pageNum和pageSize")
    @JsonIgnore
    private Boolean allData = false;

    public void setAllData(Boolean allData) {
        // 兼容PageHelper分页插件
        this.pageSize = 0;
        this.allData = allData;
    }

    public void setPageSize(Integer pageSize) {
        // 兼容PageHelper分页插件
        if (this.getAllData()) {
            this.pageSize = 0;
        } else {
            this.pageSize = pageSize;
        }
    }

    public void setIsAsc(String isAsc) {
        this.isAsc = PageDomainUtils.getIsAscValue(isAsc);
    }
}