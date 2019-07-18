package com.ibay.tea.api.service.coupons;

import com.ibay.tea.entity.TbUserCoupons;

import java.util.List;
import java.util.Map;

public interface ApiCouponsService {
    TbUserCoupons findCouponsByCondition(Map<String, Object> condition);

    TbUserCoupons findOneCouponsByOppenId(String oppenId);

    List<TbUserCoupons> findUserValidCoupons(String oppenId);

    List<TbUserCoupons> getUserCouponsByOppenId(String oppenId);
}
