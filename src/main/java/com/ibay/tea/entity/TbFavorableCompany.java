package com.ibay.tea.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TbFavorableCompany {
    private Integer id;

    private String companyName;

    private String companyAddress;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String companyRatio;

    private int couponsType;

    private int chargeAmount;

    private int actualAmount;

}
