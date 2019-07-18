package com.ibay.tea.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "print.sys")
@Component
public class PrintSysProperties {

    private String printUserName;

    private String printUKey;

    private String printUrl;

    private String orderItemUrl;

    private String orderItemUserId;

    private String orderItemApiKey;

    public String getPrintUserName() {
        return printUserName;
    }

    public void setPrintUserName(String printUserName) {
        this.printUserName = printUserName;
    }

    public String getPrintUKey() {
        return printUKey;
    }

    public void setPrintUKey(String printUKey) {
        this.printUKey = printUKey;
    }

    public String getPrintUrl() {
        return printUrl;
    }

    public void setPrintUrl(String printUrl) {
        this.printUrl = printUrl;
    }

    public String getOrderItemUrl() {
        return orderItemUrl;
    }

    public void setOrderItemUrl(String orderItemUrl) {
        this.orderItemUrl = orderItemUrl;
    }

    public String getOrderItemUserId() {
        return orderItemUserId;
    }

    public void setOrderItemUserId(String orderItemUserId) {
        this.orderItemUserId = orderItemUserId;
    }

    public String getOrderItemApiKey() {
        return orderItemApiKey;
    }

    public void setOrderItemApiKey(String orderItemApiKey) {
        this.orderItemApiKey = orderItemApiKey;
    }

    @Override
    public String toString() {
        return "PrintSysProperties{" +
                "printUserName='" + printUserName + '\'' +
                ", printUKey='" + printUKey + '\'' +
                ", printUrl='" + printUrl + '\'' +
                ", orderItemUrl='" + orderItemUrl + '\'' +
                ", orderItemUserId='" + orderItemUserId + '\'' +
                ", orderItemApiKey='" + orderItemApiKey + '\'' +
                '}';
    }
}
