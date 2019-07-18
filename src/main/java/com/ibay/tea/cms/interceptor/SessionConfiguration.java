package com.ibay.tea.cms.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class SessionConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new CrossOriginInterceptor()).addPathPatterns("/cms/**");
//        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/cms/**").excludePathPatterns("/cms/login/**");
    }
}
