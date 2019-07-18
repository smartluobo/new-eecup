package com.ibay.tea.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class TbSkuDetail {
    private Integer id;

    private Integer skuTypeId;

    private String skuDetailName;

    private int skuDetailPrice;

    private Date createTime;

    //该明细是否被选中 0-表示未选中，1-表示选中
    private int isSelected;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSkuTypeId() {
        return skuTypeId;
    }

    public void setSkuTypeId(Integer skuTypeId) {
        this.skuTypeId = skuTypeId;
    }

    public String getSkuDetailName() {
        return skuDetailName;
    }

    public void setSkuDetailName(String skuDetailName) {
        this.skuDetailName = skuDetailName == null ? null : skuDetailName.trim();
    }

    public int getSkuDetailPrice() {
        return skuDetailPrice;
    }

    public void setSkuDetailPrice(Integer skuDetailPrice) {
        this.skuDetailPrice = skuDetailPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public TbSkuDetail copy(){
        String thisStr = JSONObject.toJSONString(this);
        return JSONObject.parseObject(thisStr, TbSkuDetail.class);
    }
}