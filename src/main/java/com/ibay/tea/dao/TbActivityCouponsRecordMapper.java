package com.ibay.tea.dao;

import com.ibay.tea.entity.TbActivityCouponsRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbActivityCouponsRecordMapper {
    int deleteByPrimaryKey(Integer id);

    TbActivityCouponsRecord selectByPrimaryKey(Integer id);

    List<TbActivityCouponsRecord> findAll();

    List<TbActivityCouponsRecord> getJackpotInfo(int activityId);

    List<TbActivityCouponsRecord> findCouponsByActivityId(int activityId);

    void insert(TbActivityCouponsRecord tbActivityCouponsRecord);

    void updateRecord(TbActivityCouponsRecord tbActivityCouponsRecord);

    void activityDeleteCoupons(@Param("activityId") int activityId, @Param("id") int id);
}