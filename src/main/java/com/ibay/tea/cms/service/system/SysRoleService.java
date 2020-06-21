package com.ibay.tea.cms.service.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.ibay.tea.common.SysRoleTypeEnum;
import com.ibay.tea.dao.system.SysRoleMapper;
import com.ibay.tea.dao.system.SysRoleMenuMapper;
import com.ibay.tea.dao.system.SysRoleRoleMapper;
import com.ibay.tea.dao.system.SysUserMapper;
import com.ibay.tea.entity.system.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 系统角色Service层
 */
@Service
@Slf4j
public class SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private SysRoleRoleMapper sysRoleRoleMapper;
    @Resource
    private UserCacheService userCacheService;
    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Resource
    private SysUserMapper sysUserMapper;


    /**
     * 根据id查询角色信息
     *
     * @param id
     * @return
     */
    public SysRoleRequest getSysRoleById(Integer id) {
        SysRoleRequest sysRoleResponse = new SysRoleRequest();
        SysRole sysRole = sysRoleMapper.getSysRoleById(id);
        if (null == sysRole) {
            return sysRoleResponse;
        }

        BeanUtils.copyProperties(sysRole, sysRoleResponse);
        sysRoleResponse.setMenuList(sysRoleMenuMapper.getSysRoleMenuList(id));
        List<SysRole> associatedRoleList = sysRoleMapper.getSysRoleAssociatedRole(id);
        sysRoleResponse.setAssociatedRoleList(associatedRoleList);
        return sysRoleResponse;
    }

    /**
     * 获取新增角色时展示的数据信息
     *
     * @return
     */
    public SysRoleShowInfo getRoleShowInfo() {

        SysRoleShowInfo sysRoleShoInfo = new SysRoleShowInfo();
        //获取菜单树形结构
        sysRoleShoInfo.setSysMenuTree(sysMenuService.querySysMenuTree(null, false));

        //角色类型
        Map<Integer, String> roleType = new HashMap<>(4);
        List<Long> dataTypeList = userCacheService.getDataType();
        dataTypeList.forEach(dataType ->
                roleType.put(dataType.intValue(), SysRoleTypeEnum.getByValue(dataType.intValue())));
        sysRoleShoInfo.setRoleType(roleType);

        //数据权限
        Map<Integer, List<SysRole>> dataAuthor = new HashMap<>(2);
        List<SysRole> testSysRoleList = sysRoleMapper.getAllSysRoleByRoleType(SysRoleTypeEnum.TEST.getValue());
        List<SysRole> operateSysRoleList = sysRoleMapper.getAllSysRoleByRoleType(SysRoleTypeEnum.OPERATE.getValue());
        dataAuthor.put(SysRoleTypeEnum.TEST.getValue(), testSysRoleList);
        dataAuthor.put(SysRoleTypeEnum.OPERATE.getValue(), operateSysRoleList);
        sysRoleShoInfo.setDataAuthor(dataAuthor);
        return sysRoleShoInfo;
    }

    /**
     * 获取所有的角色列表
     *
     * @return
     */
    public Map<String, Object> getAllRoleList() {
        Map<String, Object> resultMap = new HashMap<>(16);
        List<SysRole> testSysRoleList = sysRoleMapper.getAllSysRoleByRoleType(0);
        List<SysRole> operateSysRoleList = sysRoleMapper.getAllSysRoleByRoleType(1);
        resultMap.put("testRoles", testSysRoleList);
        resultMap.put("operateRoles", operateSysRoleList);
        return resultMap;
    }

    /**
     * 删除角色信息
     * @param id
     * @return
     */
    public boolean deleteSysRoleById(Integer id) {
        if (id == 1) {
            log.info("role admin can not delete");
            return false;
        }
        sysRoleMapper.deleteSysRoleById(id, String.valueOf(userCacheService.getSysUser().getId()), new Date());
        sysRoleRoleMapper.deleteSysRoleRole(id);
        sysRoleMenuMapper.deleteSysRoleMenu(id);
        return true;
    }

    /**
     * 保存系统角色信息
     *
     * @param sysRoleRequest
     */

    public void saveSysRole(SysRoleRequest sysRoleRequest) {
        //保存系统角色信息
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleRequest, sysRole);
        sysRole.setCreateDate(new Date());
        sysRole.setCreateBy(String.valueOf(userCacheService.getSysUser().getId()));
        sysRole.setUpdateDate(new Date());
        sysRole.setUpdateBy(String.valueOf(userCacheService.getSysUser().getId()));
        sysRole.setDataScope("0");
        sysRoleMapper.saveSysRole(sysRole);

        //保存角色与角色的关联关系
        Integer roleId = sysRole.getId();
        List<SysRole> sysRoleList = sysRoleRequest.getAssociatedRoleList();
        if (!CollectionUtils.isEmpty(sysRoleList)) {
            List<SysRoleRole> saveList = new ArrayList<>();
            sysRoleList.forEach(role -> {
                SysRoleRole sysRoleRole = new SysRoleRole(roleId, role.getId());
                saveList.add(sysRoleRole);
            });
            sysRoleRoleMapper.batchSaveSysRoleRole(saveList);
        }

        //保存角色与菜单的关联关系
        List<Integer> menuList = sysRoleRequest.getMenuList();
        if (!CollectionUtils.isEmpty(menuList)) {
            List<SysRoleMenu> sysRoleMenuList = new ArrayList<>();
            menuList.forEach(menuId -> {
                SysRoleMenu sysRoleMenu = new SysRoleMenu(roleId, menuId);
                sysRoleMenuList.add(sysRoleMenu);
            });
            sysRoleMenuMapper.batchSaveSysRoleMenu(sysRoleMenuList);
        }

    }

    /**
     * 同步角色数据到老的数据库
     *
     * @param roleId
     */
    public void syncSysRole(String roleId) {
        log.info("sync sys role:{}", roleId);
        if (StringUtils.isBlank(roleId)) {
            return;
        }
        SysRole sysRole = sysRoleMapper.getSysRoleById(Integer.valueOf(roleId));
        if (null == sysRole) {
            return;
        }
        SysRole oldSysRole = sysRoleMapper.getSysRoleOld(sysRole.getName());
        if (null != oldSysRole) {
            log.info("old sys role is exits roleName:{}", sysRole.getName());
            return;
        }
        sysRoleMapper.saveSysRoleOld(sysRole);

        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuMapper.getSysRoleMenu(sysRole.getId());
        if (CollectionUtils.isEmpty(sysRoleMenuList)) {
            return;
        }
        sysRoleMenuMapper.batchSaveSysRoleMenuOld(sysRoleMenuList);
    }

    /**
     * 更新系统角色信息
     *
     * @param sysRoleRequest
     */

    public void updateSysRole(SysRoleRequest sysRoleRequest) {
        //保存系统角色信息
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleRequest, sysRole);
        sysRole.setUpdateDate(new Date());
        sysRole.setUpdateBy(String.valueOf(userCacheService.getSysUser().getId()));

        sysRoleMapper.updateSysRole(sysRole);

        //保存角色与角色的关联关系
        Integer roleId = sysRole.getId();
        List<SysRole> sysRoleList = sysRoleRequest.getAssociatedRoleList();
        if (!CollectionUtils.isEmpty(sysRoleList)) {
            List<SysRoleRole> saveList = new ArrayList<>();
            sysRoleList.forEach(role -> {
                SysRoleRole sysRoleRole = new SysRoleRole(roleId, role.getId());
                saveList.add(sysRoleRole);
            });
            sysRoleRoleMapper.deleteSysRoleRole(roleId);
            sysRoleRoleMapper.batchSaveSysRoleRole(saveList);
        }

        //保存角色与菜单的关联关系
        List<Integer> menuList = sysRoleRequest.getMenuList();
        if (!CollectionUtils.isEmpty(menuList)) {
            List<SysRoleMenu> sysRoleMenuList = new ArrayList<>();
            menuList.forEach(menuId -> {
                SysRoleMenu sysRoleMenu = new SysRoleMenu(roleId, menuId);
                sysRoleMenuList.add(sysRoleMenu);
            });
            sysRoleMenuMapper.deleteSysRoleMenu(roleId);
            sysRoleMenuMapper.batchSaveSysRoleMenu(sysRoleMenuList);
        }
    }

    /**
     * 获取角色信息
     *
     * @param name
     * @return
     */
    public SysRole getSysRoleByUser(String name) {
        return sysRoleMapper.getSysRole(name);
    }

    /**
     * 分页查询角色列表
     *
     * @param pageNum
     * @param pageSize
     * @param roleName
     * @return
     */
    public PageInfo<SysRole> getSysRoleByPage(int pageNum, int pageSize, String roleName) {
        PageHelper.startPage(pageNum, pageSize, "create_date desc");
        List<SysRole> sysRoleList = sysRoleMapper.getSysRoleList(roleName);
        if (!CollectionUtils.isEmpty(sysRoleList)) {
            sysRoleList.forEach(sysRole -> {
                if (StringUtils.isNotBlank(sysRole.getUpdateBy())) {
                    SysUser sysUser = sysUserMapper.getSysUserById(Integer.valueOf(sysRole.getUpdateBy()));
                    if (null != sysUser) {
                        sysRole.setUpdateBy(sysUser.getName());
                    }
                }
            });
        }
        return new PageInfo<>(sysRoleList);
    }
}
