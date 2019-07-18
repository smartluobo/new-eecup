package com.ibay.tea.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ExecutorService sendExecutorService(){
        return new ThreadPoolExecutor(50,
                100,
                60000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(10000),
                new ThreadFactoryBuilder().setNameFormat("test_wechat_send_pool_%d").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}