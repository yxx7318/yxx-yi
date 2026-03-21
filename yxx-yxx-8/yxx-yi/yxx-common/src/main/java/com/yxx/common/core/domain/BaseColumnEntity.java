package com.yxx.common.core.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxx.common.core.domain.model.LoginUser;
import com.yxx.common.core.utils.FieldFillUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    private LocalDateTime createTime;

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
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @Schema(description = "备注")
    private String remark;

    /**
     * 填充插入时的公共字段
     */
    public void fieldFillInsert() {
        FieldFillUtils.fieldFillInsert(this);
    }

    /**
     * 填充插入时的公共字段
     */
    public void fieldFillUpdate() {
        FieldFillUtils.fieldFillUpdate(this);
    }

    /**
     * 填充插入时的公共字段通过登录数据
     */
    public void fieldFillInsertByLoginUser(LoginUser loginUser) {
        FieldFillUtils.fieldFillInsertByLoginUser(this, loginUser);
    }

    /**
     * 填充插入时的公共字段通过登录数据
     */
    public void fieldFillUpdateByLoginUser(LoginUser loginUser) {
        FieldFillUtils.fieldFillUpdateByLoginUser(this, loginUser);
    }

    public Long getCreateById() {
        return createById;
    }

    public void setCreateById(Long createById) {
        this.createById = createById;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateById() {
        return updateById;
    }

    public void setUpdateById(Long updateById) {
        this.updateById = updateById;
    }

    public String getUpdateByName() {
        return updateByName;
    }

    public void setUpdateByName(String updateByName) {
        this.updateByName = updateByName;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
