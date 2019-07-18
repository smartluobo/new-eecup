package com.ibay.tea.entity;

import java.util.Date;
import java.util.List;

public class TbCmsMenu {
    private int id;

    private String menuName;

    private int isParent;

    private int parentId;

    private Date createTime;

    private String menuUrl;

    private List<TbCmsMenu> subMenu;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getIsParent() {
        return isParent;
    }

    public void setIsParent(int isParent) {
        this.isParent = isParent;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public List<TbCmsMenu> getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(List<TbCmsMenu> subMenu) {
        this.subMenu = subMenu;
    }
}