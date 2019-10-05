package com.ibay.tea.cms.controller.sms;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.sms.CmsSmsService;
import com.ibay.tea.entity.TbSmsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/cms/sms")
public class CmsSmsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsSmsController.class);

    @Resource
    private CmsSmsService cmsSmsService;

    @RequestMapping("list")
    public ResultInfo list(){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbSmsConfig> categories = cmsSmsService.findAll();
            resultInfo.setData(categories);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error(" sms config find list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/add")
    public ResultInfo addSmsConfig(@RequestBody TbSmsConfig smsConfig){
        if (smsConfig == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsSmsService.addSmsConfig(smsConfig);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error(" sms config addSmsConfig happen exception",e);
            return ResultInfo.newExceptionResultInfo();

        }
    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteSmsConfig(@PathVariable("id") int id){

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsSmsService.deleteSmsConfig(id);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error(" sms config deleteSmsConfig happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/update")
    public ResultInfo updateSmsConfig( @RequestBody TbSmsConfig smsConfig){

        if (smsConfig == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsSmsService.updateSmsConfig(smsConfig);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error(" sms config updateSmsConfig happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/sendSms/{id}")
    public ResultInfo updateSmsConfig( @PathVariable("id") int id){

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsSmsService.sendSms(id);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error(" sms config updateSmsConfig happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

}
