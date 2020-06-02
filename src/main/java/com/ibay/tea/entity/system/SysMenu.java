package com.ibay.tea.entity.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 菜单实体类
 */
@Data
public class SysMenu {
    /**
     * 物理主键
     */
    private int id;
    /**
     * 父菜单id
     */
    private int parentId;
    /**
     * 父菜单id,使用逗号分隔
     */
    private String parentIds;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 菜单排序
     */
    private int sort;
    /**
     * 是否展示 0:不展示 1:展示
     */
    private int isShow;
    /**
     * 菜单权限
     */
    private String permission;
    /**
     * 目标
     */
    private String target;
    /**
     * 连接
     */
    private String href;
    /**
     * 图标
     */
    private String icon;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 是否删除标识
     */
    private int delFlag;
    /**
     * 创建用户
     */
    private String createBy;
    /**
     * 更新用户
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

}
