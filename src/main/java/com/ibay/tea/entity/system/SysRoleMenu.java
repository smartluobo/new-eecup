package com.ibay.tea.entity.system;

import lombok.Data;

/**
 * 角色与菜单的关联关系
 *
 */
@Data
public class SysRoleMenu {
    /**
     * 角色id
     */
    private int roleId;
    /**
     * 菜单id
     */
    private int menuId;


    public SysRoleMenu() {
    }

    public SysRoleMenu(int roleId, int menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }
}
