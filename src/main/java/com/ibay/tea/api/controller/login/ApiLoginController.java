package com.ibay.tea.api.controller.login;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.login.ApiLoginService;
import com.ibay.tea.api.service.user.ApiUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class ApiLoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiLoginController.class);

    @Resource
    private ApiLoginService apiLoginService;

    @Resource
    private ApiUserService apiUserService;

    @RequestMapping("/login")
    public ResultInfo login(@RequestBody Map<String,String> codeParam){
        LOGGER.info("api user login call.....");
        String code = null;
        try {
            if (CollectionUtils.isEmpty(codeParam)){
                return ResultInfo.newEmptyResultInfo();
            }
            code = codeParam.get("code");
            LOGGER.info("code:{}",code);
            if (StringUtils.isEmpty(code)){
                return ResultInfo.newEmptyResultInfo();
            }
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            String oppenId = apiLoginService.login(code);
            LOGGER.info("oppenId : {}",oppenId);
            if (StringUtils.isNotEmpty(oppenId)){
                apiUserService.saveApiUser(oppenId);

            }
            resultInfo.setData(oppenId);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("login happen exception code : {}",code,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
