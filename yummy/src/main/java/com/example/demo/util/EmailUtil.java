package com.example.demo.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class EmailUtil {
    // 随机验证码
    public static String achieveCode(int length) {  //由于数字1 和0 和字母 O,l 有时分不清，所有，没有字母1 、0
        String[] beforeShuffle = new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a",
                "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z"};
        List list = Arrays.asList(beforeShuffle);//将数组转换为集合
        Collections.shuffle(list);  //打乱集合顺序
        StringBuilder sb = new StringBuilder();
        for (Object o : list) {
            sb.append(o); //将集合转化为字符串
        }
        return sb.toString().substring(0, length);  //截取字符串第4到8
    }

    public static void sendAuthCodeEmail(String email, String authCode, int duration) throws EmailException {
        SimpleEmail mail = preHandleEmail(email);
        mail.setSubject("Yummy验证码");//设置邮件的主题
        mail.setMsg("尊敬的用户:您好!\n Yummy登陆验证码为:" + authCode + "\n" + "     (有效期为" + duration + "分钟)");//设置邮件的内容
        mail.send();//发送
    }

    public static void sendRestaurantCodeEmail(String email, String authCode) throws EmailException {
        SimpleEmail mail = preHandleEmail(email);
        mail.setSubject("Yummy餐厅登录码");//设置邮件的主题
        mail.setMsg("尊敬的用户:您好!\n 恭喜您，审批已通过！您的Yummy餐厅登录码为:" + authCode + "\n" + "     请妥善保管)");//设置邮件的内容
        mail.send();//发送
    }

    public static void sendRestaurantFailEmail(String email) throws EmailException {
        SimpleEmail mail = preHandleEmail(email);
        mail.setSubject("Yummy餐厅登录码");//设置邮件的主题
        mail.setMsg("尊敬的用户:您好!\n 您的餐厅注册审批不通过，请您重新发送注册信息");//设置邮件的内容
        mail.send();//发送
    }

    public static void sendRestaurantChange(String oldEmail, String newEmail) throws EmailException {
        SimpleEmail oldOne = preHandleEmail(oldEmail);
        oldOne.setSubject("通知用邮箱已修改");
        oldOne.setMsg("尊敬的用户:您好!\n 您的餐厅通知用邮箱已修改为" + newEmail + "，如非本人操作，请联系Yummy");
        oldOne.send();
        SimpleEmail newOne = preHandleEmail(newEmail);
        newOne.setSubject("通知用邮箱修改成功");
        newOne.setMsg("尊敬的用户:您好!\n 您的餐厅通知用邮箱已修改成功，今后的相关信息将发送到此邮箱");
        newOne.send();
    }

    public static void sendRestaurantReviseReject(String email) throws EmailException {
        SimpleEmail mail = preHandleEmail(email);
        mail.setSubject("Yummy餐厅登录码");//设置邮件的主题
        mail.setMsg("尊敬的用户:您好!\n 您的餐厅信息修改审批不通过，请您重新发送注册信息");//设置邮件的内容
        mail.send();//发送
    }

    private static SimpleEmail preHandleEmail(String email) throws EmailException {
        SimpleEmail mail = new SimpleEmail();
        mail.setHostName("smtp.163.com");//发送邮件的服务器
        mail.setAuthentication("15914746024@163.com", "cwl520748");//登录邮箱的密码，是开启SMTP的密码
        mail.setFrom("15914746024@163.com", "yummy服务器");  //发送邮件的邮箱和发件人
        mail.setSSLOnConnect(true); //使用安全链接
        mail.addTo(email);//接收的邮箱
        return mail;
    }

    public static boolean judgeEmail(String email) {
        String regex = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$";
        return email.matches(regex);
    }
}

