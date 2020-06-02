package com.ibay.tea.cms.shiro;

import com.ibay.tea.cms.service.system.SysUserService;
import com.ibay.tea.cms.service.system.UserCacheService;
import com.ibay.tea.cms.shiro.interceptor.SystemShiroFilter;
import com.ibay.tea.common.CommonConstant;
import com.ibay.tea.entity.system.SysMenuRequest;
import com.ibay.tea.entity.system.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.CollectionUtils;
import org.springside.modules.utils.Encodes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;


/**
 * 用户认证类
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private UserCacheService userCacheService;



    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("--------doGetAuthorizationInfo--------");
        ShiroUser shiroUser = (ShiroUser) getAvailablePrincipal(principalCollection);
        SysUser sysUser = sysUserService.getUserByLoginName(shiroUser.getLoginName());
        if (sysUser == null){
            return null;
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<SysMenuRequest> list = userCacheService.getSysMenuList();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(menu -> {
                if (StringUtils.isNotBlank(menu.getPermission())) {
                    // 添加基于Permission的权限信息
                    info.addStringPermission(menu.getPermission());
                }
            });
        }

        sysUserService.updateUserLoginInfo(shiroUser.getId());

        log.info("login host:{}, id:{}, user loginName:{}", SecurityUtils.getSubject().getSession().getHost(),
                shiroUser.getId(), shiroUser.getLoginName());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        String loginName = (String) userToken.getPrincipal();

        //用户名为空
        if (StringUtils.isBlank(loginName)) {
            throw new AccountException("Null username are not allowed by this realm.");
        }
        //验证码逻辑判断
        if (userToken instanceof UsernamePasswordCaptchaToken){
            UsernamePasswordCaptchaToken captchaToken = (UsernamePasswordCaptchaToken) userToken;
            // 增加判断验证码逻辑
            String captcha = captchaToken.getCaptcha();
            Session session = SecurityUtils.getSubject().getSession();
            String exitCode = (String) session.getAttribute(SystemShiroFilter.DEFAULT_CAPTCHA_PARAM);

            if (null == captcha || !captcha.equalsIgnoreCase(exitCode)) {
                throw new CaptchaException("captcha code is error");
            }
        }

        SysUser sysUser = sysUserService.getUserByLoginName(loginName);
        //用户不存在
        if (null == sysUser){
            log.error("loginName[" + loginName
                    + "] login error, there is no user");
            throw new UnknownAccountException("loginName[" + loginName
                    + "] login error, there is no user");
        }
        //用户已被删除
        if (sysUser.getDelFlag() == CommonConstant.DEL) {
            log.info("the current user [{}] has been deleted, can not login.", loginName);
            return null;
        }
        byte[] salt = Encodes.decodeHex(sysUser.getSalt());
        ShiroUser shiroUser = new ShiroUser(sysUser.getId(), sysUser.getLoginName(), sysUser.getName());
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(shiroUser, sysUser.getPassword(),
                ByteSource.Util.bytes(salt), getName());
        return authenticationInfo;
    }

    /**
     * 初始化校验器
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher("SHA1");
        matcher.setHashIterations(1024);
        setCredentialsMatcher(matcher);
    }

}
