package com.example.demo.controller.restcontroller;

import com.example.demo.entity.UserType;
import com.example.demo.pojo.send.LoginStateMessage;
import com.example.demo.util.LoginChecker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @RequestMapping(value = "/checkLogin", method = RequestMethod.GET)
    public LoginStateMessage isLogin(HttpServletRequest request) {
        LoginStateMessage message = new LoginStateMessage();
        HttpSession session = request.getSession(false);
        message.setLoggedIn(LoginChecker.checkLogin(session));
        UserType userType = LoginChecker.getUserType(session);
        message.setUserType(userType == null ? "" : userType.toString());
        return message;
    }

    @GetMapping("/logout")
    public LoginStateMessage logout(HttpServletRequest request) {
        LoginStateMessage message = new LoginStateMessage();
        HttpSession session = request.getSession(false);
        if (session != null)
            session.invalidate();
        message.setLoggedIn(false);
        return message;
    }
}
