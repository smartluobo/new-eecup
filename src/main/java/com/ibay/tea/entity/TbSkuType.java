package com.ibay.tea.entity;

import java.util.Date;
import java.util.List;

public class TbSkuType {
    private Integer id;

    private String skuTypeName;

    private Date createTime;

    private List<TbSkuDetail> skuDetails;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSkuTypeName() {
        return skuTypeName;
    }

    public void setSkuTypeName(String skuTypeName) {
        this.skuTypeName = skuTypeName == null ? null : skuTypeName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<TbSkuDetail> getSkuDetails() {
        return skuDetails;
    }

    public void setSkuDetails(List<TbSkuDetail> skuDetails) {
        this.skuDetails = skuDetails;
    }
}