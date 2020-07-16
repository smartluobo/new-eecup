package com.ibay.tea.dao.charge;

import com.ibay.tea.entity.charge.UserAccount;

public interface UserAccountMapper {
    UserAccount findByApiUserId(int apiUserId);

    void insert(UserAccount account);
}
