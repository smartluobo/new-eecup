package com.ibay.tea.cms.service.coupons;

import com.ibay.tea.common.utils.SerialGenerator;
import com.ibay.tea.dao.TbCouponsMapper;
import com.ibay.tea.dao.TbShoppingCardMapper;
import com.ibay.tea.dao.TbUserCouponsMapper;
import com.ibay.tea.entity.TbCoupons;
import com.ibay.tea.entity.TbShoppingCard;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CmsCouponsService {


    @Resource
    private TbCouponsMapper tbCouponsMapper;

    @Resource
    private TbUserCouponsMapper tbUserCouponsMapper;

    @Resource
    private TbShoppingCardMapper tbShoppingCardMapper;
    public List<TbCoupons> findAll() {
        return tbCouponsMapper.findAll();
    }

    public void addCoupons(TbCoupons tbCoupons) {
        tbCouponsMapper.addCoupons(tbCoupons);
    }

    public void deleteCoupons(int id) {
        tbCouponsMapper.deleteCoupons(id);
    }

    public void updateCoupons(TbCoupons tbCoupons) {
        TbCoupons dbCoupons = tbCouponsMapper.selectByPrimaryKey(tbCoupons.getId());
        if (dbCoupons == null){
            return;
        }
        tbCouponsMapper.deleteCoupons(tbCoupons.getId());
        tbCouponsMapper.saveUpdateCoupons(tbCoupons);
    }

    public List<TbCoupons> findUserExperienceCoupons(Map<String, Object> condition) {
        return tbUserCouponsMapper.findUserExperienceCoupons(condition);
    }

    public long countUserExperienceCoupons(Map<String, Object> condition) {
        return tbUserCouponsMapper.countUserExperienceCoupons(condition);
    }

    public void updateExperience(int userCouponsId, int useStatus) {
        tbUserCouponsMapper.updateExperience(userCouponsId,useStatus);
    }

    public boolean generateShoppingCard(int count, int amount,int type) {
        int dbCount = 0;
        TbShoppingCard tbShoppingCard = new TbShoppingCard();
        tbShoppingCard.setAmount(amount);
        tbShoppingCard.setCreateTime(new Date());
        tbShoppingCard.setUpdateTime(new Date());
        while (dbCount < 10000 && count > 0){
            dbCount++;
            if (type == 0){
                String shoppingCardCode = SerialGenerator.getShoppingCardCode(10);
                tbShoppingCard.setCardCode(shoppingCardCode);
            }

            if (type == 1){
                String shoppingCardCode = SerialGenerator.getCashCouponsCode(10);
                tbShoppingCard.setCardCode(shoppingCardCode);
                tbShoppingCard.setType(1);
            }
            try {
                tbShoppingCardMapper.insert(tbShoppingCard);
                count--;
            }catch (Exception e){
                log.error("tbShoppingCardMapper insert happen exception",e);
            }
        }
        log.info("generateShoppingCard save success dbCount : {}",dbCount);
        return true;
    }
}
