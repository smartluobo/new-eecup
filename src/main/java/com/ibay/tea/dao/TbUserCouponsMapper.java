package com.ibay.tea.dao;

import com.ibay.tea.entity.TbCoupons;
import com.ibay.tea.entity.TbUserCoupons;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TbUserCouponsMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(TbUserCoupons record);

    TbUserCoupons selectByPrimaryKey(Integer id);

    TbUserCoupons findCouponsByCondition(Map<String, Object> condition);

    TbUserCoupons findOneCouponsByOppenId(@Param("oppenId") String oppenId,@Param("currentDate") String currentDate);

    void updateStatusById(@Param("id") int id, @Param("status") int status);

    TbUserCoupons selectValidUserCoupons(@Param("oppenId") String oppenId,@Param("id") int id);

    List<TbUserCoupons> findUserValidCoupons(@Param("oppenId") String oppenId,@Param("useWay") String useWay);

    List<TbUserCoupons> getUserCouponsByOppenId(String oppenId);

    TbUserCoupons findCurrentDayUserCoupons(@Param("oppenId") String oppenId, @Param("currentDate") String currentDate,@Param("activityId") String activityId);

    TbUserCoupons findCurrentDayExperienceCoupons(@Param("oppenId") String oppenId, @Param("currentDate") String currentDate,@Param("activityId") String activityId);

    TbUserCoupons findReferrerCoupons(@Param("oppenId") String oppenId);

    void updateRatio(@Param("id") Integer id, @Param("ratioStr") String ratioStr);

    void updateUpgradeCouponsType(@Param("id") Integer id, @Param("couponsType") int couponsType);

    TbUserCoupons findExperienceCoupons(Map<String, Object> condition);

    void insertBatch(List<TbUserCoupons> userCouponsList);

    List<TbCoupons> findUserExperienceCoupons(Map<String, Object> condition);

    long countUserExperienceCoupons(Map<String, Object> condition);

    void updateExperience(@Param("userCouponsId") int userCouponsId, @Param("useStatus") int useStatus);

    TbUserCoupons checkReceiveStatus(@Param("oppenId") String oppenId, @Param("couponsId") String couponsId, @Param("currentDate") String currentDate);

    void updateCashAmount(@Param("id") Integer id, @Param("remainingAmount") double remainingAmount);
}