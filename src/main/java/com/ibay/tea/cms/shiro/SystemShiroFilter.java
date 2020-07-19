package com.ibay.tea.cms.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.util.Assert;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Slf4j
public class SystemShiroFilter extends FormAuthenticationFilter {

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        AuthenticationToken token = null;
        try {
            token = createToken(request);
            Subject subject = getSubject(request, response);
            subject.login(token);
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }



    /**
     * @param request
     * @return
     */
    private AuthenticationToken createToken(ServletRequest request) {
        String username = getUsername(request);
        String password = getPassword(request);
        Assert.notNull(username, "用户名不能为空");
        Assert.notNull(password, "密码不能为空");

        return new UsernamePasswordToken(username, password.toCharArray());
    }



}
