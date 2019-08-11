package com.ibay.tea.cache;

import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.dao.TbActivityCouponsRecordMapper;
import com.ibay.tea.dao.TbActivityMapper;
import com.ibay.tea.dao.TbCouponsMapper;
import com.ibay.tea.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ActivityCache implements InitializingBean{

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityCache.class);

    @Resource
    private TbActivityMapper tbActivityMapper;

    @Resource
    private TbCouponsMapper tbCouponsMapper;

    @Resource
    private TbActivityCouponsRecordMapper tbActivityCouponsRecordMapper;

    @Resource
    private StoreCache storeCache;

    //当日活动
    private List<TbActivity> todayActivityList = new ArrayList<>();

    //今日活动对象 包含了活动关联的优惠券信息
    private List<TodayActivityBean> todayActivityBeans = new ArrayList<>();

    private List<TbCoupons> couponsListCache;

    //通用券，当用户参与活动为抢到优惠券时返回通用优惠券
    private TbCoupons generalCoupons;

    private List<TbActivityCouponsRecord> tbActivityCouponsRecordsCache;





    private void initActivityCache(){
        List<TbStore> storeList = storeCache.getStoreList();
        if (!CollectionUtils.isEmpty(storeList)){
            for (TbStore store : storeList) {
                //如果有及节日活动优先选择当天活动为节假日活动
                TbActivity tbActivity = tbActivityMapper.findFullActivity(DateUtil.getDateYyyyMMdd(),store.getId());
                if (tbActivity == null){
                    tbActivity = tbActivityMapper.findHolidayActivity(DateUtil.getDateYyyyMMdd(),store.getId());
                }
                if(tbActivity == null){
                    //没有节假日活动查询常规活动
                    tbActivity = tbActivityMapper.findRegularActivity(DateUtil.getDateYyyyMMdd(),store.getId());
                }
                if (tbActivity != null){
                    todayActivityList.add(tbActivity);
                }
            }
        }

        couponsListCache = tbCouponsMapper.findAll();
        tbActivityCouponsRecordsCache = tbActivityCouponsRecordMapper.findAll();
        if (todayActivityList.size() > 0){
            buildTodayActivityInfoBean();
        }

        initGeneralCoupons();
        if (!CollectionUtils.isEmpty(todayActivityList)){
            for (TbActivity tbActivity : todayActivityList) {
                LOGGER.info("today activity info :{}",tbActivity);
                List<TbActivityCouponsRecord> records = getActivityCouponsRecordsByActivityId(tbActivity.getId());
                if (!CollectionUtils.isEmpty(records)){
                    for (TbActivityCouponsRecord record : records) {
                        LOGGER.info("activityId : {},recordInfo : {}",tbActivity.getId(),record);
                    }
                }
            }
        }
    }

    private void initGeneralCoupons() {
        for (TbCoupons tbCoupons : couponsListCache) {
            if (tbCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_GENERAL){
                generalCoupons = tbCoupons;
                break;
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initActivityCache();
    }

    public TbActivity getTodayActivity(int storeId) {
        for (TbActivity tbActivity : todayActivityList) {
            if (tbActivity.getStoreId() == storeId){
                return tbActivity;
            }
        }
        return null;
    }

    public void buildTodayActivityInfoBean(){

        if (!CollectionUtils.isEmpty(todayActivityList)){

            for (TbActivity tbActivity : todayActivityList) {
                TodayActivityBean todayActivityBean = new TodayActivityBean();
                todayActivityBean.setTbActivity(tbActivity);
                setTodayActivityCouponsRecordList(todayActivityBean,tbActivity);
                setTodayActivityCouponsMap(todayActivityBean);
                todayActivityBeans.add(todayActivityBean);
            }
        }
    }

    private void setTodayActivityCouponsMap(TodayActivityBean todayActivityBean) {
        Map<Integer,TbCoupons> tbCouponsMap = new HashMap<>();
        List<TbActivityCouponsRecord> tbActivityCouponsRecordList = todayActivityBean.getTbActivityCouponsRecordList();
        for (TbActivityCouponsRecord tbActivityCouponsRecord : tbActivityCouponsRecordList) {
            for (TbCoupons tbCoupons : couponsListCache) {
                if (tbActivityCouponsRecord.getCouponsId() == tbCoupons.getId()){
                    tbCouponsMap.put(tbCoupons.getId(),tbCoupons);
                }
            }
        }
    }

    /**
     * 组装今日活动信息，将今日活动下关联的优惠券放入今日活动下
     * @param todayActivityBean 今日活动信息
     * @param activityInfo 活动描述
     */
    private void setTodayActivityCouponsRecordList(TodayActivityBean todayActivityBean, TbActivity activityInfo) {
        List<TbActivityCouponsRecord> currentActivityCouponsRecords = getActivityCouponsRecordsByActivityId(activityInfo.getId());
        todayActivityBean.setTbActivityCouponsRecordList(currentActivityCouponsRecords);
    }

    /**
     * 获取今日抽奖活动信息
     * @return 返回今日活动信息
     */
    public TodayActivityBean getTodayActivityBean(int storeId) {
        for (TodayActivityBean todayActivityBean : todayActivityBeans) {
            if (todayActivityBean.getTbActivity().getStoreId() == storeId){
                return todayActivityBean;
            }
        }
        return null;
    }

    public TbCoupons getGeneralCoupons() {
        return generalCoupons;
    }

    /**
     * 通过活动id 查询该活动下绑定的所有优惠券
     * @param activityId 活动id
     * @return 返回活动绑定的优惠券
     */
    public List<TbActivityCouponsRecord> getActivityCouponsRecordsByActivityId(long activityId){
        List<TbActivityCouponsRecord> result = new ArrayList<>();
        for (TbActivityCouponsRecord tbActivityCouponsRecord : tbActivityCouponsRecordsCache) {
            if (tbActivityCouponsRecord.getActivityId() == activityId){
                result.add(tbActivityCouponsRecord);
            }
        }
        return result;
    }

    public TbCoupons getTbCouponsById(long couponsId){
        for (TbCoupons tbCoupons : couponsListCache) {
            if (tbCoupons.getId() == couponsId){
                return tbCoupons;
            }
        }
        return null;
    }
}
