package com.ibay.tea.dao;

import com.ibay.tea.entity.TbUserPayRecord;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface TbUserPayRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbUserPayRecord record);

    TbUserPayRecord selectByPrimaryKey(Integer id);

    TbUserPayRecord findPayRecordByOrderId(String orderId);

    void updatePayStatus(Map<String, Object> updateMap);

    void updatePayCloseStatus(Map<String, Object> updateMap);
}