package com.example.demo.controller.restcontroller;

import com.example.demo.entity.Manager;
import com.example.demo.entity.Restaurant;
import com.example.demo.entity.RestaurantRevise;
import com.example.demo.global.GlobalValue;
import com.example.demo.global.Vocabulary;
import com.example.demo.pojo.receive.CreditRestaurantMessage;
import com.example.demo.pojo.send.CommonStateMessage;
import com.example.demo.pojo.send.LoginStateMessage;
import com.example.demo.pojo.send.ManagerStatistic;
import com.example.demo.service.ManagerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {
    @Resource
    private ManagerService managerService;

    @PostMapping("/login")
    public LoginStateMessage managerLogin(@RequestBody Manager manager, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(true);
        LoginStateMessage loginStateMessage = managerService.managerLogin(manager.getUsername(), manager.getPasscode());
        if (loginStateMessage.isLoggedIn()) {
            session.setMaxInactiveInterval(GlobalValue.loggedInSessionTimeInMins * 60);
            session.setAttribute(Vocabulary.YUMMY_USER_INFO, loginStateMessage.getCurrentUser());
        }
        return loginStateMessage;
    }

    @PostMapping("/approve")
    public CommonStateMessage approveRestaurant(@RequestBody CreditRestaurantMessage message) {
        return managerService.approveRestaurant(message.getRestaurantId());
    }

    @PostMapping("/reject")
    public CommonStateMessage rejectRestaurant(@RequestBody CreditRestaurantMessage message) {
        return managerService.disapproveRestaurant(message.getRestaurantId());
    }

    @GetMapping("/wait/register")
    public Iterable<Restaurant> getRestaurantWaitRegister() {
        return managerService.getRestaurantWaitRegister();
    }

    @GetMapping("/wait/revise")
    public Iterable<RestaurantRevise> getWaitRevise() {
        return managerService.getWaitRevise();
    }

    @PostMapping("/approve/revise")
    public CommonStateMessage approveRevise(@RequestBody CreditRestaurantMessage message) {
        System.out.println(message);
        System.out.println(message.getRestaurantId());
        return managerService.approveRevise(message.getRestaurantId());
    }

    @PostMapping("/reject/revise")
    public CommonStateMessage rejectRevise(@RequestBody CreditRestaurantMessage message) {
        return managerService.rejectRevise(message.getRestaurantId());
    }

    @GetMapping("/statistic")
    public ManagerStatistic getStatistic() {
        return managerService.getStatistic();
    }
}
