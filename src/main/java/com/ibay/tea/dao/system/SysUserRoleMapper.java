package com.ibay.tea.dao.system;


import com.ibay.tea.entity.system.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统用户与角色的关联关系dao层
 */
@Mapper
@Component
public interface SysUserRoleMapper {
    /**
     * 保存系统用户与角色的关联关系
     * @param sysUserRole
     */
    void saveSysUserRole(SysUserRole sysUserRole);

    /**
     * 批量保存
     * @param sysUserRole
     */
    void batchSaveSysUserRole(List<SysUserRole> sysUserRole);

    /**
     * 保存到老的表中
     * @param sysUserRole
     */
    void batchSaveSysUserRoleOld(List<SysUserRole> sysUserRole);

    /**
     * 删除系统用户与角色的关联关系
     * @param userId
     */
    void deleteSysUserRole(@Param("userId") Integer userId);

    /**
     * 查询用户关联的角色信息
     * @param userId
     * @return
     */
    List<SysUserRole> selectSysUserRole(@Param("userId") Integer userId);

}
