package com.ibay.tea.dao;

import com.ibay.tea.entity.TbApiUserAddress;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TbApiUserAddressMapper {

    int deleteByPrimaryKey(int id);

    int insert(TbApiUserAddress record);

    TbApiUserAddress selectByPrimaryKey(int id);

    List<TbApiUserAddress> findUserAddressByOppenId(@Param("oppenId") String oppenId, @Param("storeId") String storeId);

    void saveUpdateApiUserAddress(TbApiUserAddress tbApiUserAddress);

    void deleteApiUserAddress(Map<String, String> params);

    void updateAddressNotDefault(@Param("oppenId") String oppenId, @Param("storeId") int storeId);
}