package com.ibay.tea.dao;

import com.ibay.tea.entity.TbShoppingCard;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TbShoppingCardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbShoppingCard record);

    TbShoppingCard selectByPrimaryKey(Integer id);

    TbShoppingCard selectByCouponsCode(@Param("cardCode") String cardCode);

    void updateUseStatusById(@Param("id") int id, @Param("useStatus") int useStatus);
}