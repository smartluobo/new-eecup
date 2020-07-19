package com.ibay.tea.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibay.tea.common.utils.DateUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class TbOrder {
    private String orderId;

    private double payment;

    private Integer paymentType;

    private double postFee;

    private Integer status;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date consignTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date closeTime;

    private String shippingName;

    private String shippingCode;

    private Long userId;

    private String buyerMessage;

    private String buyerNick;

    private Boolean buyerComment;

    private String oppenId;

    private Integer selfGet;

    private String address;

    private String phoneNum;

    private String posterUrl;

    private List<TbOrderItem> orderItems;

    private String createDateStr;

    private String takeCode;

    //订单商品数量
    private int goodsTotalCount = 1;

    //订单支付金额
    private double orderPayment;

    //优惠券减少金额
    private double couponsReduceAmount;

    //制作完成消息发送状态 0 未发送 1发送成功 2 发送失败
    private int makeCompleteSendStatus;

    //订单关闭消息发送状态 0未发送 1发送成功 2发送失败
    private int closeSendStatus;

    //订单店铺id
    private int storeId;

    //订单商铺名称
    private String storeName;

    private String goodsName;

    private int userCouponsId;

    private String userCouponsName = "无优惠";

    private int currentIndex;

    private int userAddressId;

    private int isFirstOrder;

    public void setCreateTime(Date createTime) {
        if (createTime != null){
            this.createDateStr = DateUtil.viewDateFormat(createTime);
        }
        this.createTime = createTime;
    }

    public void setCreateDateStr(String createDateStr) {
        if (StringUtils.isNotEmpty(createDateStr)){
            this.createDateStr = createDateStr;
        }
    }
}
