package com.ibay.tea.api.service.login.impl;

import com.alibaba.fastjson.JSONObject;
import com.ibay.tea.api.config.WechatInfoProperties;
import com.ibay.tea.api.service.login.ApiLoginService;
import com.ibay.tea.common.utils.HttpUtil;
import com.ibay.tea.dao.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ApiLoginServiceImpl implements ApiLoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiLoginServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private WechatInfoProperties wechatPayProperties;

    @Override
    public String login(String code) {

        // 根据appid、secret、js_code、grant_type调用微信官方接口，获取openid
        String loginUrl = wechatPayProperties.getLoginUrl()
                +"appid="+wechatPayProperties.getAppId()
                +"&secret="+wechatPayProperties.getSecret()
                +"&js_code="+code;
        LOGGER.info("loginUrl : {}",loginUrl);

        String result = HttpUtil.get(loginUrl);
        LOGGER.info("result:{}", result);

        // 发送请求并解析
        JSONObject jsonObject = JSONObject.parseObject(result);
        Object oppenId = jsonObject.get("openid");
        if (oppenId != null){
            return oppenId.toString();
        }
        return null;
    }
}
