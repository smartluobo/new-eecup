package com.ibay.tea.dao;

import com.ibay.tea.entity.GoodsSkuRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsSkuRecordMapper {
    int insert(GoodsSkuRecord record);

    void deleteGoodsSkuRecordByPrimaryKey(Integer id);

}