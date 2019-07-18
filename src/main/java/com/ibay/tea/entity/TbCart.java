package com.ibay.tea.entity;

import java.util.Date;

public class TbCart {
    private Integer id;

    private String oppenId;

    private Integer goodsId;

    private Double showPrice;

    private String skuDetailIds;

    private Date createTime;

    private int itemCount;

    private String skuDetailDesc;

    private int storeId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOppenId() {
        return oppenId;
    }

    public void setOppenId(String oppenId) {
        this.oppenId = oppenId == null ? null : oppenId.trim();
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Double getShowPrice() {
        return showPrice;
    }

    public void setShowPrice(Double showPrice) {
        this.showPrice = showPrice;
    }

    public String getSkuDetailIds() {
        return skuDetailIds;
    }

    public void setSkuDetailIds(String skuDetailIds) {
        this.skuDetailIds = skuDetailIds == null ? null : skuDetailIds.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getSkuDetailDesc() {
        return skuDetailDesc;
    }

    public void setSkuDetailDesc(String skuDetailDesc) {
        this.skuDetailDesc = skuDetailDesc;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "TbCart{" +
                "id=" + id +
                ", oppenId='" + oppenId + '\'' +
                ", goodsId=" + goodsId +
                ", showPrice=" + showPrice +
                ", skuDetailIds='" + skuDetailIds + '\'' +
                ", createTime=" + createTime +
                ", itemCount=" + itemCount +
                '}';
    }
}