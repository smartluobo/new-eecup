package com.ibay.tea.entity.system;

import lombok.Data;

import java.util.List;

/**
 * 系统菜单树

 */
@Data
public class SysMenuTree implements Comparable<SysMenuTree> {
    /**
     * 菜单id
     */
    private Integer id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 父菜单id
     */
    private Integer parentId;
    /**
     * 所有的父菜单id，以逗号分隔
     */
    private String parentIds;
    /**
     * 路由
     */
    private String href;
    /**
     * 图标
     */
    private String icon;
    /**
     * 是否展示
     */
    private int isShow;
    /**
     * 权限
     */
    private String permission;
    /**
     * 菜单排序
     */
    private int sort;
    /**
     * 子菜单
     */
    private List<SysMenuTree> subMenuList;


    public SysMenuTree(Integer id, String name, Integer parentId, String href, String icon,
                       int isShow, String permission, String parentIds, int sort) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.href = href;
        this.icon = icon;
        this.isShow = isShow;
        this.permission = permission;
        this.parentIds = parentIds;
        this.sort = sort;
    }

    @Override
    public int compareTo(SysMenuTree o) {
        if (this.sort > o.getSort()){
            return 1;
        } else if (this.sort < o.getSort()){
            return -1;
        }
        return 0;
    }
}
