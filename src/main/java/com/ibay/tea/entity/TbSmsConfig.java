package com.ibay.tea.entity;

import java.util.Date;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName == null ? null : templateName.trim();
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName == null ? null : signName.trim();
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode == null ? null : templateCode.trim();
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

    public int getShouldSendCount() {
        return shouldSendCount;
    }

    public void setShouldSendCount(int shouldSendCount) {
        this.shouldSendCount = shouldSendCount;
    }

    public int getActualSendCount() {
        return actualSendCount;
    }

    public void setActualSendCount(int actualSendCount) {
        this.actualSendCount = actualSendCount;
    }
}