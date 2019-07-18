package com.ibay.tea.dao;

import com.ibay.tea.entity.TbOrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbOrderItemMapper {
    int deleteByPrimaryKey(String id);

    int insert(TbOrderItem record);

    TbOrderItem selectByPrimaryKey(int id);

    void insertBatch(List<TbOrderItem> tbOrderItems);

    List<TbOrderItem> findOrderItemByOrderId(String orderId);
}