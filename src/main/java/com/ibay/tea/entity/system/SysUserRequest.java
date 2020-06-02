package com.ibay.tea.entity.system;

import lombok.Data;

import java.util.List;

/**
 * 提交的系统用户
 */
@Data
public class SysUserRequest extends SysUser {
    /** 确认密码 **/
    private String confirmPassword;
    /** 角色名称 **/
    private List<SysRole> sysRoleList;
}
