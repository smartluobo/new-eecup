package com.ibay.tea.cms.controller.system;


import com.github.pagehelper.PageInfo;
import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.system.SysRoleService;
import com.ibay.tea.cms.service.system.SysUserService;
import com.ibay.tea.common.ReturnCodeEnum;
import com.ibay.tea.common.utils.PageUtil;
import com.ibay.tea.entity.system.SysRole;
import com.ibay.tea.entity.system.SysRoleRequest;
import com.ibay.tea.entity.system.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色Controller
 */
@RestController
@RequestMapping("/cms/system")
@Slf4j
public class RoleController {

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysUserService sysUserService;

    /**
     * 角色新增是展示的信息
     * @return
     */
    @RequiresPermissions("user:info:view")
    @RequestMapping(value = "/getRoleShowInfo")
    public Object getRoleShowInfo() {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setData(sysRoleService.getRoleShowInfo());
        return resultInfo;
    }

    /**
     * 根据id查询用户角色信息
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/getSysRoleById")
    public Object getSysRoleById(String roleId) {
        ResultInfo resultInfo = new ResultInfo();
        if (StringUtils.isBlank(roleId)) {
            return  ResultInfo.newEmptyParamsResultInfo();
        }
        resultInfo.setData(sysRoleService.getSysRoleById(Integer.valueOf(roleId)));
        return resultInfo;
    }

    /**
     * 获取所有的角色信息
     * @return
     */
    @RequiresPermissions("user:info:view")
    @RequestMapping(value = "/getAllRoleList")
    public Object getAllRoleList() {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setData(sysRoleService.getAllRoleList());
        return resultInfo;
    }


    /**
     * 根据id删除用户角色信息
     * @param roleId
     * @return
     */
    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "/deleteSysRole")
    public Object deleteSysRole(String roleId) {
        ResultInfo resultInfo = new ResultInfo();
        if (StringUtils.isBlank(roleId)) {
            return  ResultInfo.newEmptyParamsResultInfo();
        }
        List<SysUser> userList = sysUserService.getSysUserByRoleId(Integer.valueOf(roleId));
        if (!CollectionUtils.isEmpty(userList)){
            return new ResultInfo(ReturnCodeEnum.ROLE_HAS_RELATION_WITH_USER);
        }
        resultInfo.setData(sysRoleService.deleteSysRoleById(Integer.valueOf(roleId)));
        return resultInfo;
    }


    /**
     * 分页查询用户角色信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequiresPermissions("sys:role:view")
    @RequestMapping("/getRoleInfoByPage")
    public Object getRoleInfoByPage(Integer pageNum, Integer pageSize, String roleName) {
        log.info("pageNum:{},pageSize:{},loginName:{},roleName:{}", pageNum, pageSize, roleName);
        ResultInfo resultInfo = new ResultInfo();
        //1、检查请求参数是否为空
        if(pageNum == null || pageSize == null){
            return new ResultInfo(ReturnCodeEnum.PARAMETER_EMPTY);
        } else {
            //2、进行查询
            PageInfo<SysRole> sysUserByPage = sysRoleService.getSysRoleByPage(pageNum, pageSize, roleName);
            resultInfo.setData(PageUtil.getData(sysUserByPage));
        }
        return resultInfo;
    }

    /**
     * 校验角色名称是否重复
     * @param roleName
     * @return
     */
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "/checkRoleName")
    public Object checkRoleName(String roleName) {
        log.info("check login name, roleName:{}", roleName);
        ResultInfo resultInfo = new ResultInfo();
        boolean flag = true;
        if (null != sysRoleService.getSysRoleByUser(roleName)) {
            flag = false;
        }
        resultInfo.setData(flag);
        return resultInfo;
    }

    /**
     * 保存角色信息接口
     * @param sysRoleRequest
     * @return
     */
    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "/saveSysRole")
    public Object saveSysRole(@RequestBody SysRoleRequest sysRoleRequest) {
        ResultInfo resultInfo = new ResultInfo();
        if (null == sysRoleRequest){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        sysRoleService.saveSysRole(sysRoleRequest);
        return resultInfo;
    }

    /**
     * 同步角色信息
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/syncSysRoleById")
    public Object syncSysRoleById(String roleId) {
        ResultInfo resultInfo = new ResultInfo();
        if (StringUtils.isBlank(roleId)) {
            return  ResultInfo.newEmptyParamsResultInfo();
        }
        sysRoleService.syncSysRole(roleId);
        return resultInfo;
    }

    /**
     * 更新角色信息
     * @param sysRoleRequest
     * @return
     */
    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "/updateSysRole")
    public Object updateSysRole(@RequestBody SysRoleRequest sysRoleRequest) {
        ResultInfo resultInfo = new ResultInfo();
        if (null == sysRoleRequest){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        //角色不存在
        SysRoleRequest oldSysRoleRequest = sysRoleService.getSysRoleById(sysRoleRequest.getId());
        if (null == oldSysRoleRequest){
            return new ResultInfo(ReturnCodeEnum.ROLE_NOT_EXITS);
        }
        //校验角色名是否存在
        String roleName = sysRoleRequest.getName();
        if (!StringUtils.isBlank(roleName) && !roleName.equals(oldSysRoleRequest.getName())){
            if (null != sysRoleService.getSysRoleByUser(roleName)){
                return new ResultInfo(ReturnCodeEnum.ROLE_NAME_IS_EXITS);
            }
        }
        sysRoleService.updateSysRole(sysRoleRequest);
        return resultInfo;
    }
}
