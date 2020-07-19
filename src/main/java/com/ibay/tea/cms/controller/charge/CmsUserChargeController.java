package com.ibay.tea.cms.controller.charge;


import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.charge.CmsUserChargeService;
import com.ibay.tea.entity.TbItemCat;
import com.ibay.tea.entity.charge.TbChargeConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/cms/charge")
@Slf4j
public class CmsUserChargeController {

    @Resource
    private CmsUserChargeService cmsUserChargeService;



    @RequestMapping("/listChargeConfig")
    public ResultInfo list(){

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            List<TbChargeConfig> chargeConfigs = cmsUserChargeService.findAllChargeConfig();
            resultInfo.setData(chargeConfigs);
            return resultInfo;
        }catch (Exception e){
            log.error(" category find list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }


    @RequestMapping("/userCharge")
    public ResultInfo userCharge(@RequestBody Map<String,String> params){

        if (CollectionUtils.isEmpty(params)){
            return  ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            cmsUserChargeService.userCharge(params);
            return resultInfo;
        }catch (Exception e){
            log.error(" category find list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }




}
