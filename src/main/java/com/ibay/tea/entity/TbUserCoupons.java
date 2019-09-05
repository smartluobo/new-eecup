package com.ibay.tea.entity;

import com.alibaba.fastjson.JSONObject;
import com.ibay.tea.common.utils.DateUtil;

import java.util.Date;

public class TbUserCoupons {

    private Integer id;

    private String oppenId;

    private Integer couponsId;

    private String couponsName;

    private Integer receiveDate;

    private Date createTime;

    private Integer status;

    private String couponsPoster;

    private Date expireDate;

    private String expireDateStr;

    private int bigNum;

    private int smallNum;

    private int couponsType;

    private String useRules;

    private String useScope;

    private int activityId;

    private int isReferrer;

    private String couponsRatio;

    //优惠券来源 0 常规活动抢券 1 分享专属 2 体验券
    private int couponsSource;

    private String couponsCode;

    //来源名称 幸运抽奖  分享专属  新人大礼包 系统派发
    private String sourceName;

    private String createTimeStr;

    //使用方式 0 小程序专享 1 门店专享
    private int useWay;

    //过期类型 0过期时间过期 1 仅限当日有效 2永久有效
    private int expireType;

    private int consumeAmount;

    private int reduceAmount;

    private int consumeCount;

    private int giveCount;

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
        this.oppenId = oppenId;
    }

    public Integer getCouponsId() {
        return couponsId;
    }

    public void setCouponsId(Integer couponsId) {
        this.couponsId = couponsId;
    }

    public String getCouponsName() {
        return couponsName;
    }

    public void setCouponsName(String couponsName) {
        this.couponsName = couponsName == null ? null : couponsName.trim();
    }

    public Integer getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Integer receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
        if (createTime != null){
            createTimeStr = DateUtil.viewDateFormat(createTime);
        }
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCouponsPoster() {
        return couponsPoster;
    }

    public void setCouponsPoster(String couponsPoster) {
        this.couponsPoster = couponsPoster;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        if (expireDate != null){
            this.expireDateStr = DateUtil.formatExpireDate(expireDate);
        }
        this.expireDate = expireDate;
    }

    public String getExpireDateStr() {
        return expireDateStr;
    }

    public void setExpireDateStr(String expireDateStr) {
        if (this.expireDateStr == null){
            this.expireDateStr = expireDateStr;
        }
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

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getIsReferrer() {
        return isReferrer;
    }

    public void setIsReferrer(int isReferrer) {
        this.isReferrer = isReferrer;
    }

    public String getCouponsRatio() {
        return couponsRatio;
    }

    public void setCouponsRatio(String couponsRatio) {
        this.couponsRatio = couponsRatio;
    }

    public int getCouponsSource() {
        return couponsSource;
    }

    public void setCouponsSource(int couponsSource) {
        this.couponsSource = couponsSource;
    }

    public String getCouponsCode() {
        return couponsCode;
    }

    public void setCouponsCode(String couponsCode) {
        this.couponsCode = couponsCode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public int getUseWay() {
        return useWay;
    }

    public void setUseWay(int useWay) {
        this.useWay = useWay;
    }

    public int getExpireType() {
        return expireType;
    }

    public void setExpireType(int expireType) {
        this.expireType = expireType;
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

    public TbUserCoupons copy() {
        String thisStr = JSONObject.toJSONString(this);
        return JSONObject.parseObject(thisStr, TbUserCoupons.class);
    }

    @Override
    public String toString() {
        return "TbUserCoupons{" +
                "id=" + id +
                ", oppenId='" + oppenId + '\'' +
                ", couponsId=" + couponsId +
                ", couponsName='" + couponsName + '\'' +
                ", receiveDate=" + receiveDate +
                ", createTime=" + createTime +
                ", status=" + status +
                ", couponsPoster='" + couponsPoster + '\'' +
                ", expireDate=" + expireDate +
                ", expireDateStr='" + expireDateStr + '\'' +
                ", bigNum=" + bigNum +
                ", smallNum=" + smallNum +
                ", couponsType=" + couponsType +
                ", useRules='" + useRules + '\'' +
                ", useScope='" + useScope + '\'' +
                ", activityId=" + activityId +
                ", isReferrer=" + isReferrer +
                ", couponsRatio='" + couponsRatio + '\'' +
                '}';
    }
}