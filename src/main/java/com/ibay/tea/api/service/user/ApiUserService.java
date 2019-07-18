package com.ibay.tea.api.service.user;

import com.ibay.tea.entity.TbApiUser;

public interface ApiUserService {

    TbApiUser findApiUserByOppenId(String oppenId);

    void saveApiUser(String oppenId);
}
