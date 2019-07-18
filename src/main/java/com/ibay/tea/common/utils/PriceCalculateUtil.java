package com.ibay.tea.common.utils;

import com.ibay.tea.entity.TbCoupons;

import java.math.BigDecimal;

public class PriceCalculateUtil {

    public static BigDecimal activityPriceCalculate(BigDecimal price, BigDecimal ratio) {
        BigDecimal activityPrice = price.multiply(ratio);
        return activityPrice ;
    }

    public static double ratioCouponsPriceCalculate(TbCoupons tbCoupons, double orderTotalPrice) {

        BigDecimal realAmount = new BigDecimal(tbCoupons.getCouponsRatio()).multiply(new BigDecimal(orderTotalPrice));
        BigDecimal reduceAmount = new BigDecimal(orderTotalPrice).subtract(realAmount);
        return reduceAmount.doubleValue();
    }

    public static double multy(double price, String couponsRatio) {
        BigDecimal multiply = new BigDecimal(price).multiply(new BigDecimal(couponsRatio));
        return multiply.doubleValue();
    }

    public static double multy(double price, int count) {
        BigDecimal multiply = new BigDecimal(price).multiply(new BigDecimal(count));
        return multiply.doubleValue();
    }

    public static int intOrderTbPrice(BigDecimal payment) {
        return payment.multiply(new BigDecimal(100)).intValue();
    }

    public static double subtract(double totalAmount,double reduceAmount){
       return  new BigDecimal(totalAmount).subtract(new BigDecimal(reduceAmount)).doubleValue();
    }
}
