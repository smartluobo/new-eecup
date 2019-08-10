package com.ibay.tea.api.service.user;

import com.ibay.tea.entity.TbApiUser;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ApiUserService {

    TbApiUser findApiUserByOppenId(String oppenId);

    void saveApiUser(String oppenId,String referrerOppenId);

    void updateApiUserInfo(String oppenId, String nickName, String userHeadImage);

    void bindPhoneNum(String oppenId, String phoneNum, String verificationCode);

    long countUserByCondition(Map<String, Object> condition);

    List<TbApiUser> findUserListByPage(Map<String, Object> condition);
}
