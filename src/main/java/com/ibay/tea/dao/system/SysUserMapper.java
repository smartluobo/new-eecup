package com.ibay.tea.dao.system;


import com.ibay.tea.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户dao层
 *
 * @author Sunny
 * @version 1.0
 * @className SysUserMapper
 * @date 2019-07-23 15:24
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
     * 保存到老的表中
     *
     * @param sysUser
     * @return
     */
    Integer saveOld(SysUser sysUser);

    /**
     * 根据用户登录名查询用户信息
     *
     * @param loginName
     * @return
     */
    SysUser getSysUserByLoginName(@Param("loginName") String loginName);

    /**
     * 根据用户登录名查询用户信息
     *
     * @param loginName
     * @return
     */
    SysUser getSysUserByLoginNameOld(@Param("loginName") String loginName);

    /**
     * 根据id删除用户
     * @param id
     */
//    void deleteSysUserById(@Param("id") Integer id);

    /**
     * 查询用户列表
     *
     * @param loginName
     * @param name
     * @param dataTypeList
     * @return
     */
    List<SysUser> getSysUserList(@Param("loginName") String loginName, @Param("name") String name,
                                 @Param("dataTypeList") List<Long> dataTypeList);

    /**
     * 根据id查询用户信息
     *
     * @param id
     * @return
     */
    SysUser getSysUserById(@Param("id") Integer id);

    /**
     * 获取所有的tab权限
     *
     * @return
     */
    List<String> getAllTabAuth();

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
}
