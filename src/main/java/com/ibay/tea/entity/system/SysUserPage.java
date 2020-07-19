package com.ibay.tea.entity.system;

import lombok.Data;

/**
 * 用户信息分页实体
 */
@Data
public class SysUserPage extends SysUser {

    /**
     * 创建者
     */
    private String createUser;
    /**
     * 修改者
     */
    private String updateUser;
}
