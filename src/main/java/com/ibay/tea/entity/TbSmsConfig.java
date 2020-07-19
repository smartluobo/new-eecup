package com.ibay.tea.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TbSmsConfig {
    private int id;

    private String templateName;

    private String signName;

    private String templateCode;

    private int storeId;

    private Date createTime;

    private int sendStatus;

    private int shouldSendCount;

    private int actualSendCount;
}
