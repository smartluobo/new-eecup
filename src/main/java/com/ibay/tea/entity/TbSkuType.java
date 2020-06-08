package com.ibay.tea.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TbSkuType {
    private Integer id;

    private String skuTypeName;

    private Date createTime;

    private List<TbSkuDetail> skuDetails;

    private String remark;
}
