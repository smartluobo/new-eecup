package com.ibay.tea.api.controller.sys;

import com.ibay.tea.api.config.ApiSysProperties;
import com.ibay.tea.api.response.ResultInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/env")
public class SysEnvController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysEnvController.class);

    @Resource
    private ApiSysProperties apiSysProperties;

    @RequestMapping("/getEnvironment")
    public ResultInfo getEnvironment(){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            resultInfo.setData(apiSysProperties.getEnvironment());
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("getEnvironment happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
