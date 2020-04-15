package com.ibay.tea.dao;

import com.ibay.tea.entity.TbItem;
import com.ibay.tea.entity.TbStoreGoods;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TbStoreGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbStoreGoods record);

    TbStoreGoods selectByPrimaryKey(Integer id);

    List<TbStoreGoods> findRecordByCondition(Map<String, Object> condition);

    void updateInventory(@Param("goodsId") long itemId, @Param("storeId") int storeId, @Param("num") int num);

    List<TbStoreGoods> findAllByStoreId(Map<String,Object> params);

    void insertBatch(List<TbStoreGoods> storeGoodsList);

    void deleteByStoreId(int storeId);

    Integer findGoodsInventory(@Param("goodsId") Long goodsId, @Param("storeId") int storeId);

    void clearStoreGoods(int storeId);

    List<Long> findGoodsIdByStoreId(Long storeId);

    long countByCondition(Map<String, Object> condition);

    void updateInventoryByStoreId(int storeId);

    void updateInventoryByStoreGoods(TbStoreGoods storeGoods);
}
