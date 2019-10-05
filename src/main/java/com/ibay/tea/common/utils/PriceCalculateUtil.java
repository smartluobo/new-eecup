package com.ibay.tea.common.utils;

import com.ibay.tea.entity.TbCoupons;
import com.ibay.tea.entity.TbUserCoupons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class PriceCalculateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceCalculateUtil.class);

    public static double ratioCouponsPriceCalculate(TbUserCoupons userCoupons, double orderTotalPrice) {

        BigDecimal realAmount = new BigDecimal(userCoupons.getCouponsRatio()).multiply(new BigDecimal(String.valueOf(orderTotalPrice)));
        BigDecimal reduceAmount = new BigDecimal(String.valueOf(orderTotalPrice)).subtract(realAmount);
        LOGGER.info("ratioCouponsPriceCalculate coupons ratio : {} ,orderTotalPrice : {}",userCoupons.getCouponsRatio(),orderTotalPrice);
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

    public static double subtract(String totalAmount,String reduceAmount){
        return  new BigDecimal(String.valueOf(totalAmount)).subtract(new BigDecimal(String.valueOf(reduceAmount))).doubleValue();
    }

    public static double add(double specialReduceAmount, double cartPrice) {
        return new BigDecimal(String.valueOf(specialReduceAmount)).add(new BigDecimal(String.valueOf(cartPrice))).doubleValue();
    }

    public static double add(String amount1, String amount2) {
        return new BigDecimal(amount1).add(new BigDecimal(amount2)).doubleValue();
    }
}
