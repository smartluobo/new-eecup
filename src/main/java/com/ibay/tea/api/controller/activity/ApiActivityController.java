package com.ibay.tea.api.controller.activity;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.activity.ApiActivityService;
import com.ibay.tea.api.service.coupons.ApiCouponsService;
import com.ibay.tea.api.service.user.ApiUserService;
import com.ibay.tea.cache.ActivityCache;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.dao.TbActivityMapper;
import com.ibay.tea.dao.TbApiUserMapper;
import com.ibay.tea.dao.TbExperienceCouponsPoolMapper;
import com.ibay.tea.dao.TbUserCouponsMapper;
import com.ibay.tea.entity.*;
import org.apache.http.client.utils.DateUtils;
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

    @Resource
    private ActivityCache activityCache;

    @Resource
    private TbActivityMapper tbActivityMapper;

    @Resource
    private TbUserCouponsMapper tbUserCouponsMapper;

    @Resource
    private TbExperienceCouponsPoolMapper tbExperienceCouponsPoolMapper;

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
            Map<String,Object> condition = new HashMap<>();
            condition.put("currentDate",DateUtil.getDateYyyyMMdd());
            condition.put("storeId",storeId);
            LOGGER.info("params oppenId : {},storeId : {}",oppenId,storeId);
            if (StringUtils.isEmpty(oppenId) || StringUtils.isEmpty(storeId)){
                return ResultInfo.newEmptyParamsResultInfo();
            }
            Map<String,Object> result = new HashMap<>();

            //查看当前店铺是否有买一送一或第二杯半价活动
            TbActivity specialActivity = tbActivityMapper.findSpecialActivity(storeId,DateUtil.getDateYyyyMMdd());
            if (specialActivity != null){
                LOGGER.info("getActivityInfo return specialActivity ");
                specialActivity.setShowImageUrl(specialActivity.getStartingPoster());
                result.put("type",5);
                result.put("info",specialActivity);
                resultInfo.setData(result);
                return resultInfo;
            }

            //查询是否有体验活动
            TbActivity experienceActivity = tbActivityMapper.findExperienceActivity(condition);
            if (experienceActivity != null){
                LOGGER.info("getActivityInfo return experienceActivity ");
                int activityStatus = apiActivityService.checkActivityStatus(experienceActivity);
                experienceActivity.setShowImageUrl(experienceActivity.getNoStartPoster());
                result.put("type",5);
                if (activityStatus == ApiConstant.ACTIVITY_STATUS_STARTING){
                    //判断用户当日是否已经参与了抢券,参与了抢券依然弹活动海报，用户点击抢券提示用户已经参与过抢券或者当日体验券已经抢光
                    experienceActivity.setShowImageUrl(experienceActivity.getStartingPoster());
                    result.put("type",6);
                }

                result.put("info",experienceActivity);
                resultInfo.setData(result);
                return resultInfo;
            }

            //获取常规活动
            TbActivity activityInfo = apiActivityService.getTodayActivity(Integer.valueOf(storeId));
            if (activityInfo == null ){
                return ResultInfo.newEmptyResultInfo();
            }
            if (activityInfo.getActivityType() == ApiConstant.ACTIVITY_TYPE_FULL){
                LOGGER.info("getActivityInfo return ACTIVITY_TYPE_FULL ");
                activityInfo.setShowImageUrl(activityInfo.getStartingPoster());
                result.put("type",5);
                result.put("info",activityInfo);
                resultInfo.setData(result);
                return resultInfo;
            }
            int activityStatus = apiActivityService.checkActivityStatus(activityInfo);
            if (activityStatus == ApiConstant.ACTIVITY_STATUS_NOT_START){
                LOGGER.info("getActivityInfo activity no start");
                activityInfo.setStatus(activityStatus);
                activityInfo.setShowImageUrl(activityInfo.getNoStartPoster());
                result.put("type",1);
                result.put("info",activityInfo);
                resultInfo.setData(result);
                return resultInfo;
            }
            if (activityStatus == ApiConstant.ACTIVITY_STATUS_STARTING){
                //活动正在进行中，查询用户是否有领取过奖品，如果有返回优惠券信息，如果优惠券用户已经使用提示用户明天继续参加抽奖
                result.put("type",2);
                activityInfo.setStatus(activityStatus);
                activityInfo.setShowImageUrl(activityInfo.getStartingPoster());
                result.put("info",activityInfo);
                resultInfo.setData(result);
                return resultInfo;
            }
            if (activityStatus == ApiConstant.ACTIVITY_STATUS_END){
                result.put("type",4);
                resultInfo.setData(result);
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
        String activityId = params.get("activityId");
        LOGGER.info("extractPrize params oppenId : {} ,storeId : {}, activityId : {}",oppenId,storeId,activityId);
        if (StringUtils.isEmpty(oppenId) || StringUtils.isEmpty(storeId)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        try {
            String currentDate = DateUtil.getDateYyyyMMdd();
            TbUserCoupons userCoupons = apiCouponsService.findCurrentDayUserCoupons(oppenId,currentDate,activityId);
            if (userCoupons != null){
                //提示用户已经参与过当日的抢券活动
                return resultInfo;
            }

            //校验用户是否存在
            TbApiUser tbApiUser = apiUserService.findApiUserByOppenId(oppenId);
            if (tbApiUser == null){
                return ResultInfo.newNoLoginResultInfo();
            }
            TodayActivityBean todayActivityBean = activityCache.getTodayActivityBean(Integer.valueOf(storeId));
            if (todayActivityBean == null || todayActivityBean.getTbActivity().getActivityType() == ApiConstant.ACTIVITY_TYPE_FULL){
                return null;
            }

            //判断通过执行抽奖过程
            TbActivityCouponsRecord record = apiActivityService.extractPrize(oppenId,Integer.valueOf(storeId));
            if (record != null){
                //将用户的优惠券存入数据库
                TbUserCoupons tbUserCoupons = apiActivityService.buildUserCoupons(oppenId,record);
                //设置优惠券过期时间
                Date expireDate = DateUtil.getExpireDate(tbUserCoupons.getReceiveDate(),ApiConstant.USER_COUPONS_EXPIRE_LIMIT);
                tbUserCoupons.setExpireDate(expireDate);
                tbUserCoupons.setCouponsSource(ApiConstant.COUPONS_SOURCE_ACTIVITY);
                tbUserCoupons.setSourceName("幸运抽奖");
                tbUserCoupons.setActivityId(Integer.valueOf(activityId));
                tbUserCoupons.setUseWay(0);
                tbUserCoupons.setExpireType(0);
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
            LOGGER.info("getJackpotInfo params activityId : {}",activityId);
            List<TbActivityCouponsRecord> recordList = apiActivityService.getJackpotInfo(activityId);
            LOGGER.info("getJackpotInfo activity coupons type count : {}",recordList.size());
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
            LOGGER.error("getJackpotInfo happen exception ",e);
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/extractExperience")
    public ResultInfo extractExperience(@RequestBody Map<String,String> params){
        //判断oppenId是否有效
        if (CollectionUtils.isEmpty(params)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");
        String storeId = params.get("storeId");
        String activityId = params.get("activityId");
        LOGGER.info("extractExperience params oppenId : {} ,storeId : {},activityId : {}",oppenId,storeId,activityId);
        if (StringUtils.isEmpty(oppenId) || StringUtils.isEmpty(storeId)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        try {
            TbApiUser tbApiUser = apiUserService.findApiUserByOppenId(oppenId);
            if (tbApiUser == null){
                return ResultInfo.newNoLoginResultInfo();
            }
            TbActivity experienceActivity = tbActivityMapper.selectByPrimaryKey(Integer.valueOf(activityId));
            if (experienceActivity == null){
                return null;
            }
            String currentDate = DateUtil.getDateYyyyMMdd();
            TbUserCoupons userCoupons = apiCouponsService.findCurrentDayExperienceCoupons(oppenId,currentDate,activityId);
            if (userCoupons != null){
               //你已参与了抽奖不能重复抽奖
                return resultInfo;
            }
            //判断通过执行抽奖过程
             TbExperienceCouponsPool pool = apiActivityService.extractExperience(activityId);
            if (pool != null){
                //将用户的优惠券存入数据库
                String yyyyMMdd = DateUtils.formatDate(new Date(), "yyyyMMdd");
                TbUserCoupons tbUserCoupons = new TbUserCoupons();
                tbUserCoupons.setOppenId(oppenId);
                tbUserCoupons.setCouponsId(pool.getCouponsId());
                tbUserCoupons.setReceiveDate(Integer.valueOf(yyyyMMdd));
                tbUserCoupons.setCreateTime(new Date());
                tbUserCoupons.setStatus(0);
                tbUserCoupons.setCouponsPoster(pool.getBackgroundUrl());
                tbUserCoupons.setExpireDate(DateUtil.getExpireDate(Integer.valueOf(yyyyMMdd),1));
                tbUserCoupons.setIsReferrer(0);

                tbUserCoupons.setActivityId(Integer.valueOf(activityId));
                tbUserCoupons.setSourceName("幸运抽奖");
                tbUserCoupons.setCouponsSource(0);
                tbUserCoupons.setUseScope("任意商品");
                tbUserCoupons.setCouponsType(ApiConstant.USER_COUPONS_TYPE_EXPERIENCE);
                tbUserCoupons.setCouponsName("免费券");



                tbUserCoupons.setOppenId(oppenId);


                tbUserCoupons.setUseRules("到店使用，全场任意商品有效");
                tbUserCoupons.setCouponsCode(pool.getCouponsCode());

                apiActivityService.saveUserCouponsToDb(tbUserCoupons);
                tbExperienceCouponsPoolMapper.updateReceiveStatus(pool.getId());
                resultInfo.setData(tbUserCoupons);
                return resultInfo;
            }
            return ResultInfo.newEmptyResultInfo();
        }catch (Exception e){
            LOGGER.error("extractPrize happen exception : {} ",oppenId,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
