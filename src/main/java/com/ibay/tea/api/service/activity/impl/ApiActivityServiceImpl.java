package com.ibay.tea.api.service.activity.impl;

import com.ibay.tea.api.service.activity.ApiActivityService;
import com.ibay.tea.cache.ActivityCache;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.dao.TbActivityCouponsRecordMapper;
import com.ibay.tea.dao.TbActivityMapper;
import com.ibay.tea.dao.TbExperienceCouponsPoolMapper;
import com.ibay.tea.dao.TbUserCouponsMapper;
import com.ibay.tea.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class ApiActivityServiceImpl implements ApiActivityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiActivityServiceImpl.class);

    private static final String LOCK_STRING = "LOCK_STRING";

    private static final String INIT_EXPERIENCE_COUPONS_LOCK = "init_experience_Coupons_lock";

    @Resource
    private TbActivityMapper tbActivityMapper;

    @Resource
    private ActivityCache activityCache;

    @Resource
    private TbUserCouponsMapper tbUserCouponsMapper;

    @Resource
    private TbActivityCouponsRecordMapper tbActivityCouponsRecordMapper;

    @Resource
    private TbExperienceCouponsPoolMapper tbExperienceCouponsPoolMapper;

    @Override
    public TbActivity getTodayActivity(int storeId) {
        TbActivity activity = activityCache.getTodayActivity(storeId);
        if (activity != null){
            return activity.copy();
        }
        return null;
    }

    @Override
    public TbActivityCouponsRecord extractPrize(String oppenId,int storeId) {
        TodayActivityBean todayActivityBean = activityCache.getTodayActivityBean(storeId);
        TbActivityCouponsRecord record = null;
        if (todayActivityBean == null || todayActivityBean.getTbActivity().getActivityType() == ApiConstant.ACTIVITY_TYPE_FULL){
            return null;
        }
        List<TbActivityCouponsRecord> tbActivityCouponsRecordList = todayActivityBean.getTbActivityCouponsRecordList();
        if (CollectionUtils.isEmpty(tbActivityCouponsRecordList)){
            return null;
        }
        Random random = new Random();
        synchronized (LOCK_STRING){
            for (int i = 0 ;i < 5;i++){
                int index = random.nextInt(tbActivityCouponsRecordList.size());
                TbActivityCouponsRecord tbActivityCouponsRecord = tbActivityCouponsRecordList.get(index);
                if (tbActivityCouponsRecord.getCouponsCount() > 0){
                    tbActivityCouponsRecord.setCouponsCount(tbActivityCouponsRecord.getCouponsCount()-1);
                    LOGGER.info("current user : {} extract {}, coupons inventory : {}",oppenId,tbActivityCouponsRecord.getCouponsName(),tbActivityCouponsRecord.getCouponsCount());
                    record = tbActivityCouponsRecord;
                    break;
                }
            }
        }
        if (record != null){
            //表示已经抽取到优惠券
            return record;
        }else{
            LOGGER.error("current day activity coupons already grab starting extract general coupons ");
            //查看是否定义了通用券，如果又直接返回通用券
            TbCoupons generalCoupons = activityCache.getGeneralCoupons();
            if (generalCoupons != null){
                return buildRecord(todayActivityBean.getTbActivity(),generalCoupons);
            }
        }
        return null;
    }

    @Override
    public void saveUserCouponsToDb(TbUserCoupons tbUserCoupons) {
        tbUserCouponsMapper.insert(tbUserCoupons);
    }

    @Override
    public TbUserCoupons buildUserCoupons(String oppenId, TbActivityCouponsRecord record) {
        int couponsId = record.getCouponsId();
        TbCoupons tbCoupons = activityCache.getTbCouponsById((long) couponsId);
        TbUserCoupons tbUserCoupons = new TbUserCoupons();
        tbUserCoupons.setOppenId(oppenId);
        tbUserCoupons.setCouponsId(couponsId);
        tbUserCoupons.setCouponsName(tbCoupons.getCouponsName());
        tbUserCoupons.setReceiveDate(Integer.valueOf(DateUtil.getDateYyyyMMdd()));
        tbUserCoupons.setCreateTime(new Date());
        tbUserCoupons.setStatus(0);
        tbUserCoupons.setCouponsPoster(tbCoupons.getCouponsPoster());
        tbUserCoupons.setIsReferrer(0);
        tbUserCoupons.setCouponsRatio(tbCoupons.getCouponsRatio());
        tbUserCoupons.setCouponsType(tbCoupons.getCouponsType());
        tbUserCoupons.setUseRules(tbCoupons.getUseRules());
        tbUserCoupons.setUseScope(tbCoupons.getUseScope());
        return tbUserCoupons;
    }

    private TbActivityCouponsRecord buildRecord(TbActivity tbActivity, TbCoupons generalCoupons) {

        TbActivityCouponsRecord record = new TbActivityCouponsRecord();
        record.setActivityId(tbActivity.getId());
        record.setActivityName(tbActivity.getActivityName());
        record.setCouponsId(generalCoupons.getId());
        record.setCouponsName(generalCoupons.getCouponsName());
        return record;
    }

    @Override
    public int checkActivityStatus(TbActivity tbActivity) {
        int hour = DateUtil.getHour();
        if (tbActivity.getStartHour() > hour){
            //活动未开始
            return ApiConstant.ACTIVITY_STATUS_NOT_START;
        }
        if (tbActivity.getStartHour() <= hour && tbActivity.getEndHour() > hour){
            //活动正在进行中
            return ApiConstant.ACTIVITY_STATUS_STARTING;
        }

        return ApiConstant.ACTIVITY_STATUS_END;
    }

    @Override
    public void setExtractTime(TbActivity activityInfo) {
        activityInfo.setExtractTime("开抢时间: "+activityInfo.getStartHour()+":00 -- "+activityInfo.getEndHour()+":00");
    }

    @Override
    public List<TbActivityCouponsRecord> getJackpotInfo(int activityId) {
        List<TbActivityCouponsRecord> couponsRecords = tbActivityCouponsRecordMapper.getJackpotInfo(activityId);
        if (CollectionUtils.isEmpty(couponsRecords)){
            return null;
        }
        for (TbActivityCouponsRecord couponsRecord : couponsRecords) {
            TbCoupons tbCouponsById = activityCache.getTbCouponsById((long) couponsRecord.getCouponsId());
            couponsRecord.setUseRules(tbCouponsById.getUseRules());
            couponsRecord.setUseScope(tbCouponsById.getUseScope());
            if (ApiConstant.USER_COUPONS_TYPE_RATIO == tbCouponsById.getCouponsType() || ApiConstant.USER_COUPONS_TYPE_GENERAL == tbCouponsById.getCouponsType()){
                couponsRecord.setCouponsType(ApiConstant.USER_COUPONS_TYPE_RATIO);
                String couponsRatio = tbCouponsById.getCouponsRatio();
                int index = couponsRatio.indexOf(".");
                String bigNumStr = couponsRatio.substring(index+1, index + 2);
                String smallNumStr = "0";
                if (couponsRatio.length() == 4){
                    smallNumStr = couponsRatio.substring(index+2, index + 3);
                }
                couponsRecord.setBigNum(Integer.valueOf(bigNumStr));
                couponsRecord.setSmallNum(Integer.valueOf(smallNumStr));
            }else if (ApiConstant.USER_COUPONS_TYPE_FREE == tbCouponsById.getCouponsType()){
                couponsRecord.setCouponsType(ApiConstant.USER_COUPONS_TYPE_FREE);
            }
        }
        return couponsRecords;
    }

    @Override
    public TbExperienceCouponsPool extractExperience(String activityId) {
        List<TbExperienceCouponsPool> tbExperienceCouponsPools = activityCache.getExperienceCouponsPoolCacheMap().get(activityId);
        if (tbExperienceCouponsPools == null){
            synchronized (INIT_EXPERIENCE_COUPONS_LOCK){
                tbExperienceCouponsPools = activityCache.getExperienceCouponsPoolCacheMap().get(activityId);
                if (tbExperienceCouponsPools == null){
                    List<TbExperienceCouponsPool> poolsList = tbExperienceCouponsPoolMapper.findCouponsByActivityId(activityId);
                    if (poolsList == null){
                        poolsList = new ArrayList<>();
                    }
                    activityCache.getExperienceCouponsPoolCacheMap().put(activityId,poolsList);
                }
            }
        }
        tbExperienceCouponsPools = activityCache.getExperienceCouponsPoolCacheMap().get(activityId);

        if (CollectionUtils.isEmpty(tbExperienceCouponsPools)){
            return null;
        }
        synchronized (tbExperienceCouponsPools){
            if (tbExperienceCouponsPools.size() > 0){
                return tbExperienceCouponsPools.remove(0);
            }
        }
        return null;
    }
}
