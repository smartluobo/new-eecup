package com.ibay.tea.cms.controller.coupons;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.coupons.CmsCouponsService;
import com.ibay.tea.entity.TbCoupons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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


}
