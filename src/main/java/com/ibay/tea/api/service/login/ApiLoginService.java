package com.ibay.tea.api.service.login;

import com.alibaba.fastjson.JSONObject;
import com.ibay.tea.api.config.WechatInfoProperties;
import com.ibay.tea.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class ApiLoginService {


    @Resource
    private WechatInfoProperties wechatPayProperties;

    public String login(String code) {

        // 根据appid、secret、js_code、grant_type调用微信官方接口，获取openid
        String loginUrl = wechatPayProperties.getLoginUrl()
                +"appid="+wechatPayProperties.getAppId()
                +"&secret="+wechatPayProperties.getSecret()
                +"&js_code="+code;
        log.info("loginUrl : {}",loginUrl);

        String result = HttpUtil.get(loginUrl);
        log.info("result:{}", result);

        // 发送请求并解析
        JSONObject jsonObject = JSONObject.parseObject(result);
        Object oppenId = jsonObject.get("openid");
        if (oppenId != null){
            return oppenId.toString();
        }
        return null;
    }
}
