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

    TbUserCoupons findOneCouponsByOppenId(String oppenId);

    void updateStatusById(@Param("id") int id, @Param("status") int status);

    TbUserCoupons selectValidUserCoupons(@Param("oppenId") String oppenId,@Param("id") int id);

    List<TbUserCoupons> findUserValidCoupons(String oppenId);

    List<TbUserCoupons> getUserCouponsByOppenId(String oppenId);
}