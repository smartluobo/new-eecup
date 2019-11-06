package com.ibay.tea.entity;

import java.util.Date;

public class TbCoupons {
    private int id;

    private int couponsType;

    private String couponsName;

    private String couponsRatio;

    private int consumeAmount;

    private int reduceAmount;

    private int consumeCount;

    private int giveCount;

    private Date createTime;

    private Date updateTime;

    private String couponsPoster;

    private String useRules;

    private String useScope;

    private String storePoster;

    private String cashAmount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCouponsType() {
        return couponsType;
    }

    public void setCouponsType(int couponsType) {
        this.couponsType = couponsType;
    }

    public String getCouponsName() {
        return couponsName;
    }

    public void setCouponsName(String couponsName) {
        this.couponsName = couponsName;
    }

    public String getCouponsRatio() {
        return couponsRatio;
    }

    public void setCouponsRatio(String couponsRatio) {
        this.couponsRatio = couponsRatio;
    }

    public int getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(int consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    public int getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(int reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    public int getConsumeCount() {
        return consumeCount;
    }

    public void setConsumeCount(int consumeCount) {
        this.consumeCount = consumeCount;
    }

    public int getGiveCount() {
        return giveCount;
    }

    public void setGiveCount(int giveCount) {
        this.giveCount = giveCount;
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

    public String getCouponsPoster() {
        return couponsPoster;
    }

    public void setCouponsPoster(String couponsPoster) {
        this.couponsPoster = couponsPoster;
    }

    public String getUseRules() {
        return useRules;
    }

    public void setUseRules(String useRules) {
        this.useRules = useRules;
    }

    public String getUseScope() {
        return useScope;
    }

    public void setUseScope(String useScope) {
        this.useScope = useScope;
    }

    public String getStorePoster() {
        return storePoster;
    }

    public void setStorePoster(String storePoster) {
        this.storePoster = storePoster;
    }

    public String getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(String cashAmount) {
        this.cashAmount = cashAmount;
    }

    @Override
    public String toString() {
        return "TbCoupons{" +
                "id=" + id +
                ", couponsType=" + couponsType +
                ", couponsName='" + couponsName + '\'' +
                ", couponsRatio='" + couponsRatio + '\'' +
                ", consumeAmount=" + consumeAmount +
                ", reduceAmount=" + reduceAmount +
                ", consumeCount=" + consumeCount +
                ", giveCount=" + giveCount +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", couponsPoster='" + couponsPoster + '\'' +
                ", useRules='" + useRules + '\'' +
                ", useScope='" + useScope + '\'' +
                ", storePoster='" + storePoster + '\'' +
                '}';
    }
}