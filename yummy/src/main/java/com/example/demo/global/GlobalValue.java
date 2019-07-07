package com.example.demo.global;

public class GlobalValue {
    public static final int loggedInSessionTimeInMins = 30;
    // 单位是千米每小时， 1000米/60 * 60 * 1000毫秒 = 1/3600 米每毫秒
    public static final double speedKMPerHour = 40;
    public static final boolean inTestMode = false;
    public static int cancelInMins = 2;
    public static int confirmInMins = 2;
    public static String bankServerUrlPrefix = "http://127.0.0.1:9090";
    public static String systemBankAccount = "6220152685452632021";
    public static boolean useMonthlyBatch = false;
}
