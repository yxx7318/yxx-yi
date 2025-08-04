package com.yxx.common.core.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseColumnEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 创建者Id
     */
    @TableField(value = "create_by_id", fill = FieldFill.INSERT)
    @Schema(description = "创建者Id", hidden = true)
    private Long createById;

    /**
     * 创建者名称
     */
    @TableField(value = "create_by_name", fill = FieldFill.INSERT)
    @Schema(description = "创建者名称", hidden = true)
    private String createByName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Schema(description = "创建时间", hidden = true)
    private Date createTime;

    /**
     * 更新者Id
     */
    @TableField(value = "update_by_id", fill = FieldFill.UPDATE)
    @Schema(description = "更新者Id", hidden = true)
    private Long updateById;

    /**
     * 更新者名称
     */
    @TableField(value = "update_by_name", fill = FieldFill.UPDATE)
    @Schema(description = "更新者名称", hidden = true)
    private String updateByName;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @Schema(description = "更新时间", hidden = true)
    private Date updateTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @Schema(description = "备注")
    private String remark;
}
