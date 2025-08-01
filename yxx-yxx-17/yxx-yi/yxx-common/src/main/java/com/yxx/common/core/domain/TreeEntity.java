package com.yxx.common.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * Tree基类
 */
public class TreeEntity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 父菜单名称 */
    @TableField(exist = false)
    @Schema(hidden = true)
    private String parentName;

    /** 父菜单ID */
    @TableField(exist = false)
    @Schema(hidden = true)
    private Long parentId;

    /** 显示顺序 */
    @TableField(exist = false)
    @Schema(hidden = true)
    private Integer orderNum;

    /** 祖级列表 */
    @TableField(exist = false)
    @Schema(hidden = true)
    private String ancestors;

    /** 子部门 */
    @TableField(exist = false)
    @Schema(hidden = true)
    private List<?> children = new ArrayList<>();

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Integer getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public String getAncestors()
    {
        return ancestors;
    }

    public void setAncestors(String ancestors)
    {
        this.ancestors = ancestors;
    }

    public List<?> getChildren()
    {
        return children;
    }

    public void setChildren(List<?> children)
    {
        this.children = children;
    }
}
