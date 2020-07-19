package com.ibay.tea.entity;

import lombok.Data;

@Data
public class TbOrderItem {
    private String id;

    private long itemId;

    private String orderId;

    private int num;

    private String title;

    private double price;

    private double totalFee;

    private String picPath;

    private String skuDetailIds;

    private String skuDetailDesc;
}
