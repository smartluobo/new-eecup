package com.ibay.tea.cms.controller.sms;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.sms.CmsSmsService;
import com.ibay.tea.entity.TbSmsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/cms/sms")
public class CmsSmsController {


    @Resource
    private CmsSmsService cmsSmsService;

    @RequestMapping("list")
    public ResultInfo list(){
        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            List<TbSmsConfig> categories = cmsSmsService.findAll();
            resultInfo.setData(categories);
            return resultInfo;
        }catch (Exception e){
            log.error(" sms config find list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/add")
    public ResultInfo addSmsConfig(@RequestBody TbSmsConfig smsConfig){
        if (smsConfig == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            cmsSmsService.addSmsConfig(smsConfig);
            return resultInfo;
        }catch (Exception e){
            log.error(" sms config addSmsConfig happen exception",e);
            return ResultInfo.newExceptionResultInfo();

        }
    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteSmsConfig(@PathVariable("id") int id){

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            cmsSmsService.deleteSmsConfig(id);
            return resultInfo;
        }catch (Exception e){
            log.error(" sms config deleteSmsConfig happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/update")
    public ResultInfo updateSmsConfig( @RequestBody TbSmsConfig smsConfig){

        if (smsConfig == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            cmsSmsService.updateSmsConfig(smsConfig);
            return resultInfo;
        }catch (Exception e){
            log.error(" sms config updateSmsConfig happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/sendSms/{id}")
    public ResultInfo updateSmsConfig( @PathVariable("id") int id){

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            cmsSmsService.sendSms(id);
            return resultInfo;
        }catch (Exception e){
            log.error(" sms config updateSmsConfig happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

}
