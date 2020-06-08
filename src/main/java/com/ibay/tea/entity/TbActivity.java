package com.ibay.tea.entity;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TbActivity {
    private Integer id;

    private Integer startDate;

    private Integer endDate;

    private Integer startHour;

    private Integer endHour;

    private Integer activityType;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String activityName;

    private int status;

    private String tips;

    private int storeId;

    private String noStartPoster;

    private String startingPoster;

    private String winPoster;

    private String noWinPoster;

    private String repeatPoster;

    private String extractTime;

    private String showImageUrl;

    private String activityRatio;

    private String emptyPoster;

    private String goodsIds;



    public TbActivity copy(){
        String thisStr = JSONObject.toJSONString(this);
        return JSONObject.parseObject(thisStr, TbActivity.class);
    }

}
