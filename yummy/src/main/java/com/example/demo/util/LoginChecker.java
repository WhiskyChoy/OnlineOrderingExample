package com.example.demo.util;

import com.example.demo.entity.User;
import com.example.demo.entity.UserType;
import com.example.demo.global.Vocabulary;

import javax.servlet.http.HttpSession;

public class LoginChecker {
    public static boolean checkLogin(HttpSession session) {
        return !(session == null || session.getAttribute(Vocabulary.YUMMY_USER_INFO) == null);
    }

    public static UserType getUserType(HttpSession session) {
        if (LoginChecker.checkLogin(session)) {
            User user = (User) session.getAttribute(Vocabulary.YUMMY_USER_INFO);
            return UserType.getUserType(user.getClass().getSimpleName());
        } else {
            return null;
        }
    }
}
