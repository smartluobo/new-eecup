package com.ibay.tea.cms.shiro.interceptor;

import com.alibaba.fastjson.JSON;
import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.shiro.UsernamePasswordCaptchaToken;
import com.ibay.tea.common.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.data.redis.cache.RedisCacheManager;
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

    /**
     * 超过最大登录次数 则需要验证码
     **/
    private static final int MAX_LOGIN_COUNT_WITHOUT_CAPTCHA = 3;
    /**
     * 默认的ip地址
     */
    private static final String DEFAULT_IP = "127.0.0.1";
    /**
     * Http状态头
     */
    private static final String HTTP_HEAD_X_FORWARDED_FOR = "x-forwarded-for";
    private static final String HTTP_HEAD_PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String HTTP_HEAD_WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    /**
     * 登录次数后缀
     **/
    private static final String CAPTCHA_LOGIN_COUNT = "_login_count";

    /**
     * 验证码参数
     **/
    public static final String DEFAULT_CAPTCHA_PARAM = "captcha";

    private static final String SHIRO_ERROR_COUNT = "shiro_error_count";

    private RedisCacheManager redisCacheManager;

    public SystemShiroFilter() {
        log.info("-------SystemShiroFilter");
    }

    public SystemShiroFilter(RedisCacheManager redisCacheManager) {
        log.info("-------SystemShiroFilter");
        this.redisCacheManager = redisCacheManager;
    }
    /**
     * 校验ip是否正确
     * 1.ip不为空
     * 2.不是未知的ip
     * 3.通过点号分割后长度为4
     *
     * @param ip
     * @return
     */
    private static boolean isIpValid(String ip) {
        String unknownIp = "unkown";
        String ipSeparate = "\\.";
        if (StringUtils.isEmpty(ip) || unknownIp.equalsIgnoreCase(ip) || ip.split(ipSeparate).length != 4) {
            return false;
        }
        return true;
    }
    /**
     * 根据请求获取ip
     *
     * @param request
     * @return
     */
    public static String getIpByRequest(HttpServletRequest request) {
        String xForwardedFor = request.getHeader(HTTP_HEAD_X_FORWARDED_FOR);
        String ip = DEFAULT_IP;
        if (StringUtils.isNotEmpty(xForwardedFor)) {
            ip = xForwardedFor.split(CommonConstant.DEFAULT_SEPARATE)[0].trim();
            if (isIpValid(ip)) {
                return ip;
            }
        }
        ip = request.getHeader(HTTP_HEAD_PROXY_CLIENT_IP);
        if (isIpValid(ip)) {
            return ip;
        }
        ip = request.getHeader(HTTP_HEAD_WL_PROXY_CLIENT_IP);
        if (isIpValid(ip)) {
            return ip;
        }
        ip = request.getRemoteAddr();
        if (isIpValid(ip)) {
            return ip;
        }
        return ip;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        String captcha = getCaptcha(request);

        boolean rememberMe = isRememberMe(request);
        String host = getIpByRequest((HttpServletRequest)request);
        Assert.notNull(username, "用户名不能为空");
        Assert.notNull(password, "密码不能为空");

        return new UsernamePasswordCaptchaToken(username, password.toCharArray(), rememberMe, host, captcha);
    }


    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        log.info("-------SystemShiroFilter");
        setContext(request);
        AuthenticationToken token = null;
        String userName = getUsername(request);
        String contextKey = String.format("%s%s", userName, CAPTCHA_LOGIN_COUNT);
        long errorCount = 0L;

        Object count = redisCacheManager.getCache(SHIRO_ERROR_COUNT).get(contextKey);
        if (count != null) {
            errorCount = (Long) count;
        }

        boolean needCaptcha = errorCount > MAX_LOGIN_COUNT_WITHOUT_CAPTCHA;
        token = createToken(request, response, needCaptcha);

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
     * @param needCaptcha
     * @return
     */
    private AuthenticationToken createToken(ServletRequest request, ServletResponse response, boolean needCaptcha) {
        if (needCaptcha) {
            return createToken(request, response);
        } else {
            String username = getUsername(request);
            String password = getPassword(request);
            Assert.notNull(username, "用户名不能为空");
            Assert.notNull(password, "密码不能为空");

            return new UsernamePasswordToken(username, password.toCharArray());
        }
    }




    /**
     * 设置返回结果对象
     *
     * @param response
     * @param resultInfo
     */
    private void setResponse(ServletResponse response, ResultInfo resultInfo) {
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
    private void setContext(ServletRequest request) {
        if (this.getServletContext() == null) {
            setServletContext(((HttpServletRequest) request).getSession(true).getServletContext());
        }
    }

    /**
     * 获取验证码
     *
     * @param request
     * @return
     */
    private String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, DEFAULT_CAPTCHA_PARAM);
    }
}
