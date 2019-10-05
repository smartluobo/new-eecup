package com.ibay.tea.api.service.coupons;

import com.ibay.tea.entity.TbCoupons;
import com.ibay.tea.entity.TbShoppingCard;
import com.ibay.tea.entity.TbUserCoupons;

import java.util.List;
import java.util.Map;

public interface ApiCouponsService {
    TbUserCoupons findCouponsByCondition(Map<String, Object> condition);

    TbUserCoupons findOneCouponsByOppenId(String oppenId,String currentDate);

    List<TbUserCoupons> findUserValidCoupons(String oppenId,String useWay);

    List<TbUserCoupons> getUserCouponsByOppenId(String oppenId);

    TbUserCoupons findCurrentDayUserCoupons(String oppenId, String currentDate,String activityId);

    TbUserCoupons findCurrentDayExperienceCoupons(String oppenId, String currentDate,String activityId);

    List<TbCoupons> getCouponsCenterList();

    TbShoppingCard queryShoppingCardInfo(String couponsCode);

    boolean rechargeByShoppingCard(String couponsCode, String oppenId,TbShoppingCard tbShoppingCard);
}
