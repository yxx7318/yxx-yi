package com.yxx.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yxx.common.core.domain.BaseColumnEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 角色和菜单关联 sys_role_menu
 */
public class SysRoleMenu extends BaseColumnEntity
{
    /** 角色菜单ID */
    @TableId(type = IdType.AUTO)
    private Long roleMenuId;

    /** 角色ID */
    private Long roleId;
    
    /** 菜单ID */
    private Long menuId;

    public Long getRoleMenuId()
    {
        return roleMenuId;
    }

    public void setRoleMenuId(Long roleMenuId)
    {
        this.roleMenuId = roleMenuId;
    }

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getMenuId()
    {
        return menuId;
    }

    public void setMenuId(Long menuId)
    {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleMenuId", getRoleMenuId())
            .append("roleId", getRoleId())
            .append("menuId", getMenuId())
            .append("super=>", super.toString())
            .toString();
    }
}
