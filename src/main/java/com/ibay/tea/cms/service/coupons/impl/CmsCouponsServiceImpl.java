package com.ibay.tea.cms.service.coupons.impl;

import com.ibay.tea.cms.service.coupons.CmsCouponsService;
import com.ibay.tea.common.utils.SerialGenerator;
import com.ibay.tea.dao.TbCouponsMapper;
import com.ibay.tea.dao.TbShoppingCardMapper;
import com.ibay.tea.dao.TbUserCouponsMapper;
import com.ibay.tea.entity.TbCoupons;
import com.ibay.tea.entity.TbShoppingCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CmsCouponsServiceImpl implements CmsCouponsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsCouponsServiceImpl.class);

    @Resource
    private TbCouponsMapper tbCouponsMapper;

    @Resource
    private TbUserCouponsMapper tbUserCouponsMapper;

    @Resource
    private TbShoppingCardMapper tbShoppingCardMapper;
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

    @Override
    public boolean generateShoppingCard(int count, int amount) {
        int dbCount = 0;
        TbShoppingCard tbShoppingCard = new TbShoppingCard();
        tbShoppingCard.setAmount(amount);
        tbShoppingCard.setCreateTime(new Date());
        tbShoppingCard.setUpdateTime(new Date());
        while (dbCount < 10000 && count > 0){
            dbCount++;
            String shoppingCardCode = SerialGenerator.getShoppingCardCode(10);
            tbShoppingCard.setCardCode(shoppingCardCode);
            try {
                tbShoppingCardMapper.insert(tbShoppingCard);
                count--;
            }catch (Exception e){
                LOGGER.error("tbShoppingCardMapper insert happen exception",e);
            }
        }
        return true;
    }
}
