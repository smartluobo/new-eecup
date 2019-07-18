package com.ibay.tea.entity;

import java.util.Date;

public class TbApiUser {
    private Integer id;

    private String nickName;

    private String wechatNum;

    private String oppenId;

    private String wechatPhoneNum;

    private String userBindPhoneNum;

    private Date createTime;

    private Date updateTime;

    public String getUserHeadImage() {
        return userHeadImage;
    }

    public void setUserHeadImage(String userHeadImage) {
        this.userHeadImage = userHeadImage;
    }

    private String userHeadImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getWechatNum() {
        return wechatNum;
    }

    public void setWechatNum(String wechatNum) {
        this.wechatNum = wechatNum == null ? null : wechatNum.trim();
    }

    public String getOppenId() {
        return oppenId;
    }

    public void setOppenId(String oppenId) {
        this.oppenId = oppenId == null ? null : oppenId.trim();
    }

    public String getWechatPhoneNum() {
        return wechatPhoneNum;
    }

    public void setWechatPhoneNum(String wechatPhoneNum) {
        this.wechatPhoneNum = wechatPhoneNum == null ? null : wechatPhoneNum.trim();
    }

    public String getUserBindPhoneNum() {
        return userBindPhoneNum;
    }

    public void setUserBindPhoneNum(String userBindPhoneNum) {
        this.userBindPhoneNum = userBindPhoneNum == null ? null : userBindPhoneNum.trim();
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
}