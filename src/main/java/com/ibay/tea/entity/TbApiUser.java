package com.ibay.tea.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TbApiUser {
    private Integer id;

    private String nickName;

    private String wechatNum;

    private String oppenId;

    private String wechatPhoneNum;

    private String userBindPhoneNum;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String referrerOppenId;

    private int companyId;

    private String companyName;

    //是否领取过新人礼包 0 未领取  1 已领取
    private int giftReceiveStatus;

    //会员折扣
    private String membersDiscount;

    private String userHeadImage;
}
