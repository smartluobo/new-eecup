package com.ibay.tea.cms.service.system;

import com.ibay.tea.cms.shiro.ShiroUser;
import com.ibay.tea.dao.system.SysMenuMapper;
import com.ibay.tea.dao.system.SysRoleMapper;
import com.ibay.tea.dao.system.SysUserMapper;
import com.ibay.tea.entity.system.SysMenuRequest;
import com.ibay.tea.entity.system.SysRole;
import com.ibay.tea.entity.system.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 用户、权限、菜单缓存Service层
 */
@Slf4j
@Service
public class UserCacheService {

    /**
     * 默认的用户状态
     **/
    private static final String DEFAULT_USER_STATUS = "0";

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired

    private SysMenuService sysMenuService;

    /**
     * 获取系统用户
     *
     * @return
     */
    public SysUser getSysUser() {
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (null == shiroUser) {
            SecurityUtils.getSubject().logout();
        }
        SysUser sysUser = sysUserMapper.getSysUserById(shiroUser.getId());
        log.info("local cache sys user is not empty, user id:{}", sysUser.getId());
        return sysUser;
    }

    /**
     * 获取角色信息
     *
     * @return
     */
    public List<SysRole> getSysRoles() {
        List<SysRole> roleList = new ArrayList<>();
        roleList.addAll(sysRoleMapper.getSysRoleByUserId(getSysUser().getId()));
        return roleList;
    }

    /**
     * 获取菜单信息
     *
     * @return
     */
    public List<SysMenuRequest> getSysMenuList() {
        List<SysMenuRequest> sysMenuList = null;
        SysUser sysUser = getSysUser();
        if (sysUser.isAdmin() || sysUser.getIsAdmin() == 1) {
            sysMenuList = sysMenuService.buildSysMenuRequest(sysMenuMapper.getAllSysMenu());
        } else {
            sysMenuList = sysMenuService.buildSysMenuRequest(sysMenuMapper.getSysMenuByUserId(sysUser.getId()));

        }
        return sysMenuList;
    }

    /**
     * 获取用户状态 若为admin返回0 否则返回
     *
     * @return
     */
    public String getUserStatus() {
        if (getSysUser().isAdmin()) {
            return DEFAULT_USER_STATUS;
        } else {
            List<SysRole> sysRoleList = getSysRoles();
            if (!CollectionUtils.isEmpty(sysRoleList)) {
                String status = String.valueOf(sysRoleList.get(0).getRoleType());
                return status;
            }
        }
        return DEFAULT_USER_STATUS;
    }

    /**
     * 获取数据权限信息
     *
     * @return
     */
    public List<Long> getDataType() {
        List<Long> list = new ArrayList<>();
        if (getSysUser().isAdmin()) {
            list.add(1L);
            list.add(0L);
        } else {
            List<SysRole> sysRoleList = getSysRoles();
            if (!CollectionUtils.isEmpty(sysRoleList)) {
                Long dataType = Long.valueOf(sysRoleList.get(0).getRoleType());
                list.add(dataType);
            }
        }
        return list;
    }

}
