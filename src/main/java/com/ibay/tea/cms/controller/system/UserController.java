package com.ibay.tea.cms.controller.system;

import com.github.pagehelper.PageInfo;
import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.system.SysUserService;
import com.ibay.tea.common.ReturnCodeEnum;
import com.ibay.tea.common.utils.PageUtil;
import com.ibay.tea.entity.system.SysUser;
import com.ibay.tea.entity.system.SysUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户Controller
 */
@Slf4j
@RestController
@RequestMapping("/cms/system/user")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 获取当前的用户信息
     *
     * @return
     */
    @RequestMapping(value = "/getCurrentUserInfo")
    public Object getCurrentUserInfo() {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setData(sysUserService.getCurrentUserInfo());
        return resultInfo;
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param userId
     * @return
     */
    @RequiresPermissions("user:info:view")
    @RequestMapping(value = "/getSysUserById")
    public Object getUserInfo(String userId) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setData(sysUserService.getUserById(Integer.valueOf(userId)));
        return resultInfo;
    }

    /**
     * 根据id删除用户信息
     *
     * @param userId
     * @return
     */
    @RequiresPermissions("user:info:edit")
    @RequestMapping(value = "/deleteSysUser")
    public Object deleteSysUser(String userId) {
        ResultInfo resultInfo = new ResultInfo();
        if (StringUtils.isBlank(userId)) {
            return ResultInfo.newEmptyParamsResultInfo();
        }
        resultInfo.setData(sysUserService.deleteSysUserById(Integer.valueOf(userId)));
        return resultInfo;
    }


    /**
     * 分页查询用户信息
     *
     * @param pageNum
     * @param pageSize
     * @param loginName
     * @param name
     * @return
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping("/getUserInfoByPage")
    public Object getUserInfoByPage(Integer pageNum, Integer pageSize, String loginName, String name) {
        log.info("pageNum:{},pageSize:{},loginName:{},name:{}", pageNum, pageSize, loginName, name);
        ResultInfo resultInfo = new ResultInfo();
        //1、检查请求参数是否为空
        if (pageNum == null || pageSize == null) {
            return new ResultInfo(ReturnCodeEnum.PARAMETER_EMPTY);
        } else {
            //2、进行查询
            PageInfo<SysUser> sysUserByPage = sysUserService.getSysUserByPage(pageNum, pageSize, loginName, name);
            resultInfo.setData(PageUtil.getData(sysUserByPage));
        }
        return resultInfo;
    }

    /**
     * 校验登录名是否正确
     *
     * @param loginName
     * @return
     */
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "/checkLoginName")
    public Object checkLoginName(String loginName) {
        log.info("check login name loginName:{}", loginName);
        ResultInfo resultInfo = new ResultInfo();
        boolean loginNameFlag = false;
        if (StringUtils.isBlank(loginName)) {
            loginNameFlag = false;
        } else if (StringUtils.isNoneBlank(loginName) && sysUserService.getUserByLoginName(loginName) == null) {
            loginNameFlag = true;
        }
        resultInfo.setData(loginNameFlag);
        return resultInfo;
    }

    /**
     * 保存用户信息
     *
     * @param sysUser
     * @return
     */
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "/saveSysUser", method = RequestMethod.POST)
    public Object saveSysUser(@RequestBody SysUserRequest sysUser) {
        ResultInfo resultInfo = new ResultInfo();
        if (null == sysUser) {
            return ResultInfo.newEmptyResultInfo();
        }
        //校验用户是否存储
        String loginName = sysUser.getLoginName();
        if (StringUtils.isNoneBlank(loginName)
                && sysUserService.getUserByLoginName(loginName) != null) {
            return new ResultInfo(ReturnCodeEnum.USER_NAME_IS_EXITS);
        }
        sysUserService.saveSysUserById(sysUser);
        return resultInfo;
    }

    /**
     * 同步角色信息
     * @param userId
     * @return
     */
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "/syncSysUser")
    public Object syncSysUser(String userId) {
        ResultInfo resultInfo = new ResultInfo();
        if (StringUtils.isBlank(userId)) {
            return ResultInfo.newEmptyParamsResultInfo();
        }
        sysUserService.syncSysUserById(userId);
        return resultInfo;
    }
    /**
     * 更新系统用户信息
     *
     * @param sysUser
     * @return
     */
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "/updateSysUser", method = RequestMethod.POST)
    public Object updateSysUser(@RequestBody SysUserRequest sysUser) {
        ResultInfo resultInfo = new ResultInfo();
        if (null == sysUser) {
            return ResultInfo.newEmptyParamsResultInfo();
        }

        SysUser oldSysUser = sysUserService.getUserById(sysUser.getId());
        if (null == oldSysUser) {
            return new ResultInfo(ReturnCodeEnum.USER_NOT_EXITS);
        }
        //校验用户是否存储
        String loginName = sysUser.getLoginName();

        //新登录名称与老的登录名称不一样是，需要校验是否重名
        if (StringUtils.isNoneBlank(loginName)
                && !loginName.equals(oldSysUser.getLoginName())) {
            if (sysUserService.getUserByLoginName(loginName) != null) {
                return new ResultInfo(ReturnCodeEnum.USER_NAME_IS_EXITS);
            }
        }

        sysUserService.updateSysUser(sysUser);
        return resultInfo;
    }

    /**
     * 更新用户信息
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/updateSysUserInfo", method = RequestMethod.POST)
    public Object updateUserInfo(@RequestBody SysUserRequest sysUser){
        ResultInfo resultInfo = new ResultInfo();
        SysUser oldSysUser = sysUserService.getUserById(sysUser.getId());
        if (null == oldSysUser) {
            return new ResultInfo(ReturnCodeEnum.USER_NOT_EXITS);
        }
        sysUserService.updateSysUserInfo(sysUser);
        return resultInfo;
    }
}
