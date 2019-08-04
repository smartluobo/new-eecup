package com.ibay.tea.entity;

import com.ibay.tea.common.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TbOrder {
    private String orderId;

    private double payment;

    private Integer paymentType;

    private double postFee;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Date paymentTime;

    private Date consignTime;

    private Date endTime;

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public double getPostFee() {
        return postFee;
    }

    public void setPostFee(double postFee) {
        this.postFee = postFee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        if (createTime != null){
            this.createDateStr = DateUtil.viewDateFormat(createTime);
        }
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getConsignTime() {
        return consignTime;
    }

    public void setConsignTime(Date consignTime) {
        this.consignTime = consignTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName == null ? null : shippingName.trim();
    }

    public String getShippingCode() {
        return shippingCode;
    }

    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode == null ? null : shippingCode.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage == null ? null : buyerMessage.trim();
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick == null ? null : buyerNick.trim();
    }

    public Boolean getBuyerComment() {
        return buyerComment;
    }

    public void setBuyerComment(Boolean buyerComment) {
        this.buyerComment = buyerComment;
    }

    public String getOppenId() {
        return oppenId;
    }

    public void setOppenId(String oppenId) {
        this.oppenId = oppenId == null ? null : oppenId.trim();
    }

    public Integer getSelfGet() {
        return selfGet;
    }

    public void setSelfGet(Integer selfGet) {
        this.selfGet = selfGet;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum == null ? null : phoneNum.trim();
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl == null ? null : posterUrl.trim();
    }

    public int getGoodsTotalCount() {
        return goodsTotalCount;
    }

    public void setGoodsTotalCount(int goodsTotalCount) {
        this.goodsTotalCount = goodsTotalCount;
    }

    public double getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(double orderPayment) {
        this.orderPayment = orderPayment;
    }

    public double getCouponsReduceAmount() {
        return couponsReduceAmount;
    }

    public void setCouponsReduceAmount(double couponsReduceAmount) {
        this.couponsReduceAmount = couponsReduceAmount;
    }

    public int getMakeCompleteSendStatus() {
        return makeCompleteSendStatus;
    }

    public void setMakeCompleteSendStatus(int makeCompleteSendStatus) {
        this.makeCompleteSendStatus = makeCompleteSendStatus;
    }

    public int getCloseSendStatus() {
        return closeSendStatus;
    }

    public void setCloseSendStatus(int closeSendStatus) {
        this.closeSendStatus = closeSendStatus;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        if (StringUtils.isNotEmpty(createDateStr)){
            this.createDateStr = createDateStr;
        }
    }

    public String getTakeCode() {
        return takeCode;
    }

    public void setTakeCode(String takeCode) {
        this.takeCode = takeCode;
    }

    public int getUserCouponsId() {
        return userCouponsId;
    }

    public void setUserCouponsId(int userCouponsId) {
        this.userCouponsId = userCouponsId;
    }

    public String getUserCouponsName() {
        return userCouponsName;
    }

    public void setUserCouponsName(String userCouponsName) {
        this.userCouponsName = userCouponsName;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(int userAddressId) {
        this.userAddressId = userAddressId;
    }

    public int getIsFirstOrder() {
        return isFirstOrder;
    }

    public void setIsFirstOrder(int isFirstOrder) {
        this.isFirstOrder = isFirstOrder;
    }

    @Override
    public String toString() {
        return "TbOrder{" +
                "orderId='" + orderId + '\'' +
                ", payment=" + payment +
                ", paymentType=" + paymentType +
                ", postFee=" + postFee +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", paymentTime=" + paymentTime +
                ", consignTime=" + consignTime +
                ", endTime=" + endTime +
                ", closeTime=" + closeTime +
                ", shippingName='" + shippingName + '\'' +
                ", shippingCode='" + shippingCode + '\'' +
                ", userId=" + userId +
                ", buyerMessage='" + buyerMessage + '\'' +
                ", buyerNick='" + buyerNick + '\'' +
                ", buyerComment=" + buyerComment +
                ", oppenId='" + oppenId + '\'' +
                ", selfGet=" + selfGet +
                ", address='" + address + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", orderItems=" + orderItems +
                ", createDateStr='" + createDateStr + '\'' +
                ", takeCode='" + takeCode + '\'' +
                ", goodsTotalCount=" + goodsTotalCount +
                ", orderPayment=" + orderPayment +
                ", couponsReduceAmount=" + couponsReduceAmount +
                ", makeCompleteSendStatus=" + makeCompleteSendStatus +
                ", closeSendStatus=" + closeSendStatus +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", userCouponsId=" + userCouponsId +
                ", userCouponsName='" + userCouponsName + '\'' +
                ", currentIndex=" + currentIndex +
                ", userAddressId=" + userAddressId +
                ", isFirstOrder=" + isFirstOrder +
                '}';
    }
}