package com.ibay.tea.cms.service.coupons.impl;

import com.ibay.tea.cms.service.coupons.CmsCouponsService;
import com.ibay.tea.dao.TbCouponsMapper;
import com.ibay.tea.dao.TbUserCouponsMapper;
import com.ibay.tea.entity.TbCoupons;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CmsCouponsServiceImpl implements CmsCouponsService {

    @Resource
    private TbCouponsMapper tbCouponsMapper;

    @Resource
    private TbUserCouponsMapper tbUserCouponsMapper;
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

    @Override
    public List<TbCoupons> findUserExperienceCoupons(Map<String, Object> condition) {
        return tbUserCouponsMapper.findUserExperienceCoupons(condition);
    }

    @Override
    public long countUserExperienceCoupons(Map<String, Object> condition) {
        return tbUserCouponsMapper.countUserExperienceCoupons(condition);
    }

    @Override
    public void updateExperience(int userCouponsId, int useStatus) {
        tbUserCouponsMapper.updateExperience(userCouponsId,useStatus);
    }
}
