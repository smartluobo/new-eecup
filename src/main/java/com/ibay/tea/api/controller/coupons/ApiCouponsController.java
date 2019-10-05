package com.ibay.tea.api.controller.coupons;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.coupons.ApiCouponsService;
import com.ibay.tea.dao.TbShoppingCardMapper;
import com.ibay.tea.entity.TbCoupons;
import com.ibay.tea.entity.TbShoppingCard;
import com.ibay.tea.entity.TbUserCoupons;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/coupons")
public class ApiCouponsController {

    @Resource
    private ApiCouponsService apiCouponsService;

    @Resource
    private TbShoppingCardMapper tbShoppingCardMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCouponsController.class);

    @RequestMapping("/findUserValidCoupons")
    public ResultInfo getUserValidCoupons(@RequestBody Map<String,String> params){

        if (CollectionUtils.isEmpty(params)){
        	return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");
        String useWay = params.get("useWay");
        if (StringUtils.isEmpty(useWay)){
            useWay = "0";
        }

        if (StringUtils.isBlank(oppenId)){
            return ResultInfo.newNoLoginResultInfo();
        }
        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbUserCoupons> userCouponsList = apiCouponsService.findUserValidCoupons(oppenId,useWay);
            LOGGER.info("current user oppen_id : {} ,have {} coupons",oppenId,userCouponsList.size());
            resultInfo.setData(userCouponsList);
        	return resultInfo;
        }catch (Exception e){
            LOGGER.info("current user oppenId : {} findUserValidCoupons happen Exception",oppenId,e);
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/getUserCouponsByOppenId")
    public ResultInfo getUserCouponsByOppenId (@RequestBody Map<String,String> params){

        if (params == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	String oppenId = params.get("oppenId");
        	if (StringUtils.isBlank(oppenId)){
        	    return ResultInfo.newNoLoginResultInfo();
            }
            List<TbUserCoupons> userCouponsList = apiCouponsService.getUserCouponsByOppenId(oppenId);
            resultInfo.setData(userCouponsList);
        	return resultInfo;
        }catch (Exception e){
            LOGGER.error("getUserCouponsByOppenId happen exception",e);
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/getCouponsCenterList")
    public ResultInfo getCouponsCenterList (@RequestBody Map<String,String> params){

        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbCoupons> couponsList = apiCouponsService.getCouponsCenterList();
            resultInfo.setData(couponsList);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("getCouponsCenterList happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/rechargeByShoppingCard")
    public ResultInfo rechargeByShoppingCard (@RequestBody Map<String,String> params){

        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String couponsCode = params.get("couponsCode");
        String oppenId = params.get("oppenId");

        if(StringUtils.isEmpty(couponsCode) || StringUtils.isEmpty(oppenId)){
            return  ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            TbShoppingCard tbShoppingCard = tbShoppingCardMapper.selectByCouponsCode(couponsCode);
            if(tbShoppingCard == null){
                return ResultInfo.newFailResultInfo("充值码有误，请重新输入");
            }
            if (tbShoppingCard.getUseStatus() == 1){
                return ResultInfo.newFailResultInfo("请勿重复充值");
            }
           apiCouponsService.rechargeByShoppingCard(couponsCode,oppenId,tbShoppingCard);
           return resultInfo;
        }catch (Exception e){
            LOGGER.error("getCouponsCenterList happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/findShoppingCardInfo")
    public ResultInfo findShoppingCardInfo (@RequestBody Map<String,String> params) {
        if (params == null) {
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String couponsCode = params.get("couponsCode");
        String oppenId = params.get("oppenId");

        if (StringUtils.isEmpty(couponsCode) || StringUtils.isEmpty(oppenId)) {
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            TbShoppingCard tbShoppingCard = apiCouponsService.queryShoppingCardInfo(couponsCode);
            if (tbShoppingCard == null){
                return ResultInfo.newFailResultInfo("充值码有误，请重新输入");
            }
            resultInfo.setData(tbShoppingCard);
            return resultInfo;
        } catch (Exception e) {
            LOGGER.error("getCouponsCenterList happen exception", e);
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
