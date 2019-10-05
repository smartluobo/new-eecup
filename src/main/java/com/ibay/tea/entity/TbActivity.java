package com.ibay.tea.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class TbActivity {
    private Integer id;

    private Integer startDate;

    private Integer endDate;

    private Integer startHour;

    private Integer endHour;

    private Integer activityType;

    private Date createTime;

    private Date updateTime;

    private String activityName;

    private int status;

    private String tips;

    private int storeId;

    private String noStartPoster;

    private String startingPoster;

    private String winPoster;

    private String noWinPoster;

    private String repeatPoster;

    private String extractTime;

    private String showImageUrl;

    private String activityRatio;

    private String emptyPoster;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStartDate() {
        return startDate;
    }

    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    public Integer getEndDate() {
        return endDate;
    }

    public void setEndDate(Integer endDate) {
        this.endDate = endDate;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public Integer getActivityType() {
        return activityType;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
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

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName == null ? null : activityName.trim();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getNoStartPoster() {
        return noStartPoster;
    }

    public void setNoStartPoster(String noStartPoster) {
        this.noStartPoster = noStartPoster;
    }

    public String getStartingPoster() {
        return startingPoster;
    }

    public void setStartingPoster(String startingPoster) {
        this.startingPoster = startingPoster;
    }

    public String getWinPoster() {
        return winPoster;
    }

    public void setWinPoster(String winPoster) {
        this.winPoster = winPoster;
    }

    public String getExtractTime() {
        return extractTime;
    }

    public void setExtractTime(String extractTime) {
        this.extractTime = extractTime;
    }

    public String getShowImageUrl() {
        return showImageUrl;
    }

    public void setShowImageUrl(String showImageUrl) {
        this.showImageUrl = showImageUrl;
    }

    public String getActivityRatio() {
        return activityRatio;
    }

    public void setActivityRatio(String activityRatio) {
        this.activityRatio = activityRatio;
    }

    public String getNoWinPoster() {
        return noWinPoster;
    }

    public void setNoWinPoster(String noWinPoster) {
        this.noWinPoster = noWinPoster;
    }

    public String getRepeatPoster() {
        return repeatPoster;
    }

    public void setRepeatPoster(String repeatPoster) {
        this.repeatPoster = repeatPoster;
    }

    public String getEmptyPoster() {
        return emptyPoster;
    }

    public void setEmptyPoster(String emptyPoster) {
        this.emptyPoster = emptyPoster;
    }

    public TbActivity copy(){
        String thisStr = JSONObject.toJSONString(this);
        return JSONObject.parseObject(thisStr, TbActivity.class);
    }

    @Override
    public String toString() {
        return "TbActivity{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                ", activityType=" + activityType +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", activityName='" + activityName + '\'' +
                ", status=" + status +
                ", tips='" + tips + '\'' +
                ", storeId=" + storeId +
                ", noStartPoster='" + noStartPoster + '\'' +
                ", startingPoster='" + startingPoster + '\'' +
                ", winPoster='" + winPoster + '\'' +
                ", noWinPoster='" + noWinPoster + '\'' +
                ", repeatPoster='" + repeatPoster + '\'' +
                ", extractTime='" + extractTime + '\'' +
                ", showImageUrl='" + showImageUrl + '\'' +
                ", activityRatio='" + activityRatio + '\'' +
                '}';
    }
}