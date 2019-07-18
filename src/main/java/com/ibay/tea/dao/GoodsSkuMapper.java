package com.ibay.tea.dao;

import com.ibay.tea.entity.GoodsSku;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsSkuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsSku record);

    GoodsSku selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(GoodsSku record);
}