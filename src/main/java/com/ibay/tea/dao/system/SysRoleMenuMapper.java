package com.ibay.tea.dao.system;


import com.ibay.tea.entity.system.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统角色与菜单的关联关系dao层
 */
@Component
@Mapper
public interface SysRoleMenuMapper {

    /**
     * 删除角色与菜单的关联关系
     * @param roleId
     */
    void deleteSysRoleMenu(@Param("roleId") Integer roleId);

    /**
     * 批量保存角色与菜单的关联关系
     * @param sysRoleMenuList
     */
    void batchSaveSysRoleMenu(List<SysRoleMenu> sysRoleMenuList);

    /**
     * 批量保存角色与菜单的关联关系
     * @param sysRoleMenuList
     */
    void batchSaveSysRoleMenuOld(List<SysRoleMenu> sysRoleMenuList);

    /**
     * 查询列表
     * @param roleId
     * @return
     */
    List<Integer> getSysRoleMenuList(@Param("roleId") Integer roleId);
    /**
     * 查询列表
     * @param roleId
     * @return
     */
    List<SysRoleMenu> getSysRoleMenu(@Param("roleId") Integer roleId);
}
