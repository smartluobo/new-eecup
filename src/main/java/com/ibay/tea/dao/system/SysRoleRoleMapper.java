package com.ibay.tea.dao.system;


import com.ibay.tea.entity.system.SysRoleRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 与角色关联的角色信息Dao层
 */
@Mapper
@Component
public interface SysRoleRoleMapper {
    /**
     * 删除角色与角色的关联关系
     * @param roleId
     */
    void deleteSysRoleRole(@Param("roleId") Integer roleId);

    /**
     * 批量保存角色与角色的关联关系
     * @param sysRoleRoleList
     */
    void batchSaveSysRoleRole(List<SysRoleRole> sysRoleRoleList);
}
