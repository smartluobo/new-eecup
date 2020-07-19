package com.ibay.tea.entity.charge;

import lombok.Data;

import java.util.Date;

@Data
public class UserAccountRecord {

    private int id;
    private int type;
    private String amount;
    private String orderId;
    private int storeId;
    private String preAmount;
    private String afterAmount;
    private int cmsUserId;
    private String cmsUserName;
    private Date createTime;
}
