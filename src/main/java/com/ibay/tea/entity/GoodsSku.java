package com.ibay.tea.entity;

import java.util.Date;

public class GoodsSku {
    private Integer id;

    private String skuName;

    private String skuPrice;

    private Integer skuTypeId;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName == null ? null : skuName.trim();
    }

    public String getSkuPrice() {
        return skuPrice;
    }

    public void setSkuPrice(String skuPrice) {
        this.skuPrice = skuPrice == null ? null : skuPrice.trim();
    }

    public Integer getSkuTypeId() {
        return skuTypeId;
    }

    public void setSkuTypeId(Integer skuTypeId) {
        this.skuTypeId = skuTypeId;
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
        return "GoodsSku{" +
                "id=" + id +
                ", skuName='" + skuName + '\'' +
                ", skuPrice='" + skuPrice + '\'' +
                ", skuTypeId=" + skuTypeId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}