package com.ibay.tea.cms.controller.system;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.system.SysMenuService;
import com.ibay.tea.common.ReturnCodeEnum;
import com.ibay.tea.entity.system.SysMenuRequest;
import com.ibay.tea.entity.system.SysMenuTree;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 菜单Controller
 */
@RestController
@CrossOrigin
@RequestMapping("/cms/system")
@Slf4j
public class MenuController {

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 根据id查询用户菜单信息
     * @param menuId
     * @return
     */
    @RequiresPermissions("sys:menu:view")
    @RequestMapping(value = "/getSysMenuById")
    public Object getUserInfo(String menuId) {
        ResultInfo resultInfo = new ResultInfo();
        if (StringUtils.isBlank(menuId)) {
            return  ResultInfo.newEmptyParamsResultInfo();
        }
        resultInfo.setData(sysMenuService.getSysMenuById(Integer.valueOf(menuId)));
        return resultInfo;
    }

    /**
     * 查询所有的菜单信息
     * @return
     */
    @RequiresPermissions("sys:menu:view")
    @RequestMapping(value = "/getAllSysMenu")
    public Object getAllSysMenu(String menuId) {
        ResultInfo resultInfo = new ResultInfo();
        if (StringUtils.isBlank(menuId)) {
            return ResultInfo.newEmptyResultInfo();
        }
        SysMenuTree menuTree = sysMenuService.querySysMenuTree(menuId, false);
        resultInfo.setData(menuTree);
        return resultInfo;
    }

    /**
     * 保存菜单信息
     * @param sysMenuRequest
     * @return
     */
    @RequiresPermissions("sys:menu:edit")
    @RequestMapping(value = "/saveSysMenu")
    public Object saveSysMenu(@RequestBody SysMenuRequest sysMenuRequest) {
        ResultInfo resultInfo = new ResultInfo();
        if (null == sysMenuRequest) {
            return ResultInfo.newEmptyParamsResultInfo();
        }
        if (null == sysMenuRequest.getParent()){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        sysMenuService.saveSysMenu(sysMenuRequest);
        return resultInfo;
    }

    /**
     * 修改菜单信息
     * @param sysMenuRequest
     * @return
     */
    @RequiresPermissions("sys:menu:edit")
    @RequestMapping(value = "/updateSysMenu")
    public Object updateSysMenu(@RequestBody SysMenuRequest sysMenuRequest) {
        ResultInfo resultInfo = new ResultInfo();
        if (null == sysMenuRequest) {
            return ResultInfo.newEmptyParamsResultInfo();
        }
        if (null == sysMenuRequest.getParent()){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        sysMenuService.updateSysMenu(sysMenuRequest);
        return resultInfo;
    }

    /**
     * 删除菜单信息
     * @param menuId
     * @return
     */
    @RequiresPermissions("sys:menu:edit")
    @RequestMapping(value = "/deleteSysMenu")
    public Object deleteSysMenu(String menuId) {
        ResultInfo resultInfo = new ResultInfo();
        if (StringUtils.isBlank(menuId)) {
            return ResultInfo.newEmptyParamsResultInfo();
        }
        //root菜单不能被删除
        if (Integer.valueOf(menuId) == 1){
            return new ResultInfo(ReturnCodeEnum.ROOT_MENU_CAN_NOT_DELETE);
        }
        sysMenuService.deleteSysMenu(Integer.valueOf(menuId));
        return resultInfo;
    }

    /**
     * 获取树形结构
     * @param menuId
     * @return
     */
    @RequiresUser
    @RequestMapping(value = "/getSysMenuTree")
    public Object getSysMenuTree(String menuId) {
        ResultInfo resultInfo = new ResultInfo();
        if (StringUtils.isBlank(menuId)) {
            return ResultInfo.newEmptyParamsResultInfo();
        }
        SysMenuTree menuTree = sysMenuService.querySysMenuTree(menuId, false);
        resultInfo.setData(menuTree);
        return resultInfo;
    }

    /**
     * 根据用户获取菜单树
     * @return
     */
    @RequiresUser
    @RequestMapping(value = "/getSysMenuTreeByUser")
    public Object getSysMenuTreeByUser() {
        ResultInfo resultInfo = new ResultInfo();
        SysMenuTree menuTree = sysMenuService.querySysMenuTree(null, false);
        resultInfo.setData(menuTree);
        return resultInfo;
    }


    /**
     * 根据菜单获取菜单的按钮权限
     * @param menuId
     * @return
     */
    @RequiresUser
    @RequestMapping(value = "/getSysMenuAuthor")
    public Object getSysMenuAuthor(String menuId) {
        ResultInfo resultInfo = new ResultInfo();
        if (StringUtils.isBlank(menuId)) {
            return ResultInfo.newEmptyParamsResultInfo();
        }
        List<Map<String, String>> authorList = sysMenuService.getSysMenuAuthor(menuId);
        resultInfo.setData(authorList);
        return resultInfo;
    }
}
