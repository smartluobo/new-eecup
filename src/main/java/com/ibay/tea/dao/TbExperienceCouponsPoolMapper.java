package com.ibay.tea.dao;

import com.ibay.tea.entity.TbExperienceCouponsPool;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbExperienceCouponsPoolMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbExperienceCouponsPool record);

    TbExperienceCouponsPool selectByPrimaryKey(Integer id);

    void insertBatch(List<TbExperienceCouponsPool> pools);

    List<TbExperienceCouponsPool> findCouponsByActivityId(String activityId);

    void updateReceiveStatus(Integer id);
}