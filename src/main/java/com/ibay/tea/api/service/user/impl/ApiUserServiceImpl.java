package com.ibay.tea.api.service.user.impl;

import com.google.common.cache.LoadingCache;
import com.ibay.tea.api.config.WechatInfoProperties;
import com.ibay.tea.api.service.user.ApiUserService;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.service.SendSmsService;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.dao.TbApiUserMapper;
import com.ibay.tea.dao.TbUserCouponsMapper;
import com.ibay.tea.dao.UserMapper;
import com.ibay.tea.entity.TbApiUser;
import com.ibay.tea.entity.TbUserCoupons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class ApiUserServiceImpl implements ApiUserService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUserServiceImpl.class);

    @Resource
    private TbApiUserMapper tbApiUserMapper;

    @Resource
    LoadingCache<String,String> wechatTokenGuavaCache;

    @Resource
    private WechatInfoProperties wechatInfoProperties;

    @Resource
    private SendSmsService sendSmsService;

    @Resource
    private TbUserCouponsMapper tbUserCouponsMapper;

    @Override
    public TbApiUser findApiUserByOppenId(String oppenId) {
        return tbApiUserMapper.findApiUserByOppenId(oppenId);
    }

    @Override
    public void saveApiUser(String oppenId,String referrerOppenId) {
        TbApiUser apiUserByOppenId = tbApiUserMapper.findApiUserByOppenId(oppenId);
        if (apiUserByOppenId != null){
            return;
        }
        //根据oppenId调用获取用户信息接口获取用户信息
       // Map<String,String> apiUserInfoMap = getApiUserInfoByOppenId(oppenId);
        TbApiUser tbApiUser = new TbApiUser();
        tbApiUser.setOppenId(oppenId);
        tbApiUser.setCreateTime(new Date());
        tbApiUser.setUpdateTime(new Date());
        if (!oppenId.equals(referrerOppenId)){
            tbApiUser.setReferrerOppenId(referrerOppenId);
        }
        LOGGER.info("new user login tbApiUser : {}",tbApiUser);
        tbApiUserMapper.insert(tbApiUser);
        TbUserCoupons coupons = buildUserCoupons(oppenId);
        tbUserCouponsMapper.insert(coupons);
    }

    private TbUserCoupons buildUserCoupons(String oppenId) {
        TbUserCoupons tbUserCoupons = new TbUserCoupons();

        tbUserCoupons.setCouponsName("九折优惠券");
        tbUserCoupons.setCreateTime(new Date());
        tbUserCoupons.setReceiveDate(Integer.valueOf(DateUtil.getDateYyyyMMdd()));
        tbUserCoupons.setStatus(0);
        tbUserCoupons.setCouponsType(ApiConstant.USER_COUPONS_TYPE_RATIO);
        tbUserCoupons.setCouponsRatio("0.9");
        tbUserCoupons.setExpireDate(DateUtil.getExpireDate(Integer.valueOf(DateUtil.getDateYyyyMMdd()),7));
        tbUserCoupons.setIsReferrer(0);
        tbUserCoupons.setUseScope("任意商品");
        tbUserCoupons.setUseRules("全场任意商品可使用，全场折扣下不能使用优惠券");
        tbUserCoupons.setCouponsId(4);
        tbUserCoupons.setOppenId(oppenId);
        return tbUserCoupons;
    }

    @Override
    public void updateApiUserInfo(String oppenId, String nickName, String userHeadImage) {
        tbApiUserMapper.updateApiUserInfo(oppenId,nickName,userHeadImage);
    }

    @Override
    public void bindPhoneNum(String oppenId, String phoneNum, String verificationCode) {
        boolean flag = sendSmsService.checkVerificationCode(phoneNum, verificationCode);
        if (flag){
            tbApiUserMapper.updateApiUserPhone(oppenId,phoneNum);
        }
    }

    private Map<String,String> getApiUserInfoByOppenId(String oppenId) {
        try {
            String token = wechatTokenGuavaCache.get(ApiConstant.WECHAT_ACCESS_TOKEN_GUAVA_KEY);

        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
