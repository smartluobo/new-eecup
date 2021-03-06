package com.ibay.tea.dao;

import com.ibay.tea.entity.TbApiUser;
import com.ibay.tea.entity.TbApiUserAddress;
import com.ibay.tea.entity.TbUserCoupons;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface TbApiUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbApiUser record);

    TbApiUser selectByPrimaryKey(Integer id);

    TbApiUser findApiUserByOppenId(String oppenId);

    void updateApiUserInfo(@Param("oppenId") String oppenId, @Param("nickName")String nickName, @Param("userHeadImage")String userHeadImage);

    void updateApiUserPhone(@Param("oppenId") String oppenId, @Param("phoneNum") String phoneNum);

    List<TbApiUser> findAll();

    long countUserByCondition(Map<String, Object> condition);

    List<TbApiUser> findUserListByPage(Map<String, Object> condition);

    void bindCompany(TbApiUser tbApiUser);

    void updateGiftReceiveStatus(String oppenId);

    Set<String> findUserPhoneByStoreId(int storeId);

    Set<String> findAllUserPhone();

    List<TbApiUser> findNoBindPhoneUser();

    List<TbApiUser> findNoBindStoreUser();

    void updateApiUserStore(@Param("oppenId") String oppenId, @Param("storeId") int storeId);

    TbApiUser findApiUserById(int apiUserId);
}
