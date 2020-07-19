package com.ibay.tea.api.service.token;

import com.alibaba.fastjson.JSON;
import com.ibay.tea.api.config.WechatInfoProperties;
import com.ibay.tea.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WechatTokenService {


    @Resource
    private WechatInfoProperties wechatInfoProperties;

    public String getToken() {
        Map<String,String> param = new HashMap<>();
        param.put("grant_type","client_credential");
        param.put("appid",wechatInfoProperties.getAppId());
        param.put("secret",wechatInfoProperties.getSecret());
        log.info("get token params : {}",param);
        log.info("get token url address : {}",wechatInfoProperties.getTokenUrl());
        String result = HttpUtil.getHttp(wechatInfoProperties.getTokenUrl(), param);
        log.info("from wechat server remote get Token return result : {}",result);
        Map resultMap = JSON.parseObject(result, Map.class);
        log.info("from wechat server remote get Token return result swap map :{}",resultMap);
        Object token = resultMap.get("access_token");
        log.info("from wechat server remote get Token,token value : {} ",token);
        if (null != token){
            return token.toString();
        }
        return null;
    }
}
