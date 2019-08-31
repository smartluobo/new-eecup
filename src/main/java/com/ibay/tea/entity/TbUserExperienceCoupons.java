package com.ibay.tea.entity;

import java.util.Date;

public class TbUserExperienceCoupons {
    private Integer id;

    private String oppenId;

    private Integer experienceCouponsId;

    private String experienceCouponsCode;

    private Integer status;

    private Date expireTime;

    private Date createTime;

    private String backgroundUrl;

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

    public Integer getExperienceCouponsId() {
        return experienceCouponsId;
    }

    public void setExperienceCouponsId(Integer experienceCouponsId) {
        this.experienceCouponsId = experienceCouponsId;
    }

    public String getExperienceCouponsCode() {
        return experienceCouponsCode;
    }

    public void setExperienceCouponsCode(String experienceCouponsCode) {
        this.experienceCouponsCode = experienceCouponsCode == null ? null : experienceCouponsCode.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl == null ? null : backgroundUrl.trim();
    }
}