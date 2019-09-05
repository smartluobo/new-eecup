package com.ibay.tea.api.service.user.impl;

import com.google.common.cache.LoadingCache;
import com.ibay.tea.api.config.WechatInfoProperties;
import com.ibay.tea.api.service.user.ApiUserService;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.service.SendSmsService;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.dao.*;
import com.ibay.tea.entity.TbApiUser;
import com.ibay.tea.entity.TbCoupons;
import com.ibay.tea.entity.TbFavorableCompany;
import com.ibay.tea.entity.TbUserCoupons;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class ApiUserServiceImpl implements ApiUserService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUserServiceImpl.class);

    @Resource
    private TbApiUserMapper tbApiUserMapper;

    @Resource
    private SendSmsService sendSmsService;

    @Resource
    private TbUserCouponsMapper tbUserCouponsMapper;

    @Resource
    private TbFavorableCompanyMapper tbFavorableCompanyMapper;

    @Resource
    private TbCouponsMapper tbCouponsMapper;

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
        tbUserCoupons.setUseRules("全场折扣下不能使用优惠券哦");
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

    @Override
    public long countUserByCondition(Map<String, Object> condition) {
        return tbApiUserMapper.countUserByCondition(condition);
    }

    @Override
    public List<TbApiUser> findUserListByPage(Map<String, Object> condition) {
        return tbApiUserMapper.findUserListByPage(condition);
    }

    @Override
    public boolean bindCompany(Map<String, Integer> params) {
        Integer userId = params.get("userId");
        Integer companyId = params.get("companyId");
        LOGGER.info("api user bindCompany userId : {},companyId : {}",userId,companyId);
        TbApiUser tbApiUser = tbApiUserMapper.selectByPrimaryKey(userId);
        LOGGER.info("tbApiUser info : {}",tbApiUser);
        if (tbApiUser == null){
            return false;
        }
        TbFavorableCompany tbFavorableCompany = tbFavorableCompanyMapper.selectByPrimaryKey(companyId);
        LOGGER.info("tbFavorableCompany : {}",tbFavorableCompany);
        if (tbFavorableCompany == null){
            return false;
        }
        tbApiUser.setCompanyId(companyId);
        tbApiUser.setCompanyName(tbFavorableCompany.getCompanyName());
        tbApiUserMapper.bindCompany(tbApiUser);
        return true;
    }

    @Override
    public TbApiUser getUserInfo(String oppenId) {
        return tbApiUserMapper.findApiUserByOppenId(oppenId);
    }

    @Override
    public boolean receiveCoupons(String oppenId, String couponsId) {

        TbCoupons tbCoupons = tbCouponsMapper.selectByPrimaryKey(Integer.valueOf(couponsId));
        if (tbCoupons == null){
            return false;
        }

        String yyyyMMdd = DateUtils.formatDate(new Date(), "yyyyMMdd");
        TbUserCoupons tbUserCoupons = new TbUserCoupons();
        tbUserCoupons.setOppenId(oppenId);
        tbUserCoupons.setCouponsId(tbCoupons.getId());
        tbUserCoupons.setCouponsName(tbCoupons.getCouponsName());
        tbUserCoupons.setReceiveDate(Integer.valueOf(yyyyMMdd));
        tbUserCoupons.setCreateTime(new Date());
        tbUserCoupons.setStatus(ApiConstant.USER_COUPONS_STATUS_NO_USE);
        tbUserCoupons.setCouponsPoster(tbCoupons.getCouponsPoster());
        tbUserCoupons.setExpireDate(DateUtil.getExpireDate(Integer.valueOf(yyyyMMdd),1));
        tbUserCoupons.setIsReferrer(0);
        tbUserCoupons.setCouponsRatio("0.0");
        tbUserCoupons.setCouponsType(tbCoupons.getCouponsType());
        tbUserCoupons.setUseRules("全场折扣下不可使用哦");
        tbUserCoupons.setUseScope("任意商品");
        tbUserCoupons.setCouponsSource(ApiConstant.COUPONS_SOURCE_RECEIVE);
        tbUserCoupons.setSourceName("每日领券");
        tbUserCoupons.setUseWay(ApiConstant.COUPONS_USE_WAY_APPLET);
        tbUserCoupons.setExpireType(ApiConstant.COUPONS_EXPIRE_TYPE_CURRENT_DAY);
        tbUserCoupons.setConsumeAmount(tbCoupons.getConsumeAmount());
        tbUserCoupons.setReduceAmount(tbCoupons.getReduceAmount());
        tbUserCoupons.setConsumeCount(tbCoupons.getConsumeCount());
        tbUserCoupons.setGiveCount(tbCoupons.getGiveCount());
        tbUserCouponsMapper.insert(tbUserCoupons);
        return true;
    }

    @Override
    public boolean checkReceiveStatus(String oppenId, String couponsId) {
        String currentDate = DateUtil.getDateYyyyMMdd();
        TbUserCoupons tbUserCoupons = tbUserCouponsMapper.checkReceiveStatus(oppenId, couponsId, currentDate);
        return tbUserCoupons == null;
    }

}
