package com.ibay.tea.common.utils;

public class PointToDistance {
  
    public static void main(String[] args) {  
        getDistanceFromTwoPoints(22.53777, 113.94523, 22.611718, 114.040109);
          
        distanceOfTwoPoints(22.53777, 113.94523, 22.611718, 114.040109);
    }  
      
    private static final Double PI = Math.PI;  
  
    private static final Double PK = 180 / PI;  
      

    public static int getDistanceFromTwoPoints(double startLat, double startLng, double endLat, double endLng) {
//        double t1 = Math.cos(startLat / PK) * Math.cos(startLng / PK) * Math.cos(endLat / PK) * Math.cos(endLng / PK);
//        double t2 = Math.cos(startLat / PK) * Math.sin(startLng / PK) * Math.cos(endLat / PK) * Math.sin(endLng / PK);
//        double t3 = Math.sin(startLat / PK) * Math.sin(endLat / PK);
//
//        double tt = Math.acos(t1 + t2 + t3);
//        int distance = (int) (6366000 * tt);
//        System.out.println("两点间的距离：" + distance + " 米");
//        return (int)(6366000 * tt);
        return 0;
    }  
  
      
    /********************************************************************************************************/  
    // 地球半径  
    private static final double EARTH_RADIUS = 6370996.81;  
  
    // 弧度  
    private static double radian(double d) {  
        return d * Math.PI / 180.0;  
    }  
  
    /** 
     * @Description: 第二种方法 
     * @param lat1 
     * @param lng1 
     * @param lat2 
     * @param lng2    
     * @return void 
     * @author 钟志铖 
     * @date 2014-9-7 上午10:11:55 
     */  
    public static void distanceOfTwoPoints(double lat1, double lng1, double lat2, double lng2) {  
        double radLat1 = radian(lat1);  
        double radLat2 = radian(lat2);  
        double a = radLat1 - radLat2;  
        double b = radian(lng1) - radian(lng2);  
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)  
                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));  
        s = s * EARTH_RADIUS;  
        s = Math.round(s * 10000) / 10000;  
        double ss = s * 1.0936132983377;  
        System.out.println("两点间的距离是：" + s + "米" + "," + (int) ss + "码");  
    }  
}  