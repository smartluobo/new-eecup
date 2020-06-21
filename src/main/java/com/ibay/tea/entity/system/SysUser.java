package com.ibay.tea.entity.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 用户实体类
 */
@Data
public class SysUser {
    /**
     * 物理主键
     */
    private Integer id;
    /**
     * 用户登录名称
     */
    private String loginName;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 注册时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date registerDate;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 登录ip
     */
    private String loginIp;
    /**
     * 登录时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date loginDate;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 修改者
     */
    private String updateBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createDate;
    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateDate;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 是否删除 0:否 1:是
     */
    private int delFlag;
    /**
     * 创建者
     */
    private String createUser;
    /**
     * 修改者
     */
    private String updateUser;

    private int isAdmin;

    private String storeIds;

    private String storeName;

    /**
     * 判断当前用户是否为admin用户
     * @return
     */
    public boolean isAdmin(){
        return StringUtils.isNotBlank(loginName) && "admin".equals(loginName);
    }
}
