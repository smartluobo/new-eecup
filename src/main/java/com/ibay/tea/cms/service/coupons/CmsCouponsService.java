package com.ibay.tea.cms.service.coupons;

import com.ibay.tea.entity.TbCoupons;

import java.util.List;
import java.util.Map;

public interface CmsCouponsService {
    List<TbCoupons> findAll();

    void addCoupons(TbCoupons tbCoupons);

    void deleteCoupons(int id);

    void updateCoupons(TbCoupons tbCoupons);

    List<TbCoupons> findUserExperienceCoupons(Map<String, Object> condition);

    long countUserExperienceCoupons(Map<String, Object> condition);

    void updateExperience(int userCouponsId, int useStatus);

    boolean generateShoppingCard(int count, int amount,int type);
}
