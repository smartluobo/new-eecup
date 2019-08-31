package com.ibay.tea.api.controller.user;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.user.ApiUserService;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.dao.TbApiUserMapper;
import com.ibay.tea.dao.TbUserCouponsMapper;
import com.ibay.tea.entity.TbApiUser;
import com.ibay.tea.entity.TbUserCoupons;
import com.ibay.tea.entity.User;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/user")
public class ApiUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUserController.class);

    @Resource
    private ApiUserService apiUserService;

    @Resource
    private TbApiUserMapper tbApiUserMapper;

    @Resource
    private TbUserCouponsMapper tbUserCouponsMapper;


    @RequestMapping("/reportApiUserInfo")
    public ResultInfo reportApiUserInfo(@RequestBody Map<String,String> params){

        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");
        String nickName = params.get("nickName");
        String userHeadImage = params.get("userHeadImage");
        LOGGER.info("reportApiUserInfo current user oppenId : {}, nickName: {},userHeadImage : {}",oppenId,nickName,userHeadImage);
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            apiUserService.updateApiUserInfo(oppenId,nickName,userHeadImage);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("calculateGoodsOrderPrice GoodsOrderParamVo : {}",params,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/bindPhoneNum")
    public ResultInfo bindPhoneNum(@RequestBody Map<String,String> params){

        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");
        String phoneNum = params.get("phoneNum");
        String verificationCode = params.get("verificationCode");
        LOGGER.info("bindPhoneNum current user oppenId : {}, phoneNum: {},userHeadImage : {}",oppenId,phoneNum,verificationCode);
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            apiUserService.bindPhoneNum(oppenId,phoneNum,verificationCode);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("calculateGoodsOrderPrice GoodsOrderParamVo : {}",params,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/getUserInfo")
    public ResultInfo getUserInfo(@RequestBody Map<String,String> params){

        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            TbApiUser apiUser = apiUserService.getUserInfo(oppenId);
            resultInfo.setData(apiUser);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("getUserInfo params : {}",params,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/receiveNewUserGift")
    public ResultInfo receiveNewUserGift(@RequestBody Map<String,String> params){

        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            TbApiUser apiUser = tbApiUserMapper.findApiUserByOppenId(oppenId);

            if (apiUser == null){
                return ResultInfo.newEmptyResultInfo();
            }
            if (apiUser.getGiftReceiveStatus() == 1){
                resultInfo.setData(2);
            }

            if (apiUser.getGiftReceiveStatus() == 0){
                List<TbUserCoupons> userCouponsList = new ArrayList<>();
                TbUserCoupons tbUserCoupons = new TbUserCoupons();
                tbUserCoupons.setSourceName("新人礼包");
                tbUserCoupons.setCouponsSource(2);
                tbUserCoupons.setUseScope("任意商品");
                tbUserCoupons.setCouponsType(ApiConstant.USER_COUPONS_TYPE_RATIO);
                tbUserCoupons.setCouponsName("五折优惠券");
                tbUserCoupons.setCouponsRatio("0.5");
                tbUserCoupons.setCreateTime(new Date());
                String yyyyMMdd = DateUtils.formatDate(new Date(), "yyyyMMdd");
                tbUserCoupons.setExpireDate(DateUtil.getExpireDate(Integer.valueOf(yyyyMMdd),30));
                tbUserCoupons.setOppenId(oppenId);
                tbUserCoupons.setReceiveDate(Integer.valueOf(yyyyMMdd));
                tbUserCoupons.setStatus(0);
                tbUserCoupons.setUseRules("任意商品可使用，全场折扣下不能使用优惠券");

                userCouponsList.add(tbUserCoupons);
                TbUserCoupons copy1 = tbUserCoupons.copy();
                copy1.setCouponsName("68折优惠券");
                copy1.setCouponsRatio("0.68");
                userCouponsList.add(copy1);

                TbUserCoupons copy2 = tbUserCoupons.copy();
                copy2.setCouponsName("八折优惠券");
                copy2.setCouponsRatio("0.8");
                userCouponsList.add(copy2);

                TbUserCoupons copy3 = tbUserCoupons.copy();
                copy3.setCouponsName("85折优惠券");
                copy3.setCouponsRatio("0.85");
                userCouponsList.add(copy3);

                tbUserCouponsMapper.insertBatch(userCouponsList);
                tbApiUserMapper.updateGiftReceiveStatus(oppenId);
                resultInfo.setData(1);
            }
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("getUserInfo params : {}",params,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

}
