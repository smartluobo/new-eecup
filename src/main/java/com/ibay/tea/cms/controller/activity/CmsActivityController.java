package com.ibay.tea.cms.controller.activity;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.activity.CmsActivityService;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.SerialGenerator;
import com.ibay.tea.dao.TbActivityCouponsRecordMapper;
import com.ibay.tea.dao.TbActivityMapper;
import com.ibay.tea.dao.TbCouponsMapper;
import com.ibay.tea.dao.TbExperienceCouponsPoolMapper;
import com.ibay.tea.entity.TbActivity;
import com.ibay.tea.entity.TbActivityCouponsRecord;
import com.ibay.tea.entity.TbCoupons;
import com.ibay.tea.entity.TbExperienceCouponsPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/cms/activity")
public class CmsActivityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsActivityController.class);

    @Resource
    private CmsActivityService cmsActivityService;

    @Resource
    private TbActivityCouponsRecordMapper tbActivityCouponsRecordMapper;

    @Resource
    private TbCouponsMapper tbCouponsMapper;

    @Resource
    private TbActivityMapper tbActivityMapper;

    @Resource
    private TbExperienceCouponsPoolMapper tbExperienceCouponsPoolMapper;

    @RequestMapping("/list")
    public ResultInfo list(){

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	List<TbActivity> activityList =cmsActivityService.findAll();
            resultInfo.setData(activityList);
        	return resultInfo;
        }catch (Exception e){
            LOGGER.error("activity list happen exception",e);
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/add")
    public ResultInfo addActivity(@RequestBody TbActivity tbActivity){

        if (tbActivity == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsActivityService.addActivity(tbActivity);
        	return resultInfo;
        }catch (Exception e){
            LOGGER.error("addActivity list happen exception",e);
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteActivity(@PathVariable("id") int id){

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsActivityService.deleteActivity(id);
        	return resultInfo;
        }catch (Exception e){
            LOGGER.error("addActivity list happen exception",e);
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/update")
    public ResultInfo updateActivity(@RequestBody TbActivity tbActivity){

        if (tbActivity == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsActivityService.updateActivity(tbActivity);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/couponsList/{activityId}")
    public ResultInfo updateActivity(@PathVariable("activityId") int activityId){

        if (activityId == 0){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbActivityCouponsRecord> recordList = tbActivityCouponsRecordMapper.findCouponsByActivityId(activityId);
            resultInfo.setData(recordList);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/addCoupons")
    public ResultInfo activityAddCoupons(@RequestBody TbActivityCouponsRecord tbActivityCouponsRecord){

        if (tbActivityCouponsRecord == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            int activityId = tbActivityCouponsRecord.getActivityId();
            int couponsId = tbActivityCouponsRecord.getCouponsId();

            TbActivity tbActivity = tbActivityMapper.selectByPrimaryKey(activityId);
            if (tbActivity.getActivityType() == ApiConstant.ACTIVITY_TYPE_STORE){
                //门店活动添加优惠券需要每张券分配券码
                TbCoupons tbCoupons = tbCouponsMapper.selectByPrimaryKey(couponsId);
                if (tbCoupons == null){
                    return resultInfo;
                }
                int couponsCount = tbActivityCouponsRecord.getCouponsCount();
                if (couponsCount <=0){
                    return resultInfo;
                }
                List<TbExperienceCouponsPool> pools = new ArrayList<>();
                for (int i = 0; i < couponsCount; i++) {
                    TbExperienceCouponsPool experienceCouponsPool = new TbExperienceCouponsPool();
                    experienceCouponsPool.setActivityId(activityId);
                    experienceCouponsPool.setCouponsId(couponsId);
                    experienceCouponsPool.setBackgroundUrl(tbCoupons.getCouponsPoster());
                    experienceCouponsPool.setCreateTime(new Date());
                    experienceCouponsPool.setCouponsCode(SerialGenerator.getUniqueCode());
                    pools.add(experienceCouponsPool);
                }
                tbExperienceCouponsPoolMapper.insertBatch(pools);
                return resultInfo;
            }else {
                TbCoupons tbCoupons = tbCouponsMapper.selectByPrimaryKey(couponsId);
                tbActivityCouponsRecord.setActivityName(tbActivity.getActivityName());
                tbActivityCouponsRecord.setCouponsName(tbCoupons.getCouponsName());
                tbActivityCouponsRecord.setCouponsPoster(tbCoupons.getCouponsPoster());
                LOGGER.info("activity add coupons tbActivityCouponsRecord : {}",tbActivityCouponsRecord);
                if(tbActivityCouponsRecord.getId() != 0){
                    tbActivityCouponsRecordMapper.updateRecord(tbActivityCouponsRecord);
                }else {
                    tbActivityCouponsRecordMapper.insert(tbActivityCouponsRecord);
                }
                return resultInfo;
            }
        }catch (Exception e){
            LOGGER.error("");
            return ResultInfo.newExceptionResultInfo();
        }
    }


    @RequestMapping("/deleteCoupons/{activityId}/{id}")
    public ResultInfo activityDeleteCoupons(@PathVariable("activityId") int activityId,@PathVariable("id") int id){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            tbActivityCouponsRecordMapper.activityDeleteCoupons(activityId,id);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("");
            return ResultInfo.newExceptionResultInfo();
        }
    }





}
