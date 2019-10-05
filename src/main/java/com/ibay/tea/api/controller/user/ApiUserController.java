package com.ibay.tea.api.controller.user;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.user.ApiUserService;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.dao.TbApiUserMapper;
import com.ibay.tea.dao.TbUserCouponsMapper;
import com.ibay.tea.entity.TbApiUser;
import com.ibay.tea.entity.TbUserCoupons;
import org.apache.commons.lang3.StringUtils;
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
                return ResultInfo.newFailResultInfo("您已领取过新人礼包，不能重复领取");
            }

            if (apiUser.getGiftReceiveStatus() == 0){
                List<TbUserCoupons> userCouponsList = new ArrayList<>();
                TbUserCoupons tbUserCoupons = new TbUserCoupons();
                String yyyyMMdd = DateUtil.getDateYyyyMMdd();

                tbUserCoupons.setOppenId(oppenId);
                tbUserCoupons.setCouponsId(0);
                tbUserCoupons.setCouponsName("五折优惠券");
                tbUserCoupons.setReceiveDate(Integer.valueOf(yyyyMMdd));
                tbUserCoupons.setCreateTime(new Date());
                tbUserCoupons.setStatus(ApiConstant.USER_COUPONS_STATUS_NO_USE);
                tbUserCoupons.setExpireDate(DateUtil.getExpireDate(Integer.valueOf(yyyyMMdd),30));
                tbUserCoupons.setCouponsRatio("0.5");
                tbUserCoupons.setCouponsType(ApiConstant.USER_COUPONS_TYPE_RATIO);
                tbUserCoupons.setUseRules("全场折扣下不能使用优惠券哦");
                tbUserCoupons.setUseScope("任意商品");
                tbUserCoupons.setCouponsSource(ApiConstant.COUPONS_SOURCE_NEW_USER);
                tbUserCoupons.setSourceName("新人礼包");
                tbUserCoupons.setUseWay(ApiConstant.COUPONS_USE_WAY_APPLET);
                tbUserCoupons.setExpireType(ApiConstant.COUPONS_EXPIRE_TYPE_DEFAULT);

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
            resultInfo.setMsg("新人礼包领取成功，请到优惠券列表查看");
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("getUserInfo params : {}",params,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/receiveCoupons")
    public ResultInfo receiveCoupons (@RequestBody Map<String,String> params){

        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");
        String couponsId = params.get("couponsId");

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            if (StringUtils.isEmpty(oppenId) || StringUtils.isEmpty(couponsId)){
                return ResultInfo.newEmptyParamsResultInfo();
            }
            //判断用户当日是否领取过相同的券且未使用
            boolean receiveStatus = apiUserService.checkReceiveStatus(oppenId,couponsId);
            if (!receiveStatus){
                return ResultInfo.newRepeatResultInfo("您也领取该券且未使用，不可重复领取");
            }

            boolean flag = apiUserService.receiveCoupons(oppenId,couponsId);
            if (flag){
                return resultInfo;
            }
            return ResultInfo.newFailResultInfo();
        }catch (Exception e){
            LOGGER.error("getCouponsCenterList happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

}
