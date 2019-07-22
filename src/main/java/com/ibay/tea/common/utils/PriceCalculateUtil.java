package com.ibay.tea.common.utils;

import com.ibay.tea.entity.TbCoupons;

import java.math.BigDecimal;

public class PriceCalculateUtil {

    public static double ratioCouponsPriceCalculate(TbCoupons tbCoupons, double orderTotalPrice) {

        BigDecimal realAmount = new BigDecimal(tbCoupons.getCouponsRatio()).multiply(new BigDecimal(String.valueOf(orderTotalPrice)));
        BigDecimal reduceAmount = new BigDecimal(String.valueOf(orderTotalPrice)).subtract(realAmount);
        return reduceAmount.doubleValue();
    }

    public static double multiply(double price, String couponsRatio) {
        BigDecimal multiply = new BigDecimal(String.valueOf(price)).multiply(new BigDecimal(couponsRatio));
        return multiply.doubleValue();
    }

    public static double multiply(double price, int count) {
        BigDecimal multiply = new BigDecimal(String.valueOf(price)).multiply(new BigDecimal(count));
        return multiply.doubleValue();
    }

    public static int intOrderTbPrice(BigDecimal payment) {

        return payment.multiply(new BigDecimal("100")).intValue();
    }

    public static double subtract(double totalAmount,double reduceAmount){
       return  new BigDecimal(String.valueOf(totalAmount)).subtract(new BigDecimal(String.valueOf(reduceAmount))).doubleValue();
    }
}
