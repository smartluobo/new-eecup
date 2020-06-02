package com.ibay.tea.entity.system;

import lombok.Data;

/**
 * 更新密码字段
 */
@Data
public class UpdatePasswordInfo {
    private String newPassword;
    private String oldPassword;
    private String username;
}
