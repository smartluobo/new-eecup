package com.ibay.tea.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TbCoupons {
    private int id;

    private int couponsType;

    private String couponsName;

    private String couponsRatio;

    private int consumeAmount;

    private int reduceAmount;

    private int consumeCount;

    private int giveCount;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String couponsPoster;

    private String useRules;

    private String useScope;

    private String storePoster;

    private String cashAmount;

    //过期类型 0默认 1当日有效 2 永久有效
    private int expireType;
}
