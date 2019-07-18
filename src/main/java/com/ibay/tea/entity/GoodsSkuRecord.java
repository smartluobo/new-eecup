package com.ibay.tea.entity;

import java.util.Date;

public class GoodsSkuRecord {
    private Integer id;

    private Integer goodsId;

    private Integer skuId;

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

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
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
        return "GoodsSkuRecord{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", skuId=" + skuId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}