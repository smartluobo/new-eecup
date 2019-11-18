package com.ibay.tea.common.utils;

import com.ibay.tea.entity.TbStore;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SerialGenerator {

    private static AtomicInteger payIndex = new AtomicInteger(50);
    private static AtomicInteger orderIndex = new AtomicInteger(80);

    private static final char[] CHARS = {'a','b','c','d','e','f','g','h','i','j','k','m','n','p','q','r','s','t','u','v','w','x','y','z','2','3','4','5','6','7','8','9'};

    private static final char[] NUMS = {'1','2','3','4','5','6','7','8','9','0'};

    private static Random random = new Random();

    public static String getOrderSerial(TbStore store){
        int orderSerial = orderIndex.getAndIncrement();
        String takeCode = StringUtils.leftPad(String.valueOf(orderSerial),4,"0");
        String dateYyyyMMddHHmm = DateUtil.getDateYyyyMMddHHmm();
        if (store == null){
            return dateYyyyMMddHHmm + takeCode;
        }else {
            return store.getStoreFlag() + dateYyyyMMddHHmm + takeCode;
        }
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
        return code;
    }

    public static String getShoppingCardCode(int length){
        int range = CHARS.length;
        if (length <=0){
            return null;
        }
        char[] codeChars = new char[length];
        for (int i = 0; i < length; i++) {
            codeChars[i] = CHARS[random.nextInt(range)];
        }
        return new String(codeChars);
    }

    public static String getCashCouponsCode(int length) {
        int range = NUMS.length;
        if (length <=0){
            return null;
        }
        char[] codeChars = new char[length];
        for (int i = 0; i < length; i++) {
            codeChars[i] = NUMS[random.nextInt(range)];
        }
        return new String(codeChars);
    }
}
