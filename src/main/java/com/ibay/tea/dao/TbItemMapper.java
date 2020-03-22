package com.ibay.tea.dao;

import com.ibay.tea.entity.TbItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TbItemMapper {

    TbItem selectByPrimaryKey(Long id);

    List<TbItem> getGoodsListByCategoryId(long categoryId);

    List<TbItem> findAll();

    List<TbItem> findGoodsListByPage(Map<String,Object> condition);

    long countGoodsByCondition(Map<String, Object> condition);

    void deleteGoods(Long id);

    void addGoods(TbItem tbItem);

    void saveUpdateGoods(TbItem tbItem);

    List<TbItem> findGoodsListByCatIds(List<Long> storeCatIds);
}
