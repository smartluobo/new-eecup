package com.ibay.tea.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TbExperienceCouponsPool {
    private Integer id;

    private Integer activityId;

    private String couponsCode;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String backgroundUrl;

    private int couponsId;

    private int receiveStatus;

    private int couponsType;

    private String couponsName;

    private int isEmpty;
}
