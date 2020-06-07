package com.ibay.tea.cms.shiro.interceptor;

import com.alibaba.fastjson.JSON;
import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.common.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.Assert;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 系统Shiro拦截器
 */
@Slf4j
public class SystemShiroFilter extends FormAuthenticationFilter {

    public SystemShiroFilter() {
        log.info("-------SystemShiroFilter");
    }

    @Override
    public boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        log.info("-------SystemShiroFilter executeLogin");
        setContext(request);
        AuthenticationToken token = null;
        token = createToken(request, response);

        if (token == null) {
            String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
                    "must be created in order to execute a login attempt.";
            throw new IllegalStateException(msg);
        }

        try {
            Subject subject = getSubject(request, response);
            subject.login(token);
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }


    /**
     * 创建校验的token
     *
     * @param request
     * @param response
     * @return
     */
    public AuthenticationToken createToken(ServletRequest request, ServletResponse response) {

        String username = getUsername(request);
        String password = getPassword(request);
        Assert.notNull(username, "用户名不能为空");
        Assert.notNull(password, "密码不能为空");
        return new UsernamePasswordToken(username, password.toCharArray());
    }




    /**
     * 设置返回结果对象
     *
     * @param response
     * @param resultInfo
     */
    public void setResponse(ServletResponse response, ResultInfo resultInfo) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding(CommonConstant.DEFAULT_ENCODING);
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(resultInfo));
        } catch (IOException e) {
            log.error("write response error", e);
        }
    }

    /**
     * 设置context
     *
     * @param request
     */
    public void setContext(ServletRequest request) {
        if (this.getServletContext() == null) {
            setServletContext(((HttpServletRequest) request).getSession(true).getServletContext());
        }
    }
}
