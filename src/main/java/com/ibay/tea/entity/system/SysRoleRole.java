package com.ibay.tea.entity.system;

import lombok.Data;

/**
 * 与角色关联的角色信息
 */
@Data
public class SysRoleRole {
    /**
     * 物理主键
     */
    private Integer id;
    /**
     * 角色id
     */
    private Integer roleId;
    /**
     * 关联角色id
     */
    private Integer associatedRoleId;


    public SysRoleRole() {
    }

    public SysRoleRole(Integer roleId, Integer associatedRoleId) {
        this.roleId = roleId;
        this.associatedRoleId = associatedRoleId;
    }
}
