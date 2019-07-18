package com.ibay.tea.cache.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.ibay.tea.api.service.token.WechatTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Configuration
public class GuavaCacheConfig {

    @Resource
    private WechatTokenService wechatTokenService;

    @Bean
    public LoadingCache<String,String> wechatTokenGuavaCache(){
        LoadingCache<String, String> wechatTokenGuavaCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(120, TimeUnit.MINUTES)
                .refreshAfterWrite(110,TimeUnit.MINUTES)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return wechatTokenService.getToken();
                    }

                    @Override
                    public ListenableFuture<String> reload(String key, String oldValue) throws Exception {
                        return Futures.immediateFuture(wechatTokenService.getToken());
                    }
                });
        return wechatTokenGuavaCache;
    }


    @Bean
    public Cache<String,String> verificationCodeCache(){
        return CacheBuilder.newBuilder() .maximumSize(100)  .expireAfterAccess(5, TimeUnit.MINUTES).build();
    }
}
