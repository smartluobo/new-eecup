package com.ibay.tea.cms.shiro.conf;

import com.ibay.tea.cms.shiro.UserRealm;
import com.ibay.tea.cms.shiro.interceptor.ShiroSessionFilter;
import com.ibay.tea.cms.shiro.interceptor.SystemShiroFilter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.Map;

/**
 * Shiro配置类
 */
@Configuration
@Slf4j
public class ShiroConfig {


    /**
     * 设置自定义的SecurityManager
     * @return
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(authRealm());
        return manager;
    }

    /**
     * 设置自定义的Realm
     * @return
     */
    @Bean
    public UserRealm authRealm() {
        UserRealm userRealm = new UserRealm();
        return userRealm;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        shiroFilterFactoryBean.setLoginUrl("/tea/cms/newLogin/system/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/tea/cms/newLogin/system/login/error");

        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        //如果map里面key值为authc,表示所有名为authc的过滤条件使用这个自定义的filter
        filters.put("authc", new SystemShiroFilter());
        filters.put("user", new ShiroSessionFilter());


        // 过滤链定义，从上向下顺序执行，一般将 / ** 放在最为下边:这是一个坑呢，一不小心代码就不好使了;
        // authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问 user:配置记住我或认证通过可以访问
        Map<String, String> filterChainDefinitionMap = shiroFilterFactoryBean.getFilterChainDefinitionMap();
        filterChainDefinitionMap.put("/tea/cms/newLogin/system/login", "authc");
        filterChainDefinitionMap.put("/tea/cms/newLogin/system/logout", "authc");
        filterChainDefinitionMap.put("/system/findPassword", "anon");
        filterChainDefinitionMap.put("/system/resetPassword", "anon");
        filterChainDefinitionMap.put("/system/getCaptcha", "anon");
        filterChainDefinitionMap.put("/system/updatePassword", "anon");
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需x要认证
        filterChainDefinitionMap.put("/tea/cms/**", "user");
        return shiroFilterFactoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        // 设置spring在对shiro进行处理的时候，使用的SecurityManager为我们自定义的SecurityManager
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);

        return advisor;
    }

    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        // 设置代理类
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);

        return creator;
    }

    /**
     * Shiro生命周期处理器
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

}
