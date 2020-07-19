package com.ibay.tea.entity;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibay.tea.common.utils.DateUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TbUserCoupons {

    private Integer id;

    private String oppenId;

    private Integer couponsId;

    private String couponsName;

    private Integer receiveDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Integer status;

    private String couponsPoster;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireDate;

    private String expireDateStr;

    private String bigNum;

    private String smallNum;

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

    private String cashAmount;

    //是否当日过期 0 不是 1 是
    private int currentDayExpire;

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
        if (createTime != null){
            createTimeStr = DateUtil.viewDateFormat(createTime);
        }
    }

    public void setExpireDate(Date expireDate) {
        if (expireDate != null){
            this.expireDateStr = DateUtil.formatExpireDate(expireDate);
        }
        this.expireDate = expireDate;
    }

    public void setExpireDateStr(String expireDateStr) {
        if (this.expireDateStr == null){
            this.expireDateStr = expireDateStr;
        }
    }

    public TbUserCoupons copy() {
        String thisStr = JSONObject.toJSONString(this);
        return JSONObject.parseObject(thisStr, TbUserCoupons.class);
    }
}
