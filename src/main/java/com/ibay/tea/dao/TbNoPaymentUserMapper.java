package com.ibay.tea.dao;

import com.ibay.tea.entity.TbNoPaymentUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TbNoPaymentUserMapper {

    TbNoPaymentUser getNoPaymentUser(@Param("oppenId") String oppenId, @Param("storeId") int storeId);

    long countByCondition(Map<String, Object> condition);

    List<TbNoPaymentUser> findAllByCondition(Map<String, Object> condition);

    void insert(TbNoPaymentUser tbNoPaymentUser);

    void deleteById(int id);

    List<TbNoPaymentUser> findByStoreId(int storeId);
}
