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

    private String referrerOppenId;

    private int companyId;

    private String companyName;

    //是否领取过新人礼包 0 未领取  1 已领取
    private int giftReceiveStatus;

    //会员折扣
    private String membersDiscount;

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

    public String getReferrerOppenId() {
        return referrerOppenId;
    }

    public void setReferrerOppenId(String referrerOppenId) {
        this.referrerOppenId = referrerOppenId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getGiftReceiveStatus() {
        return giftReceiveStatus;
    }

    public void setGiftReceiveStatus(int giftReceiveStatus) {
        this.giftReceiveStatus = giftReceiveStatus;
    }

    public String getMembersDiscount() {
        return membersDiscount;
    }

    public void setMembersDiscount(String membersDiscount) {
        this.membersDiscount = membersDiscount;
    }

    @Override
    public String toString() {
        return "TbApiUser{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", wechatNum='" + wechatNum + '\'' +
                ", oppenId='" + oppenId + '\'' +
                ", wechatPhoneNum='" + wechatPhoneNum + '\'' +
                ", userBindPhoneNum='" + userBindPhoneNum + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", referrerOppenId='" + referrerOppenId + '\'' +
                ", userHeadImage='" + userHeadImage + '\'' +
                '}';
    }
}