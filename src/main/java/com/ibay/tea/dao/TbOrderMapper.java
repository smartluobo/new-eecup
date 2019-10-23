package com.ibay.tea.dao;

import com.ibay.tea.entity.TbOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TbOrderMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(TbOrder record);

    TbOrder selectByPrimaryKey(String orderId);

    void updateCompleteMessageSendStatus(String orderId, int makeCompleteSendStatus);

    void updateCloseMessageSendStatus(String orderId, int closeSendStatus);

    List<TbOrder> findOrderListByCondition(Map<String, Object> condition);

    void updateOrderStatusByCondition(Map<String, Object> condition);

    long countByCondition(Map<String, Object> condition);

    List<TbOrder> findOrderByOppenId(String oppenId);

    void updatePayStatus(Map<String, Object> updateMap);

    List<TbOrder> findExpireOrder();

    int updateOrderTimeOutClose(String orderId);

    void cancelOrder(@Param("oppenId") String oppenId, @Param("orderId") String orderId);

    Integer findHistoryOrderCount(@Param("oppenId") String oppenId);

    void updateShareOrder(@Param("orderId") String orderId, @Param("oppenId") String oppenId);

    TbOrder findSendOrderByOppenId(String oppenId);

    Map<String,Object> turnoverStatistical(Map<String, Object> condition);

    List<Map<String,Object>> orderStatistical(Map<String, Object> condition);
}