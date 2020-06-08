package com.ibay.tea.api.service.user;

import com.ibay.tea.api.service.activity.ApiActivityService;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.service.SendSmsService;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.dao.TbApiUserMapper;
import com.ibay.tea.dao.TbCouponsMapper;
import com.ibay.tea.dao.TbFavorableCompanyMapper;
import com.ibay.tea.dao.TbUserCouponsMapper;
import com.ibay.tea.entity.TbApiUser;
import com.ibay.tea.entity.TbCoupons;
import com.ibay.tea.entity.TbFavorableCompany;
import com.ibay.tea.entity.TbUserCoupons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ApiUserService {


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

    @Resource
    private ApiActivityService apiActivityService;

    public TbApiUser findApiUserByOppenId(String oppenId) {
        return tbApiUserMapper.findApiUserByOppenId(oppenId);
    }

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
        log.info("new user login tbApiUser : {}",tbApiUser);
        tbApiUserMapper.insert(tbApiUser);
    }

    public void updateApiUserInfo(String oppenId, String nickName, String userHeadImage) {
        tbApiUserMapper.updateApiUserInfo(oppenId,nickName,userHeadImage);
    }

    public void bindPhoneNum(String oppenId, String phoneNum, String verificationCode) {
        boolean flag = sendSmsService.checkVerificationCode(phoneNum, verificationCode);
        if (flag){
            tbApiUserMapper.updateApiUserPhone(oppenId,phoneNum);
        }
    }

    public long countUserByCondition(Map<String, Object> condition) {
        return tbApiUserMapper.countUserByCondition(condition);
    }

    public List<TbApiUser> findUserListByPage(Map<String, Object> condition) {
        return tbApiUserMapper.findUserListByPage(condition);
    }

    public boolean bindCompany(Map<String, Integer> params) {
        Integer userId = params.get("userId");
        Integer companyId = params.get("companyId");
        log.info("api user bindCompany userId : {},companyId : {}",userId,companyId);
        TbApiUser tbApiUser = tbApiUserMapper.selectByPrimaryKey(userId);
        log.info("tbApiUser info : {}",tbApiUser);
        if (tbApiUser == null){
            return false;
        }
        TbFavorableCompany tbFavorableCompany = tbFavorableCompanyMapper.selectByPrimaryKey(companyId);
        log.info("tbFavorableCompany : {}",tbFavorableCompany);
        if (tbFavorableCompany == null){
            return false;
        }
        if (tbFavorableCompany.getCouponsType() == 1){
            return userCharge(tbApiUser,tbFavorableCompany);
        }else{
            tbApiUser.setCompanyId(companyId);
            tbApiUser.setCompanyName(tbFavorableCompany.getCompanyName());
            tbApiUserMapper.bindCompany(tbApiUser);
            return true;
        }

    }

    private boolean userCharge(TbApiUser tbApiUser,TbFavorableCompany tbFavorableCompany) {
        if (tbFavorableCompany == null || tbFavorableCompany.getCouponsType() == 0){
            return false;
        }
        TbUserCoupons tbUserCoupons = new TbUserCoupons();
        String yyyyMMdd = DateUtil.getDateYyyyMMdd();
        tbUserCoupons.setOppenId(tbApiUser.getOppenId());
        tbUserCoupons.setCouponsId(tbFavorableCompany.getId());
        tbUserCoupons.setCouponsName("现金券");
        tbUserCoupons.setReceiveDate(Integer.valueOf(yyyyMMdd));
        tbUserCoupons.setCreateTime(new Date());
        tbUserCoupons.setStatus(ApiConstant.USER_COUPONS_STATUS_NO_USE);
        tbUserCoupons.setExpireDate(DateUtil.getExpireDate(Integer.valueOf(yyyyMMdd),360));
        tbUserCoupons.setIsReferrer(0);
        tbUserCoupons.setCouponsRatio("0.0");
        tbUserCoupons.setCouponsType(ApiConstant.USER_COUPONS_TYPE_CASH);
        tbUserCoupons.setUseRules("不可与其他优惠叠加使用");
        tbUserCoupons.setUseScope("任意商品");
        tbUserCoupons.setCouponsSource(ApiConstant.COUPONS_SOURCE_SHOPPING_CARD_RECHARGE);
        tbUserCoupons.setCouponsCode("000000");
        tbUserCoupons.setSourceName("现金充值充值");
        tbUserCoupons.setUseWay(ApiConstant.COUPONS_USE_WAY_APPLET);
        tbUserCoupons.setExpireType(ApiConstant.COUPONS_EXPIRE_TYPE_DEFAULT);
        tbUserCoupons.setCashAmount(String.valueOf(tbFavorableCompany.getActualAmount()));
        apiActivityService.saveUserCouponsToDb(tbUserCoupons);
        return true;

    }

    public TbApiUser getUserInfo(String oppenId) {
        return tbApiUserMapper.findApiUserByOppenId(oppenId);
    }

    public boolean receiveCoupons(String oppenId, String couponsId) {

        TbCoupons tbCoupons = tbCouponsMapper.selectByPrimaryKey(Integer.valueOf(couponsId));
        if (tbCoupons == null){
            return false;
        }

        String yyyyMMdd = DateUtil.getDateYyyyMMdd();
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

    public boolean checkReceiveStatus(String oppenId, String couponsId) {
        String currentDate = DateUtil.getDateYyyyMMdd();
        TbUserCoupons tbUserCoupons = tbUserCouponsMapper.checkReceiveStatus(oppenId, couponsId, currentDate);
        return tbUserCoupons == null;
    }

}
