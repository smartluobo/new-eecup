package com.ibay.tea.entity;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class TbItem implements Comparable<TbItem>{
    private Long id;

    private String title;

    private String sellPoint;

    private double price;

    //活动价
    private double activityPrice;

    //是否显示活动价 0-不现实 1-显示
    private int showActivityPrice;

    private int num;

    private int limitNum;

    private String image;

    private Long cid;

    private int status;

    private int storeId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updated;

    private String simpleDesc;

    private String skuTypeIds;

    private List<TbSkuType> skuShowInfos;

    private double cartPrice;

    private double cartTotalPrice;

    private int cartItemCount = 1;

    private int cartItemId;

    private String skuDetailDesc;

    private String defaultSkuDetailIds;

    private String cartSkuDetailIds;

    private String posterImage;

    private int isIngredients;

    private double tejiaPrice;

    public TbItem copy() {
        String thisStr = JSONObject.toJSONString(this);
        return JSONObject.parseObject(thisStr, TbItem.class);
    }

    @Override
    public int compareTo(TbItem o) {
        return Double.compare(this.getCartPrice(),o.getCartPrice());
    }
}
