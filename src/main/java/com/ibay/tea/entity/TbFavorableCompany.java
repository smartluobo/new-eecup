package com.ibay.tea.entity;

import java.util.Date;

public class TbFavorableCompany {
    private Integer id;

    private String companyName;

    private String companyAddress;

    private Date createTime;

    private String companyRatio;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress == null ? null : companyAddress.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCompanyRatio() {
        return companyRatio;
    }

    public void setCompanyRatio(String companyRatio) {
        this.companyRatio = companyRatio == null ? null : companyRatio.trim();
    }
}