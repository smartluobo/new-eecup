package com.ibay.tea.entity;

import java.util.Date;

public class Goods {
    private Integer id;

    private String goodsName;

    private String goodsPoster;

    private String goodsPrice;

    private String goodsActivityPrice;

    private Integer showActivityPrice;

    private Date activityStartTime;

    private Date activityEndTime;

    private String detail1ImgUrl;

    private String detail2ImgUrl;

    private String detail3ImgUrl;

    private String detail4ImgUrl;

    private String detail5ImgUrl;

    private Integer haveSku;

    private Integer goodsStatus;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public String getGoodsPoster() {
        return goodsPoster;
    }

    public void setGoodsPoster(String goodsPoster) {
        this.goodsPoster = goodsPoster == null ? null : goodsPoster.trim();
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice == null ? null : goodsPrice.trim();
    }

    public String getGoodsActivityPrice() {
        return goodsActivityPrice;
    }

    public void setGoodsActivityPrice(String goodsActivityPrice) {
        this.goodsActivityPrice = goodsActivityPrice == null ? null : goodsActivityPrice.trim();
    }

    public Integer getShowActivityPrice() {
        return showActivityPrice;
    }

    public void setShowActivityPrice(Integer showActivityPrice) {
        this.showActivityPrice = showActivityPrice;
    }

    public Date getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(Date activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public Date getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(Date activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public String getDetail1ImgUrl() {
        return detail1ImgUrl;
    }

    public void setDetail1ImgUrl(String detail1ImgUrl) {
        this.detail1ImgUrl = detail1ImgUrl == null ? null : detail1ImgUrl.trim();
    }

    public String getDetail2ImgUrl() {
        return detail2ImgUrl;
    }

    public void setDetail2ImgUrl(String detail2ImgUrl) {
        this.detail2ImgUrl = detail2ImgUrl == null ? null : detail2ImgUrl.trim();
    }

    public String getDetail3ImgUrl() {
        return detail3ImgUrl;
    }

    public void setDetail3ImgUrl(String detail3ImgUrl) {
        this.detail3ImgUrl = detail3ImgUrl == null ? null : detail3ImgUrl.trim();
    }

    public String getDetail4ImgUrl() {
        return detail4ImgUrl;
    }

    public void setDetail4ImgUrl(String detail4ImgUrl) {
        this.detail4ImgUrl = detail4ImgUrl == null ? null : detail4ImgUrl.trim();
    }

    public String getDetail5ImgUrl() {
        return detail5ImgUrl;
    }

    public void setDetail5ImgUrl(String detail5ImgUrl) {
        this.detail5ImgUrl = detail5ImgUrl == null ? null : detail5ImgUrl.trim();
    }

    public Integer getHaveSku() {
        return haveSku;
    }

    public void setHaveSku(Integer haveSku) {
        this.haveSku = haveSku;
    }

    public Integer getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(Integer goodsStatus) {
        this.goodsStatus = goodsStatus;
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
        return "Goods{" +
                "id=" + id +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPoster='" + goodsPoster + '\'' +
                ", goodsPrice='" + goodsPrice + '\'' +
                ", goodsActivityPrice='" + goodsActivityPrice + '\'' +
                ", showActivityPrice=" + showActivityPrice +
                ", activityStartTime=" + activityStartTime +
                ", activityEndTime=" + activityEndTime +
                ", detail1ImgUrl='" + detail1ImgUrl + '\'' +
                ", detail2ImgUrl='" + detail2ImgUrl + '\'' +
                ", detail3ImgUrl='" + detail3ImgUrl + '\'' +
                ", detail4ImgUrl='" + detail4ImgUrl + '\'' +
                ", detail5ImgUrl='" + detail5ImgUrl + '\'' +
                ", haveSku=" + haveSku +
                ", goodsStatus=" + goodsStatus +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}