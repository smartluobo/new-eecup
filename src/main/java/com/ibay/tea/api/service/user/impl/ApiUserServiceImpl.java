package com.ibay.tea.api.service.user.impl;

import com.google.common.cache.LoadingCache;
import com.ibay.tea.api.config.WechatInfoProperties;
import com.ibay.tea.api.service.user.ApiUserService;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.dao.TbApiUserMapper;
import com.ibay.tea.dao.UserMapper;
import com.ibay.tea.entity.TbApiUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class ApiUserServiceImpl implements ApiUserService{

    @Resource
    private TbApiUserMapper tbApiUserMapper;

    @Resource
    LoadingCache<String,String> wechatTokenGuavaCache;

    @Resource
    private WechatInfoProperties wechatInfoProperties;

    @Override
    public TbApiUser findApiUserByOppenId(String oppenId) {
        return tbApiUserMapper.findApiUserByOppenId(oppenId);
    }

    @Override
    public void saveApiUser(String oppenId) {
        TbApiUser apiUserByOppenId = tbApiUserMapper.findApiUserByOppenId(oppenId);
        if (apiUserByOppenId != null){
            return;
        }
        //根据oppenId调用获取用户信息接口获取用户信息
       // Map<String,String> apiUserInfoMap = getApiUserInfoByOppenId(oppenId);
        TbApiUser tbApiUser = new TbApiUser();
        tbApiUser.setOppenId(oppenId);
        tbApiUser.setCreateTime(new Date());
        tbApiUser.setUpdateTime(new Date());
        tbApiUserMapper.insert(tbApiUser);
    }

    private Map<String,String> getApiUserInfoByOppenId(String oppenId) {
        try {
            String token = wechatTokenGuavaCache.get(ApiConstant.WECHAT_ACCESS_TOKEN_GUAVA_KEY);

        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
