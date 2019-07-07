package com.example.demo.util;

import java.math.RoundingMode;
import java.text.NumberFormat;

public class NumberUtil {
    public static double doubleFormat(double number) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        // 保留两位小数
        numberFormat.setMaximumFractionDigits(2);
        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        numberFormat.setGroupingUsed(false);
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
        return Double.valueOf(numberFormat.format(number));
    }

//    public static void main(String[] args){
//        System.out.println(doubleFormat(1.5567));
//    }
}
