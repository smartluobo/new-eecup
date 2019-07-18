package com.ibay.tea.api.paramVo;

public class CartOrderParamVo {

   private String oppenId;
   private String cartItemIds;
   private int userCouponsId;
   private int addressId;
   private int selfGet;
   private int storeId;
   private String orderId;

    public String getOppenId() {
        return oppenId;
    }

    public void setOppenId(String oppenId) {
        this.oppenId = oppenId;
    }

    public String getCartItemIds() {
        return cartItemIds;
    }

    public void setCartItemIds(String cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public int getUserCouponsId() {
        return userCouponsId;
    }

    public void setUserCouponsId(int userCouponsId) {
        this.userCouponsId = userCouponsId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getSelfGet() {
        return selfGet;
    }

    public void setSelfGet(int selfGet) {
        this.selfGet = selfGet;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "CartOrderParamVo{" +
                "oppenId='" + oppenId + '\'' +
                ", cartItemIds='" + cartItemIds + '\'' +
                ", userCouponsId=" + userCouponsId +
                ", addressId=" + addressId +
                ", selfGet=" + selfGet +
                ", storeId=" + storeId +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
