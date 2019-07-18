package com.ibay.tea.api.controller.sms;


import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.common.service.SendSmsService;
import com.ibay.tea.common.utils.SerialGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/sms")
public class ApiSmsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSmsController.class);

    @Resource
    private SendSmsService sendSmsService;

    @RequestMapping("/sendVerificationCode")
    public ResultInfo sendVerificationCode(@RequestBody Map<String,String> params){

        if (params == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }
        String phoneNum = params.get("phoneNum");
        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            String verificationCode = SerialGenerator.getVerificationCode();
            boolean flag = sendSmsService.sendVerificationCode(phoneNum, verificationCode);

            if (flag){
                sendSmsService.cacheVerificationCode(phoneNum,verificationCode);
                return resultInfo;
            }else {
                return ResultInfo.newExceptionResultInfo();
            }
        }catch (Exception e){
            LOGGER.error("sendVerificationCode happen exception phoneNum : {}",phoneNum,e);
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/checkVerificationCode")
    public ResultInfo checkVerificationCode(@RequestBody Map<String,String> params){

        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String phoneNum = params.get("phoneNum");
        String verificationCode = params.get("verificationCode");
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            boolean flag  = sendSmsService.checkVerificationCode(phoneNum,verificationCode);
            if (flag){
                return resultInfo;
            }else {
                return ResultInfo.newFailResultInfo();
            }
        }catch (Exception e){
            LOGGER.error("sendVerificationCode happen exception phoneNum : {}",phoneNum,e);
            return ResultInfo.newExceptionResultInfo();
        }

    }


}
