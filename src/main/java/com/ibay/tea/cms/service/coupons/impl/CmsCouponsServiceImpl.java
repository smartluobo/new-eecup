package com.ibay.tea.cms.service.coupons.impl;

import com.ibay.tea.cms.service.coupons.CmsCouponsService;
import com.ibay.tea.dao.TbCouponsMapper;
import com.ibay.tea.entity.TbCoupons;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsCouponsServiceImpl implements CmsCouponsService {

    @Resource
    private TbCouponsMapper tbCouponsMapper;
    @Override
    public List<TbCoupons> findAll() {
        return tbCouponsMapper.findAll();
    }

    @Override
    public void addCoupons(TbCoupons tbCoupons) {
        tbCouponsMapper.addCoupons(tbCoupons);
    }

    @Override
    public void deleteCoupons(int id) {
        tbCouponsMapper.deleteCoupons(id);
    }

    @Override
    public void updateCoupons(TbCoupons tbCoupons) {
        TbCoupons dbCoupons = tbCouponsMapper.selectByPrimaryKey(tbCoupons.getId());
        if (dbCoupons == null){
            return;
        }
        tbCouponsMapper.deleteCoupons(tbCoupons.getId());
        tbCouponsMapper.saveUpdateCoupons(tbCoupons);
    }
}
