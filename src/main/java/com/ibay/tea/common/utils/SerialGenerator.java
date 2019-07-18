package com.ibay.tea.common.utils;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SerialGenerator {

    private static AtomicInteger orderIndex = new AtomicInteger(50);

    private static Random random = new Random();

    public static String getOrderSerial(){
        int orderSerial = orderIndex.getAndIncrement();
        String dateYyyyMMddHHmmss = DateUtil.getDateYyyyMMddHHmmss();
        return dateYyyyMMddHHmmss + orderSerial;
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
            getVerificationCode();
        }
    }
}
