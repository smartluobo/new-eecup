package com.ibay.tea.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class TbActivityCouponsRecord {
    private int id;

    private int activityId;

    private String activityName;

    private int couponsId;

    private String couponsName;

    private int couponsCount;

    private String couponsPoster;

    private int bigNum;

    private int smallNum;

    private int couponsType;

    private String useRules;

    private String useScope;



    public TbActivityCouponsRecord copy() {
        String thisStr = JSONObject.toJSONString(this);
        return JSONObject.parseObject(thisStr, TbActivityCouponsRecord.class);
    }


}
