package com.ibay.tea.entity;

import java.util.Date;

public class TbPrinter {
    private Integer id;

    private String printerSn;

    private String printerKey;

    private String printerRemark;

    private String printerCarnum;

    private Date createTime;

    private Integer printerType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrinterSn() {
        return printerSn;
    }

    public void setPrinterSn(String printerSn) {
        this.printerSn = printerSn == null ? null : printerSn.trim();
    }

    public String getPrinterKey() {
        return printerKey;
    }

    public void setPrinterKey(String printerKey) {
        this.printerKey = printerKey == null ? null : printerKey.trim();
    }

    public String getPrinterRemark() {
        return printerRemark;
    }

    public void setPrinterRemark(String printerRemark) {
        this.printerRemark = printerRemark == null ? null : printerRemark.trim();
    }

    public String getPrinterCarnum() {
        return printerCarnum;
    }

    public void setPrinterCarnum(String printerCarnum) {
        this.printerCarnum = printerCarnum == null ? null : printerCarnum.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getPrinterType() {
        return printerType;
    }

    public void setPrinterType(Integer printerType) {
        this.printerType = printerType;
    }

    @Override
    public String toString() {
        return "TbPrinter{" +
                "id=" + id +
                ", printerSn='" + printerSn + '\'' +
                ", printerKey='" + printerKey + '\'' +
                ", printerRemark='" + printerRemark + '\'' +
                ", printerCarnum='" + printerCarnum + '\'' +
                ", createTime=" + createTime +
                ", printerType=" + printerType +
                '}';
    }
}