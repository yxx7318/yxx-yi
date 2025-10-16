package com.yxx.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yxx.common.core.domain.BaseColumnEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 角色和部门关联 sys_role_dept
 */
public class SysRoleDept extends BaseColumnEntity
{
    /** 表ID */
    @TableId(type = IdType.AUTO)
    private Long roleDeptId;

    /** 角色ID */
    private Long roleId;
    
    /** 部门ID */
    private Long deptId;

    public Long getRoleDeptId()
    {
        return roleDeptId;
    }

    public void setRoleDeptId(Long roleDeptId)
    {
        this.roleDeptId = roleDeptId;
    }

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleDeptId", getRoleDeptId())
            .append("roleId", getRoleId())
            .append("deptId", getDeptId())
            .append("super=>", super.toString())
            .toString();
    }
}
