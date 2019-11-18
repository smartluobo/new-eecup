package com.ibay.tea.cms.controller.coupons;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.coupons.CmsCouponsService;
import com.ibay.tea.entity.TbApiUser;
import com.ibay.tea.entity.TbCoupons;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("cms/coupons")
public class CmsCouponsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CmsCouponsController.class);

    @Resource
    private CmsCouponsService cmsCouponsService;

    @RequestMapping("/list")
    public ResultInfo list(){
        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbCoupons> couponsList = cmsCouponsService.findAll();
            resultInfo.setData(couponsList);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }
    }


    @RequestMapping("/add")
    public ResultInfo addCoupons(@RequestBody TbCoupons tbCoupons){

        if (tbCoupons == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsCouponsService.addCoupons(tbCoupons);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteCoupons(@PathVariable("id") int id){
        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsCouponsService.deleteCoupons(id);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/update")
    public ResultInfo updateCoupons(@RequestBody TbCoupons tbCoupons){

        if (tbCoupons == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsCouponsService.updateCoupons(tbCoupons);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }
    }


    @RequestMapping("/experienceList")
    public ResultInfo experienceList(@RequestBody Map<String,String> params){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            int pageNum = Integer.valueOf(params.get("pageNum"));
            int pageSize = Integer.valueOf(params.get("pageSize"));
            String couponsCode = params.get("couponsCode");
            Map<String,Object> condition = new HashMap<>();
            int startIndex = (pageNum-1) * pageSize;
            condition.put("startIndex",startIndex);
            condition.put("pageSize",pageSize);
            if (StringUtils.isNotEmpty(couponsCode)){
                condition.put("couponsCode",couponsCode);
            }
            long total = cmsCouponsService.countUserExperienceCoupons(condition);
            List<TbCoupons> couponsList = cmsCouponsService.findUserExperienceCoupons(condition);
            resultInfo.setTotal(total);
            resultInfo.setData(couponsList);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }


    @RequestMapping("/updateExperience/{userCouponsId}/{useStatus}")
    public ResultInfo updateExperience(@PathVariable("userCouponsId") int userCouponsId, @PathVariable("useStatus") int useStatus){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            if (useStatus == 0 || useStatus == 2){
                cmsCouponsService.updateExperience(userCouponsId,useStatus);
            }else {
                return ResultInfo.newParameterErrorResultInfo();
            }
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }
    @RequestMapping("/generateShoppingCard/{count}/{amount}/{type}")
    public ResultInfo generateShoppingCard(@PathVariable("count") int count, @PathVariable("amount") int amount,@PathVariable("type") int type){
        LOGGER.info("generateShoppingCard count : {},amount : {}, type : {}",count,amount,type);
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            if (count == 0 || amount == 0){
                return ResultInfo.newParameterErrorResultInfo();

            }else {
                boolean flag = cmsCouponsService.generateShoppingCard(count,amount,type);
            }
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }


}
