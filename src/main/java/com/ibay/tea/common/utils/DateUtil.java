package com.ibay.tea.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static final SimpleDateFormat yyyyMMddSdf = new SimpleDateFormat("yyyyMMdd");

    private static final SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");

    private static final  SimpleDateFormat viewDateFormatSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final  SimpleDateFormat expireDateFormatSdf = new SimpleDateFormat("yyyy-MM-dd");

    public static String getDateYyyyMMdd(){
        return yyyyMMddSdf.format(new Date());
    }

    public static String getDateYyyyMMdd(Date date){
        return yyyyMMddSdf.format(date);
    }

    public static int getHour(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getHour(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static String getDateYyyyMMddHHmmss(){
        return yyyyMMddHHmmss.format(new Date());
    }

    public static String getDateYyyyMMddHHmmss(Date date){
        return yyyyMMddHHmmss.format(date);
    }

    public static String viewDateFormat(Date createTime) {
        return viewDateFormatSdf.format(createTime);
    }

    public static Date getExpireDate(Integer receiveDate, int userCouponsExpireLimit) {
        try {
            Date receive = yyyyMMddSdf.parse(String.valueOf(receiveDate));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(receive);
            calendar.add(Calendar.DAY_OF_MONTH,userCouponsExpireLimit);
            Date expireDate = calendar.getTime();
            return expireDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
       // getExpireDate(20190626,2);
    }

    public static String formatExpireDate(Date expireDate) {
        return expireDateFormatSdf.format(expireDate);
    }
}
