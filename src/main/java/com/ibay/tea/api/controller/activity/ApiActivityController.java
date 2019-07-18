package com.ibay.tea.api.controller.activity;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.activity.ApiActivityService;
import com.ibay.tea.api.service.coupons.ApiCouponsService;
import com.ibay.tea.api.service.user.ApiUserService;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.entity.TbActivity;
import com.ibay.tea.entity.TbActivityCouponsRecord;
import com.ibay.tea.entity.TbApiUser;
import com.ibay.tea.entity.TbUserCoupons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * 进入小程序
 */
@RestController
@RequestMapping("api/activity")
public class ApiActivityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiActivityController.class);

    @Resource
    private ApiActivityService apiActivityService;

    @Resource
    private ApiCouponsService apiCouponsService;

    @Resource
    private ApiUserService apiUserService;

    //查询活动，如果当前时间没有活动在进行中查询最近的活动开始时间，点击查看活动奖品
    // 如果在开奖时间内且还有奖品提示用户参与抽奖，若奖品已经发放完毕且用户没有获奖提示用户明天继续参与
    // 若用户已经在当天参与抽奖并获得优惠券提示用户立即使用

    @PostMapping("getActivityInfo")
    public ResultInfo getActivityInfo(@RequestBody Map<String,String> params){
        if (CollectionUtils.isEmpty(params)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        try {
            String oppenId = params.get("oppenId");
            String storeId = params.get("storeId");
            if (StringUtils.isEmpty(oppenId) || StringUtils.isEmpty(storeId)){
                return ResultInfo.newEmptyParamsResultInfo();
            }
            Map<String,Object> result = new HashMap<>();
            TbActivity activityInfo = apiActivityService.getTodayActivity(Integer.valueOf(storeId));
            if (activityInfo == null ){
                return ResultInfo.newEmptyResultInfo();
            }
            if (activityInfo.getActivityType() == ApiConstant.ACTIVITY_TYPE_FULL){
                activityInfo.setShowImageUrl(activityInfo.getStartingPoster());
                result.put("type",5);
                result.put("info",activityInfo);
                resultInfo.setData(result);
                return resultInfo;
            }
            int activityStatus = apiActivityService.checkActivityStatus(activityInfo);
            if (activityStatus == ApiConstant.ACTIVITY_STATUS_NOT_START){
                activityInfo.setStatus(activityStatus);
                apiActivityService.setExtractTime(activityInfo);
                activityInfo.setShowImageUrl(activityInfo.getNoStartPoster());
                result.put("type",1);
                result.put("info",activityInfo);
                resultInfo.setData(result);
                return resultInfo;
            }
            if (activityStatus == ApiConstant.ACTIVITY_STATUS_STARTING){
                //活动正在进行中，查询用户是否有领取过奖品，如果有返回优惠券信息，如果优惠券用户已经使用提示用户明天继续参加抽奖
                // 没有优惠券让用户参与抢优惠券
                Map<String,Object> condition = new HashMap<>();
                condition.put("oppenId",oppenId);
                condition.put("receiveDate", DateUtil.getDateYyyyMMdd());
                TbUserCoupons tbUserCoupons = apiCouponsService.findCouponsByCondition(condition);
                if (tbUserCoupons == null){
                    result.put("type",2);
                    activityInfo.setStatus(activityStatus);
                    activityInfo.setShowImageUrl(activityInfo.getStartingPoster());
                    apiActivityService.setExtractTime(activityInfo);
                    result.put("info",activityInfo);
                    resultInfo.setData(result);
                    return resultInfo;
                }
                if (tbUserCoupons.getStatus() == ApiConstant.USER_COUPONS_STATUS_NO_USE){
                    result.put("type",3);
                    result.put("info",tbUserCoupons);
                    resultInfo.setData(result);
                    return resultInfo;
                }else {
                    result.put("type",4);
                    resultInfo.setData(result);
                }
            }
            if (activityStatus == ApiConstant.ACTIVITY_STATUS_END){
                //活动已经结束，如果有优惠券返回优惠券 没有提示用户明天继续参与抽奖
                TbUserCoupons tbUserCoupons = apiCouponsService.findOneCouponsByOppenId(oppenId);
                if (tbUserCoupons != null){
                    result.put("type",3);
                    result.put("info",tbUserCoupons);
                    resultInfo.setData(result);
                    return resultInfo;
                }else {
                    result.put("type",4);
                    resultInfo.setData(result);
                }
            }
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("getActivityInfo happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @PostMapping("/extractPrize")
    public ResultInfo extractPrize(@RequestBody Map<String,String> params){
        //判断oppenId是否有效
        if (CollectionUtils.isEmpty(params)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");
        String storeId = params.get("storeId");
        if (StringUtils.isEmpty(oppenId) || StringUtils.isEmpty(storeId)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        try {
            TbApiUser tbApiUser = apiUserService.findApiUserByOppenId(oppenId);
            if (tbApiUser == null){
                return ResultInfo.newNoLoginResultInfo();
            }
            //判断通过执行抽奖过程
            TbActivityCouponsRecord record = apiActivityService.extractPrize(oppenId,Integer.valueOf(storeId));
            if (record != null){
                //将用户的优惠券存入数据库
                TbUserCoupons tbUserCoupons = apiActivityService.buildUserCoupons(oppenId,record);
                //设置优惠券过期时间
                Date expireDate = DateUtil.getExpireDate(tbUserCoupons.getReceiveDate(),ApiConstant.USER_COUPONS_EXPIRE_LIMIT);
                tbUserCoupons.setExpireDate(expireDate);

                apiActivityService.saveUserCouponsToDb(tbUserCoupons);
                resultInfo.setData(tbUserCoupons);
                return resultInfo;
            }
            return ResultInfo.newEmptyResultInfo();
        }catch (Exception e){
            LOGGER.error("extractPrize happen exception : {} ",oppenId,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/getJackpotInfo")
    public ResultInfo getJackpotInfo(@RequestBody Map<String,Integer> params){

        if (params == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            int activityId = params.get("activityId");
            List<TbActivityCouponsRecord> recordList = apiActivityService.getJackpotInfo(activityId);
            List<TbActivityCouponsRecord> newList = new ArrayList<>();
            for (TbActivityCouponsRecord record : recordList) {
                TbActivityCouponsRecord copy = record.copy();
                if (copy.getCouponsName().indexOf("免") >=0){
                    copy.setCouponsCount(copy.getCouponsCount() + 10);
                }else{
                    copy.setCouponsCount(copy.getCouponsCount() + 20);
                }
                newList.add(copy);
            }
            resultInfo.setData(newList);
            return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

}
