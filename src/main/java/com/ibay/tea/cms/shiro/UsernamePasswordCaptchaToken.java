package com.ibay.tea.cms.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 用户token
 */
public class UsernamePasswordCaptchaToken extends UsernamePasswordToken {

    private static final long serialVersionUID = 1L;
    /** 验证码 **/
    private String captcha;

    public String getCaptcha(){
        return captcha;
    }

    public void setCaptcha(String captcha){
        this.captcha = captcha;
    }

    public UsernamePasswordCaptchaToken(){
        super();

    }

    public UsernamePasswordCaptchaToken(String username, char[] password, boolean rememberMe, String host, String captcha){
        super(username, password, rememberMe, host);
        this.captcha = captcha;
    }
}
