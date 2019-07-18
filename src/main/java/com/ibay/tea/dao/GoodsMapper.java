package com.ibay.tea.dao;

import com.ibay.tea.entity.Goods;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    Goods selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(Goods record);

    List<Goods> findGoodsListByPage(Integer pageNum, Integer pageSize);

    long countGoodsByCondition(Map<String,Object> condition);
}