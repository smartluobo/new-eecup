package com.ibay.tea.entity;

import lombok.Data;

@Data
public class TbRecommend {
    private Integer id;

    private Integer goodsId;

    private Integer storeId;

    private String goodsName;

    private String goodsPoster;

    private String storeName;
}
