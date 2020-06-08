package com.ibay.tea.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;

@Data
public class TbSkuDetail {
    private Integer id;

    private Integer skuTypeId;

    private String skuDetailName;

    private int skuDetailPrice;

    private Date createTime;

    //该明细是否被选中 0-表示未选中，1-表示选中
    private int isSelected;

    private String cmsView;

    public TbSkuDetail copy(){
        String thisStr = JSONObject.toJSONString(this);
        return JSONObject.parseObject(thisStr, TbSkuDetail.class);
    }
}
