package com.ibay.tea.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Goods {
    private Integer id;

    private String goodsName;

    private String goodsPoster;

    private String goodsPrice;

    private String goodsActivityPrice;

    private Integer showActivityPrice;

    private Date activityStartTime;

    private Date activityEndTime;

    private String detail1ImgUrl;

    private String detail2ImgUrl;

    private String detail3ImgUrl;

    private String detail4ImgUrl;

    private String detail5ImgUrl;

    private Integer haveSku;

    private Integer goodsStatus;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
