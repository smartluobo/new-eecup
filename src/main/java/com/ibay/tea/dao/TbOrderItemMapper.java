package com.ibay.tea.dao;

import com.ibay.tea.entity.TbOrderItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TbOrderItemMapper {
    int deleteByPrimaryKey(String id);

    int insert(TbOrderItem record);

    TbOrderItem selectByPrimaryKey(int id);

    void insertBatch(List<TbOrderItem> tbOrderItems);

    List<TbOrderItem> findOrderItemByOrderId(String orderId);

    long countShufuleiCondition(Map<String, Object> condition);

    List<TbOrderItem> findShufuleiListByPage(Map<String, Object> condition);

    List<Map<String, Object>> shufuleiCountStatistical(Map<String, Object> condition);
}
