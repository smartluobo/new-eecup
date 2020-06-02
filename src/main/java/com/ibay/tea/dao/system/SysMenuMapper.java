package com.ibay.tea.dao.system;


import com.ibay.tea.entity.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 系统菜单Dao层
 */
@Mapper
@Component
public interface SysMenuMapper {
    /**
     * 获取所有的系统菜单
     * @return
     */
    List<SysMenu> getAllSysMenu();

    /**
     * 保存系统菜单
     * @param sysMenu
     */
    void save(SysMenu sysMenu);

    /**
     * 更新系统菜单
     * @param sysMenu
     */
    void update(SysMenu sysMenu);

    /**
     * 根据用户id查询系统菜单
     * @param userId
     * @return
     */
    List<SysMenu> getSysMenuByUserId(@Param("userId") Integer userId);

    /**
     * 根据id查询系统菜单信息
     * @param id
     * @return
     */
    SysMenu getSysMenuById(@Param("id") Integer id);

    /**
     * 删除系统菜单
     * @param id
     * @param updateUser
     * @param updateTime
     */
    void deleteSysMenuById(@Param("id") Integer id, @Param("updateBy") String updateUser, @Param("updateDate") Date updateTime);

    /**
     * 根据父菜单id查询
     * @param parentIds
     * @return
     */
    List<SysMenu> getSysMenuByParentIds(@Param("parentIds") String parentIds);
}
