package com.ibay.tea.entity.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 角色信息实体

 */
@Data
public class SysRole {
    /**
     * 物理主键
     */
    private Integer id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 数据权限
     */
    private String dataScope;
    /**
     * 角色类型 0:测试 1:运营 2:公共
     */
    private int roleType;
    /**
     * 备注信息
     */
    private String remarks;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 更新者
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
     * 角色的tab权限
     */
    private String tabAuth;
    /**
     * 是否删除 0:否 1:是
     */
    private int delFlag;
    /**
     * 更新用户名称
     */
    private String updateUser;
}
