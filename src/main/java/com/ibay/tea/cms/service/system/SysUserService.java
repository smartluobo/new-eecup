package com.ibay.tea.cms.service.system;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ibay.tea.common.CommonConstant;
import com.ibay.tea.dao.system.SysRoleMapper;
import com.ibay.tea.dao.system.SysUserMapper;
import com.ibay.tea.dao.system.SysUserRoleMapper;
import com.ibay.tea.entity.system.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 用户Service
 */
@Service
@Slf4j
public class SysUserService {

    public static final int HASH_ITERATIONS = 1024;
    private static final int SALT_SIZE = 8;
    private static final String DEFAULT_ENCODING = "UTF-8";

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Resource
    private UserCacheService userCacheService;

    /**
     * 根据用户id获取用户名称
     * @param id
     * @return
     */
    public String getUserNameById(String id){
        if (null == id) {
            return null;
        }
        SysUser sysUser = sysUserMapper.getSysUserById(Integer.valueOf(id));
        if (null == sysUser) {
            return null;
        }
        return sysUser.getName();
    }

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    public SysUserRequest getUserById(Integer id){
        if (null == id) {
            return null;
        }
        SysUser sysUser = sysUserMapper.getSysUserById(id);
        SysUserRequest request = new SysUserRequest();
        if (null != sysUser){
            BeanUtils.copyProperties(sysUser, request);
            List<SysRole> sysRoleList = sysRoleMapper.getSysRoleByUserId(sysUser.getId());
            request.setSysRoleList(sysRoleList);
        }
        return request;
    }

    /**
     * 获取当前的用户
     * @return
     */
    public SysUserRequest getCurrentUserInfo(){
        SysUserRequest sysUserRequest = new SysUserRequest();
        SysUser sysUser = userCacheService.getSysUser();
        BeanUtils.copyProperties(sysUser, sysUserRequest);
        sysUserRequest.setSysRoleList(sysRoleMapper.getSysRoleByUserId(sysUser.getId()));
        return sysUserRequest;
    }

    /**
     * 更新系统用户
     * @param user
     */

    public void updateSysUser(SysUserRequest user) {
        //更新用户信息
        if (StringUtils.isNotBlank(user.getPassword())) {
            encryptPassword(user, user.getPassword());
        }
        user.setUpdateDate(new Date());
        user.setCreateBy(String.valueOf(userCacheService.getSysUser().getId()));
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(user, sysUser);
        sysUserMapper.update(sysUser);

        //更新用户与角色的关联关系信息
        List<SysRole> sysRoleList = user.getSysRoleList();
        //角色不为空,则保存用户与角色的关联关系
        if (!CollectionUtils.isEmpty(sysRoleList)){
            sysUserRoleMapper.deleteSysUserRole(user.getId());
            List<SysUserRole> sysUserRoleList = new ArrayList<>();
            sysRoleList.forEach(sysRole -> {
                SysUserRole sysUserRole = new SysUserRole(user.getId(), sysRole.getId());
                sysUserRoleList.add(sysUserRole);
            });
            sysUserRoleMapper.batchSaveSysUserRole(sysUserRoleList);
        }

    }

    /**
     * 更新用户信息
     * @param user
     */

    public void updateSysUserInfo(SysUserRequest user) {
        SysUser sysUser = new SysUser();
        sysUser.setUpdateBy(String.valueOf(user.getId()));
        sysUser.setUpdateDate(new Date());
        sysUser.setId(user.getId());
        sysUser.setMobile(user.getMobile());
        sysUser.setEmail(user.getEmail());
        sysUser.setPhone(user.getPhone());
        sysUser.setRemarks(user.getRemarks());
        sysUserMapper.update(sysUser);
    }
    /**
     * 保存用户信息
     * @param user
     */

    public void saveSysUserById(SysUserRequest user) {
        //保存用户信息
        encryptPassword(user, user.getPassword());
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setRegisterDate(new Date());
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(user, sysUser);
        sysUser.setCreateBy(String.valueOf(userCacheService.getSysUser().getId()));
        sysUser.setUpdateBy(String.valueOf(userCacheService.getSysUser().getId()));
        sysUserMapper.save(sysUser);


        List<SysRole> sysRoleList = user.getSysRoleList();
        //角色不为空,则保存用户与角色的关联关系
        if (!CollectionUtils.isEmpty(sysRoleList)){
            List<SysUserRole> sysUserRoleList = new ArrayList<>();
            sysRoleList.forEach(sysRole -> {
                SysUserRole sysUserRole = new SysUserRole(sysUser.getId(), sysRole.getId());
                sysUserRoleList.add(sysUserRole);
            });
            sysUserRoleMapper.batchSaveSysUserRole(sysUserRoleList);
        }

    }

    /**
     * 同步用户数据
     * @param userId
     */
    public void syncSysUserById(String userId){
        log.info("sync sys user id:{}", userId);
        if (StringUtils.isBlank(userId)) {
            return;
        }
        SysUser sysUser = sysUserMapper.getSysUserById(Integer.valueOf(userId));
        if (null == sysUser){
            return;
        }
        SysUser oldSysUser = sysUserMapper.getSysUserByLoginNameOld(sysUser.getLoginName());
        if (null != oldSysUser){
            return;
        }
        sysUserMapper.saveOld(sysUser);
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        List<SysRole> sysRoles = sysRoleMapper.getSysRoleOldList();
        if (!CollectionUtils.isEmpty(sysRoles)) {
            SysUserRole sysUserRole = new SysUserRole(sysUser.getId(), sysRoles.get(0).getId());
            sysUserRoleList.add(sysUserRole);
            sysUserRoleMapper.batchSaveSysUserRoleOld(sysUserRoleList);
        }
    }
    /**
     * 根据id删除用户信息
     * @param id
     * @return
     */

    public boolean deleteSysUserById(Integer id){
        if (null == id) {
            return false;
        }
        //超级管理员账号不能被删除
        if (isAdmin(id)){
            return false;
        }
        SysUser sysUser = sysUserMapper.getSysUserById(id);
        if (null == sysUser) {
            log.info("get user by id is null,id:{}", id);
            return false;
        }
        sysUser.setDelFlag(CommonConstant.DEL);
        sysUserMapper.update(sysUser);
        return true;
    }



    /**
     * 根据登录名查询用户信息
     * @param loginName
     * @return
     */
    public SysUser getUserByLoginName(String loginName){
        return sysUserMapper.getSysUserByLoginName(loginName);
    }

    /**
     * 更新用户登录信息
     * @param id
     */
    public void updateUserLoginInfo(Integer id){
        SysUser oldSysUser = sysUserMapper.getSysUserById(id);
        if (oldSysUser != null){
            oldSysUser.setUpdateDate(new Date());
            oldSysUser.setLoginDate(new Date());
            oldSysUser.setLoginIp(SecurityUtils.getSubject().getSession().getHost());
            updateSysUser(oldSysUser);
        } else {
            log.error("update user login info user is not exit:{}", id);
        }
    }
    /**
     * 根据用户名称查询角色信息
     * @param loginName
     * @return
     */
    public Set<String> getRoleListByLoginName(String loginName){
        Set<String> roleList = new HashSet<>();
        return roleList;
    }

    /**
     * 分页查询用户信息
     * @param pageNum
     * @param pageSize
     * @param loginName
     * @param name
     * @return
     */
    public PageInfo<SysUser> getSysUserByPage(int pageNum, int pageSize, String loginName, String name){
        List<Long> dataTypeList = userCacheService.getDataType();
        if (userCacheService.getSysUser().isAdmin()) {
            dataTypeList = null;
        }
        PageHelper.startPage(pageNum, pageSize,"register_date desc");
        List<SysUser> sysUserList = sysUserMapper.getSysUserList(loginName, name, dataTypeList);
        if (!CollectionUtils.isEmpty(sysUserList)) {
            sysUserList.forEach(sysUser -> {
                SysUserPage sysUserPage = new SysUserPage();
                BeanUtils.copyProperties(sysUser, sysUserPage);
                if (null != sysUser.getCreateBy()) {
                    SysUser createUser = sysUserMapper.getSysUserById(Integer.valueOf(sysUser.getCreateBy()));
                    if (null != createUser){
                        sysUserPage.setCreateUser(createUser.getName());
                    }
                }
                if (null != sysUser.getUpdateBy()) {
                    SysUser updateUser = sysUserMapper.getSysUserById(Integer.valueOf(sysUser.getUpdateBy()));
                    if (null != updateUser) {
                        sysUserPage.setUpdateUser(updateUser.getName());
                    }
                }

            });
        }
        return new PageInfo<>(sysUserList);
    }

    /**
     * 更新用户信息
     * @param sysUser
     */
    public void updateSysUser(SysUser sysUser){
        sysUserMapper.update(sysUser);
    }

    /**
     * 根据角色id查询对应的用户
     * @param roleId
     * @return
     */
    public List<SysUser> getSysUserByRoleId(Integer roleId){
        return sysUserMapper.getSysUserByRoleId(roleId);
    }

    /**
     * 是否为超级管理员
     * @param id
     * @return
     */
    private boolean isAdmin(Integer id) {
        return id != null && id == 1;
    }

    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    public void encryptPassword(SysUser user, String newPassword) {
        if (StringUtils.isBlank(newPassword)){
            return;
        }
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        user.setSalt(Encodes.encodeHex(salt));
        try {
            byte[] hashPassword = Digests.sha1(newPassword.getBytes(DEFAULT_ENCODING), salt, HASH_ITERATIONS);
            user.setPassword(Encodes.encodeHex(hashPassword));
        } catch (UnsupportedEncodingException e) {
            log.error("encrypt password error", e);
        }
    }

    /**
     *
     * @param oldPassword
     * @param user
     * @return
     */
    public boolean checkPassword(String oldPassword, SysUser user){
        byte[] salt = Encodes.decodeHex(user.getSalt());
        byte[] hashPassword = null;
        try {
            hashPassword = Digests.sha1(oldPassword.getBytes(DEFAULT_ENCODING), salt, HASH_ITERATIONS);
            String password = Encodes.encodeHex(hashPassword);
            if (user.getPassword().equals(password)){
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }


}
