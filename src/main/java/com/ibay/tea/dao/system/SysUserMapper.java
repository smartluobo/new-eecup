package com.ibay.tea.dao.system;


import com.ibay.tea.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户dao层
 */
@Component
@Mapper
public interface SysUserMapper {
    /**
     * 保存用户信息
     *
     * @param sysUser
     * @return
     */
    Integer save(SysUser sysUser);


    /**
     * 根据用户登录名查询用户信息
     *
     * @param loginName
     * @return
     */
    SysUser getSysUserByLoginName(@Param("loginName") String loginName);



    /**
     * 查询用户列表
     *
     * @param loginName
     * @param name
     * @return
     */
    List<SysUser> getSysUserList(@Param("loginName") String loginName, @Param("name") String name);

    /**
     * 根据id查询用户信息
     *
     * @param id
     * @return
     */
    SysUser getSysUserById(@Param("id") Integer id);


    /**
     * 更新用户信息
     *
     * @param sysUser
     */
    void update(SysUser sysUser);

    /**
     * 根据角色id查询对应的用户信息
     *
     * @param roleId
     * @return
     */
    List<SysUser> getSysUserByRoleId(@Param("roleId") Integer roleId);

    SysUser getUserById(Integer id);

    SysUser getUserByName(String userName);
}
