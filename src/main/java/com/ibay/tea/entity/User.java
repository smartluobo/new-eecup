package com.ibay.tea.entity;

import java.util.Date;

public class User {
    private Integer id;

    private String wechatNickName;

    private String wechatNumber;

    private String wechatOppenId;

    private String wechatPhone;

    private String phone;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWechatNickName() {
        return wechatNickName;
    }

    public void setWechatNickName(String wechatNickName) {
        this.wechatNickName = wechatNickName == null ? null : wechatNickName.trim();
    }

    public String getWechatNumber() {
        return wechatNumber;
    }

    public void setWechatNumber(String wechatNumber) {
        this.wechatNumber = wechatNumber == null ? null : wechatNumber.trim();
    }

    public String getWechatOppenId() {
        return wechatOppenId;
    }

    public void setWechatOppenId(String wechatOppenId) {
        this.wechatOppenId = wechatOppenId == null ? null : wechatOppenId.trim();
    }

    public String getWechatPhone() {
        return wechatPhone;
    }

    public void setWechatPhone(String wechatPhone) {
        this.wechatPhone = wechatPhone == null ? null : wechatPhone.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
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
        return "User{" +
                "id=" + id +
                ", wechatNickName='" + wechatNickName + '\'' +
                ", wechatNumber='" + wechatNumber + '\'' +
                ", wechatOppenId='" + wechatOppenId + '\'' +
                ", wechatPhone='" + wechatPhone + '\'' +
                ", phone='" + phone + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}