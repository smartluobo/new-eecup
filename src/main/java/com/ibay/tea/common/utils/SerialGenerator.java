package com.ibay.tea.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SerialGenerator {

    private static AtomicInteger payIndex = new AtomicInteger(50);
    private static AtomicInteger orderIndex = new AtomicInteger(80);

    private static Random random = new Random();

    public static String getOrderSerial(){
        int orderSerial = orderIndex.getAndIncrement();
        String takeCode = StringUtils.leftPad(String.valueOf(orderSerial),4,"0");
        String dateYyyyMMddHHmm = DateUtil.getDateYyyyMMddHHmm();
        return dateYyyyMMddHHmm + takeCode;
    }

    public static String getPayRecordSerial(){
        int paySerial = payIndex.getAndIncrement();
        String dateYyyyMMddHHmmss = DateUtil.getDateYyyyMMddHHmmss();
        return dateYyyyMMddHHmmss + paySerial;
    }

    public static String getVerificationCode() {
        int prefix = random.nextInt(7) + 1;
        int value = random.nextInt(100000)+528;
        int code = prefix * 100000 + value;
        String verificationCode = String.valueOf(code);
        return verificationCode;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            getUniqueCode();
        }
    }

    public static String getUniqueCode() {
        int i = random.nextInt(10);
        int j = random.nextInt(10);
        int k = random.nextInt(10);
        int l = random.nextInt(10);
        String code =  ""+i+j+k+l;
        System.out.println(code);
        return code;
    }
}
