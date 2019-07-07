package com.example.demo.util;

public class LocationUtil {
    private static final double EARTH_RADIUS = 6378137;

    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat_a x1
     * @param lng_a y1
     * @param lat_b x2
     * @param lng_b y2
     * @return 距离
     */
    public static double getDistance(double lat_a, double lng_a, double lat_b, double lng_b){
        double pk = 180 / Math.PI;
        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;
        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);
        return Math.abs(EARTH_RADIUS * tt);
    }
}