package com.ibay.tea.entity.system;

import lombok.Data;

/**
 * 用户与角色的关联关系
 */
@Data
public class SysUserRole {
    /**
     * 用户id
     */
    private int userId;
    /**
     * 角色id
     */
    private int roleId;

    public SysUserRole(int userId, int roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
