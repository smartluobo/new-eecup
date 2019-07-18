package com.ibay.tea.entity;

import com.alibaba.fastjson.JSONObject;

public class TbActivityCouponsRecord {
    private int id;

    private int activityId;

    private String activityName;

    private int couponsId;

    private String couponsName;

    private int couponsCount;

    private String couponsPoster;

    private int bigNum;

    private int smallNum;

    private int couponsType;

    private String useRules;

    private String useScope;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getCouponsId() {
        return couponsId;
    }

    public void setCouponsId(int couponsId) {
        this.couponsId = couponsId;
    }

    public String getCouponsName() {
        return couponsName;
    }

    public void setCouponsName(String couponsName) {
        this.couponsName = couponsName;
    }

    public int getCouponsCount() {
        return couponsCount;
    }

    public void setCouponsCount(int couponsCount) {
        this.couponsCount = couponsCount;
    }

    public String getCouponsPoster() {
        return couponsPoster;
    }

    public void setCouponsPoster(String couponsPoster) {
        this.couponsPoster = couponsPoster;
    }

    public int getBigNum() {
        return bigNum;
    }

    public void setBigNum(int bigNum) {
        this.bigNum = bigNum;
    }

    public int getSmallNum() {
        return smallNum;
    }

    public void setSmallNum(int smallNum) {
        this.smallNum = smallNum;
    }

    public int getCouponsType() {
        return couponsType;
    }

    public void setCouponsType(int couponsType) {
        this.couponsType = couponsType;
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

    public TbActivityCouponsRecord copy() {
        String thisStr = JSONObject.toJSONString(this);
        return JSONObject.parseObject(thisStr, TbActivityCouponsRecord.class);
    }

    @Override
    public String toString() {
        return "TbActivityCouponsRecord{" +
                "id=" + id +
                ", activityId=" + activityId +
                ", activityName='" + activityName + '\'' +
                ", couponsId=" + couponsId +
                ", couponsName='" + couponsName + '\'' +
                ", couponsCount=" + couponsCount +
                ", couponsPoster='" + couponsPoster + '\'' +
                ", bigNum=" + bigNum +
                ", smallNum=" + smallNum +
                ", couponsType=" + couponsType +
                ", useRules='" + useRules + '\'' +
                ", useScope='" + useScope + '\'' +
                '}';
    }
}