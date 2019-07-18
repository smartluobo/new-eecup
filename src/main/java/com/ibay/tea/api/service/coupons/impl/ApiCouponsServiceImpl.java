package com.ibay.tea.api.service.coupons.impl;

import com.ibay.tea.api.service.coupons.ApiCouponsService;
import com.ibay.tea.cache.ActivityCache;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.dao.TbUserCouponsMapper;
import com.ibay.tea.dao.UserCouponsMapper;
import com.ibay.tea.entity.TbCoupons;
import com.ibay.tea.entity.TbUserCoupons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ApiCouponsServiceImpl implements ApiCouponsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCouponsServiceImpl.class);

    @Resource
    private TbUserCouponsMapper tbUserCouponsMapper;

    @Resource
    private ActivityCache activityCache;

    @Override
    public TbUserCoupons findCouponsByCondition(Map<String, Object> condition) {
        return tbUserCouponsMapper.findCouponsByCondition(condition);
    }

    @Override
    public TbUserCoupons findOneCouponsByOppenId(String oppenId) {
        return tbUserCouponsMapper.findOneCouponsByOppenId(oppenId);
    }

    @Override
    public List<TbUserCoupons> findUserValidCoupons(String oppenId) {
        List<TbUserCoupons> userCouponsList = tbUserCouponsMapper.findUserValidCoupons(oppenId);

        if (CollectionUtils.isEmpty(userCouponsList)){
            return null;
        }
        coverUserCoupon(userCouponsList);
        return userCouponsList;
    }

    private void coverUserCoupon(List<TbUserCoupons> userCouponsList) {
        for (TbUserCoupons tbUserCoupons : userCouponsList) {
            TbCoupons tbCouponsById = activityCache.getTbCouponsById((long) tbUserCoupons.getCouponsId());
            tbUserCoupons.setUseRules(tbCouponsById.getUseRules());
            tbUserCoupons.setUseScope(tbCouponsById.getUseScope());
            if (ApiConstant.USER_COUPONS_TYPE_RATIO == tbCouponsById.getCouponsType() || ApiConstant.USER_COUPONS_TYPE_GENERAL == tbCouponsById.getCouponsType()){
                tbUserCoupons.setCouponsType(ApiConstant.USER_COUPONS_TYPE_RATIO);
                String couponsRatio = tbCouponsById.getCouponsRatio();
                LOGGER.info("current ratio :{}",couponsRatio);
                int index = couponsRatio.indexOf(".");
                String bigNumStr = couponsRatio.substring(index+1, index + 2);
                String smallNumStr = "0";
                if (couponsRatio.length() == 4){
                    smallNumStr = couponsRatio.substring(index+2, index + 3);
                }
                LOGGER.info("bigNumStr : {} ,smallNumStr : {}",bigNumStr,smallNumStr);
                tbUserCoupons.setBigNum(Integer.valueOf(bigNumStr));
                tbUserCoupons.setSmallNum(Integer.valueOf(smallNumStr));
            }else if (ApiConstant.USER_COUPONS_TYPE_FREE == tbCouponsById.getCouponsType()){
                tbUserCoupons.setCouponsType(ApiConstant.USER_COUPONS_TYPE_FREE);
            }
        }
    }

    @Override
    public List<TbUserCoupons> getUserCouponsByOppenId(String oppenId) {
        List<TbUserCoupons> userCouponsList = tbUserCouponsMapper.getUserCouponsByOppenId(oppenId);
        if (CollectionUtils.isEmpty(userCouponsList)){
            return null;
        }
        coverUserCoupon(userCouponsList);
        return userCouponsList;
    }
}
