package com.ibay.tea.api.service.user;

import com.ibay.tea.entity.TbApiUser;
import org.apache.ibatis.annotations.Param;

public interface ApiUserService {

    TbApiUser findApiUserByOppenId(String oppenId);

    void saveApiUser(String oppenId,String referrerOppenId);

    void updateApiUserInfo(String oppenId, String nickName, String userHeadImage);
}
