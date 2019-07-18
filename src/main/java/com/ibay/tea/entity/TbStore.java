package com.ibay.tea.entity;

import java.util.Date;

public class TbStore {
    private Integer id;

    private String storeName;

    private String storeAddress;

    private Date createTime;

    private Date updateTime;

    private int extraPrice;

    private int orderPrinterId;

    private int orderItemPrinterId;

    private String longitude;

    private String latitude;

    private String distance;

    private int distributionDistance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName == null ? null : storeName.trim();
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress == null ? null : storeAddress.trim();
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

    public int getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(int extraPrice) {
        this.extraPrice = extraPrice;
    }

    public int getOrderPrinterId() {
        return orderPrinterId;
    }

    public void setOrderPrinterId(int orderPrinterId) {
        this.orderPrinterId = orderPrinterId;
    }

    public int getOrderItemPrinterId() {
        return orderItemPrinterId;
    }

    public void setOrderItemPrinterId(int orderItemPrinterId) {
        this.orderItemPrinterId = orderItemPrinterId;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getDistributionDistance() {
        return distributionDistance;
    }

    public void setDistributionDistance(int distributionDistance) {
        this.distributionDistance = distributionDistance;
    }
}