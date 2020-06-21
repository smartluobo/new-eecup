package com.ibay.tea.dao.system;


import com.ibay.tea.entity.system.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.List;

/**
 * 系统角色dao层
 */
@Component
@Mapper
public interface SysRoleMapper {

    /**
     * 获取所有的系统角色信息
     * @param roleType
     * @return
     */
    List<SysRole> getAllSysRoleByRoleType(@Param("roleType") Integer roleType);
    /**
     * 根据用户id查询用户角色信息
     * @param userId
     * @return
     */
    List<SysRole> getSysRoleByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户id查询用户角色信息
     * @param id
     * @return
     */
    SysRole getSysRoleById(@Param("id") Integer id);

    /**
     * 获取角色信息
     * @param roleName
     * @return
     */
    SysRole getSysRole(@Param("roleName") String roleName);

    /**
     * 获取角色信息
     * @param roleName
     * @return
     */
    SysRole getSysRoleOld(@Param("roleName") String roleName);

    /**
     * 删除角色信息
     * @param id
     * @param updateUser
     * @param updateTime
     */
    void deleteSysRoleById(@Param("id") Integer id, @Param("updateUser") String updateUser, @Param("updateTime") Date updateTime);

    /**
     * 保存角色信息
     * @param sysRole
     */
    void saveSysRole(SysRole sysRole);

    /**
     * 保存角色信息到旧表
     * @param sysRole
     */
    void saveSysRoleOld(SysRole sysRole);

    /**
     * 查询老的角色信息
     * @return
     */
    List<SysRole> getSysRoleOldList();

    /**
     * 更新用户角色信息
     * @param sysRole
     */
    void updateSysRole(SysRole sysRole);

    /**
     * 查询系统角色列表
     * @param roleName
     * @return
     */
    List<SysRole> getSysRoleList(@Param("roleName") String roleName);

    /**
     * 查询当前角色关联的角色信息
     * @param id
     * @return
     */
    List<SysRole> getSysRoleAssociatedRole(@Param("id") Integer id);

    List<SysRole> getSysRoleByIds(@Param("sysRoleIds") List<Integer> sysRoleIds);
}
