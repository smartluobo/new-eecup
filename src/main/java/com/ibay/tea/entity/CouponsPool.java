package com.ibay.tea.entity;

import java.util.Date;

public class CouponsPool {
    private Integer id;

    private String couponsCode;

    private Date couponsStartTime;

    private Date couponsEndTime;

    private Integer couponsType;

    private Integer couponsCondition;

    private String couponsRatio;

    private Integer couponsAmount;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCouponsCode() {
        return couponsCode;
    }

    public void setCouponsCode(String couponsCode) {
        this.couponsCode = couponsCode == null ? null : couponsCode.trim();
    }

    public Date getCouponsStartTime() {
        return couponsStartTime;
    }

    public void setCouponsStartTime(Date couponsStartTime) {
        this.couponsStartTime = couponsStartTime;
    }

    public Date getCouponsEndTime() {
        return couponsEndTime;
    }

    public void setCouponsEndTime(Date couponsEndTime) {
        this.couponsEndTime = couponsEndTime;
    }

    public Integer getCouponsType() {
        return couponsType;
    }

    public void setCouponsType(Integer couponsType) {
        this.couponsType = couponsType;
    }

    public Integer getcouponsCondition() {
        return couponsCondition;
    }

    public void setcouponsCondition(Integer couponsCondition) {
        this.couponsCondition = couponsCondition;
    }

    public String getCouponsRatio() {
        return couponsRatio;
    }

    public void setCouponsRatio(String couponsRatio) {
        this.couponsRatio = couponsRatio == null ? null : couponsRatio.trim();
    }

    public Integer getCouponsAmount() {
        return couponsAmount;
    }

    public void setCouponsAmount(Integer couponsAmount) {
        this.couponsAmount = couponsAmount;
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
        return "CouponsPool{" +
                "id=" + id +
                ", couponsCode='" + couponsCode + '\'' +
                ", couponsStartTime=" + couponsStartTime +
                ", couponsEndTime=" + couponsEndTime +
                ", couponsType=" + couponsType +
                ", couponsCondition=" + couponsCondition +
                ", couponsRatio='" + couponsRatio + '\'' +
                ", couponsAmount=" + couponsAmount +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}