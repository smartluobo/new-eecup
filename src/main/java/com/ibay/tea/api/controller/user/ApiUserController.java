package com.ibay.tea.api.controller.user;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.user.ApiUserService;
import com.ibay.tea.entity.TbApiUser;
import com.ibay.tea.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("api/user")
public class ApiUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUserController.class);

    @Resource
    private ApiUserService apiUserService;


    @RequestMapping("/reportApiUserInfo")
    public ResultInfo reportApiUserInfo(@RequestBody Map<String,String> params){

        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");
        String nickName = params.get("nickName");
        String userHeadImage = params.get("userHeadImage");
        LOGGER.info("reportApiUserInfo current user oppenId : {}, nickName: {},userHeadImage : {}",oppenId,nickName,userHeadImage);
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            apiUserService.updateApiUserInfo(oppenId,nickName,userHeadImage);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("calculateGoodsOrderPrice GoodsOrderParamVo : {}",params,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/bindPhoneNum")
    public ResultInfo bindPhoneNum(@RequestBody Map<String,String> params){

        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");
        String phoneNum = params.get("phoneNum");
        String verificationCode = params.get("verificationCode");
        LOGGER.info("bindPhoneNum current user oppenId : {}, phoneNum: {},userHeadImage : {}",oppenId,phoneNum,verificationCode);
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            apiUserService.bindPhoneNum(oppenId,phoneNum,verificationCode);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("calculateGoodsOrderPrice GoodsOrderParamVo : {}",params,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/getUserInfo")
    public ResultInfo getUserInfo(@RequestBody Map<String,String> params){

        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            TbApiUser apiUser = apiUserService.getUserInfo(oppenId);
            resultInfo.setData(apiUser);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("getUserInfo params : {}",params,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

}
