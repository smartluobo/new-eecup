package com.ibay.tea.entity.system;

import lombok.Data;

/**
 * 角色信息分页返回信息
 */
@Data
public class SysRolePage extends SysRole {
    /**
     * 更新用户名称
     */
    private String updateUser;
}
