package com.yxx.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yxx.common.core.domain.BaseColumnEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户和角色关联 sys_user_role
 */
public class SysUserRole extends BaseColumnEntity
{
    /** 用户角色ID */
    @TableId(type = IdType.AUTO)
    private Long userRoleId;

    /** 用户ID */
    private Long userId;
    
    /** 角色ID */
    private Long roleId;

    public Long getUserRoleId()
    {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId)
    {
        this.userRoleId = userRoleId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userRoleId", getUserRoleId())
            .append("userId", getUserId())
            .append("roleId", getRoleId())
            .toString();
    }
}
