package com.ibay.tea.dao;

import com.ibay.tea.entity.TbShoppingCard;
import org.springframework.stereotype.Repository;

@Repository
public interface TbShoppingCardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbShoppingCard record);

    TbShoppingCard selectByPrimaryKey(Integer id);

}