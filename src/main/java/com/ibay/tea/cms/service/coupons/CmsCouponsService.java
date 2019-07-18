package com.ibay.tea.cms.service.coupons;

import com.ibay.tea.entity.TbCoupons;

import java.util.List;

public interface CmsCouponsService {
    List<TbCoupons> findAll();

    void addCoupons(TbCoupons tbCoupons);

    void deleteCoupons(int id);

    void updateCoupons(TbCoupons tbCoupons);
}
