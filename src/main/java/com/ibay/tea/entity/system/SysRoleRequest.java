package com.ibay.tea.entity.system;

import lombok.Data;

import java.util.List;

/**
 * 系统角色实体类
 */
@Data
public class SysRoleRequest extends SysRole {
    /**
     * 关联的角色信息
     */
    private List<SysRole> associatedRoleList;
    /**
     * 菜单信息
     */
    private List<Integer> menuList;
}
