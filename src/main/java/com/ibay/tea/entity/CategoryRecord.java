package com.ibay.tea.entity;

import java.util.Date;

public class CategoryRecord {
    private Integer id;

    private Integer goodsId;

    private Integer categoryId;

    private Integer goodsWeight;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(Integer goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "CategoryRecord{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", categoryId=" + categoryId +
                ", goodsWeight=" + goodsWeight +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}