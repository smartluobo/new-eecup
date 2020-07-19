package com.ibay.tea.cms.shiro;


import com.ibay.tea.cms.service.system.SysMenuService;
import com.ibay.tea.cms.service.system.SysUserService;
import com.ibay.tea.entity.system.SysMenuRequest;
import com.ibay.tea.entity.system.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springside.modules.utils.Encodes;

import javax.annotation.Resource;
import java.util.List;

/**
 * Realm：域，Realm 充当了 Shiro 与应用安全数据间的“桥梁”或者“连接器”。也就是说，当对用户执行认证（登录）和授权（访问控制）验证时，
 * Shiro 会从应用配置的 Realm 中查找用户及其权限信息。从这个意义上讲，Realm 实质上是一个安全相关的 DAO：它封装了数据源的连接细节，
 * 并在需要时将相关数据提供给 Shiro 。当配置 Shiro时，你必须至少指定一个 Realm ，用于认证和（或）授权。
 */

@Slf4j
public class CustomRealm  extends AuthorizingRealm {


    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysMenuService sysMenuService;

    /**
     * 获取指定身份的权限，并返回相关信息
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        ShiroUser shiroUser = (ShiroUser) getAvailablePrincipal(principalCollection);
        SysUser sysUser = sysUserService.getUserByName(shiroUser.getLoginName());
        if (sysUser == null){
            return null;
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if(sysUser.getIsAdmin() == 1){
            List<String> permissionList = sysMenuService.getAllSysMenuPermission();
            if (!CollectionUtils.isEmpty(permissionList)){
                for (String permission : permissionList) {
                    info.addStringPermission(permission);
                }
            }
            info.addStringPermission("sys:user:edit");
            info.addStringPermission("sys:role:edit");
            info.addStringPermission("sys:menu:edit");
        }else{
            List<SysMenuRequest> list = sysMenuService.getSysMenuList();
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(menu -> {
                    if (!StringUtils.isEmpty(menu.getPermission())) {
                        // 添加基于Permission的权限信息
                        info.addStringPermission(menu.getPermission());
                    }
                });
            }
        }
        log.info("login host:{}, id:{}, user loginName:{}", SecurityUtils.getSubject().getSession().getHost(),
                shiroUser.getId(), shiroUser.getLoginName());
        return info;
    }

    /**
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     *
     * 身份验证（getAuthenticationInfo 方法）验证账户和密码，并返回相关信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        String username = (String) authenticationToken.getPrincipal();
        SysUser user = sysUserService.getUserByName(username);
        if (user == null) {
            return null;
        } else {

            byte[] salt = Encodes.decodeHex(user.getSalt());

            ShiroUser shiroUser = new ShiroUser(user.getId(), user.getLoginName(), user.getName());

            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(shiroUser, user.getPassword(),
                    ByteSource.Util.bytes(salt), getName());
            return authenticationInfo;
        }
    }
}
