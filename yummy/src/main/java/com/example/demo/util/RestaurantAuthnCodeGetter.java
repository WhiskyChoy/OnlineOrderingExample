package com.example.demo.util;

import java.util.Base64;
import java.util.UUID;

public class RestaurantAuthnCodeGetter {
    //id最大可以到0xFFFFF(1,048,575),而7位base64表示的数据,每位有64=8x8=0100(8进制的100,0打头表示8进制)种可能,所以7位数总共有0100000000000000种可能
    //猜中的概率为(double)0xFFFFFF / (double)0100000000000000,分母过大,可以这么算(仍有较大误差),结果为38.146970382513246/10,000,000
    //可以看到，猜中的概率低至千万分之三十八
    //    double dividend = (double) 0xFFFFFF;
    //    double divisor = (double) 0100;
    //        for (int i = 0; i < 7; i++) {
    //        dividend = dividend / divisor * 10;
    //    }
    //    System.out.println(dividend+"/10,000,000");
    public static String getAuthnCode(int id) throws Exception {
        if (id > 0xFFFFF) {
            throw new Exception("The length of id is too long");
        }
        String num = String.format("%05x", id);
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        byte[] rc4Result = new RC4(key.getBytes()).encrypt(num.getBytes());
        return Base64.getUrlEncoder().encodeToString(rc4Result).replaceAll("=+$", "");
    }
}
