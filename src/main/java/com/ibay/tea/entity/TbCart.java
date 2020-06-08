package com.ibay.tea.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TbCart {
    private Integer id;

    private String oppenId;

    private Integer goodsId;

    private Double showPrice;

    private String skuDetailIds;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private int itemCount;

    private String skuDetailDesc;

    private int storeId;
}
