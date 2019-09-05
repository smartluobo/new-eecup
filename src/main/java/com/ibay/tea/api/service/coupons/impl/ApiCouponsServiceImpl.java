package com.ibay.tea.api.service.coupons.impl;

import com.ibay.tea.api.service.coupons.ApiCouponsService;
import com.ibay.tea.cache.ActivityCache;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.dao.TbCouponsMapper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ApiCouponsServiceImpl implements ApiCouponsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCouponsServiceImpl.class);

    @Resource
    private TbUserCouponsMapper tbUserCouponsMapper;

    @Resource
   private TbCouponsMapper tbCouponsMapper;

    @Override
    public TbUserCoupons findCouponsByCondition(Map<String, Object> condition) {
        return tbUserCouponsMapper.findCouponsByCondition(condition);
    }

    @Override
    public TbUserCoupons findOneCouponsByOppenId(String oppenId,String currentDate) {
        return tbUserCouponsMapper.findOneCouponsByOppenId(oppenId,currentDate);
    }

    @Override
    public List<TbUserCoupons> findUserValidCoupons(String oppenId) {
        List<TbUserCoupons> userCouponsList = tbUserCouponsMapper.findUserValidCoupons(oppenId);

        if (CollectionUtils.isEmpty(userCouponsList)){
            return new ArrayList<>();
        }
        coverUserCoupon(userCouponsList);
        return userCouponsList;
    }

    private void coverUserCoupon(List<TbUserCoupons> userCouponsList) {
        for (TbUserCoupons tbUserCoupons : userCouponsList) {

            if (ApiConstant.USER_COUPONS_TYPE_RATIO == tbUserCoupons.getCouponsType() || ApiConstant.USER_COUPONS_TYPE_GENERAL == tbUserCoupons.getCouponsType()){
                tbUserCoupons.setCouponsType(ApiConstant.USER_COUPONS_TYPE_RATIO);
                String couponsRatio = tbUserCoupons.getCouponsRatio();
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
            }else if (ApiConstant.USER_COUPONS_TYPE_FREE == tbUserCoupons.getCouponsType()){
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

    @Override
    public TbUserCoupons findCurrentDayUserCoupons(String oppenId, String currentDate,String activityId) {

        return tbUserCouponsMapper.findCurrentDayUserCoupons(oppenId,currentDate,activityId);
    }

    @Override
    public TbUserCoupons findCurrentDayExperienceCoupons(String oppenId, String currentDate,String activityId) {
        return tbUserCouponsMapper.findCurrentDayExperienceCoupons(oppenId,currentDate,activityId);
    }

    @Override
    public List<TbCoupons> getCouponsCenterList() {
        return tbCouponsMapper.getCouponsCenterList();
    }
}
