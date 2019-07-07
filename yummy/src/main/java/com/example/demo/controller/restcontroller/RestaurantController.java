package com.example.demo.controller.restcontroller;

import com.example.demo.entity.FoodOrder;
import com.example.demo.entity.Restaurant;
import com.example.demo.entity.RestaurantRevise;
import com.example.demo.global.GlobalValue;
import com.example.demo.global.Vocabulary;
import com.example.demo.pojo.receive.AddProductRequest;
import com.example.demo.pojo.receive.RestaurantLoginRequest;
import com.example.demo.pojo.receive.RestaurantRegisterRequest;
import com.example.demo.pojo.send.LoginStateMessage;
import com.example.demo.pojo.send.CommonStateMessage;
import com.example.demo.service.RestaurantService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {
    @Resource
    private RestaurantService restaurantService;

    @PostMapping("/register")
    public CommonStateMessage registerStateMessage(@RequestBody RestaurantRegisterRequest registerRequest) {
        return restaurantService.registerStateMessage(registerRequest);
    }

    @PostMapping("/login")
    public LoginStateMessage loginStateMessage(@RequestBody RestaurantLoginRequest loginRequest,
                                               HttpServletRequest httpServletRequest) {
        LoginStateMessage loginStateMessage = restaurantService.loginStateMessage(loginRequest.getAuthnCode());
        HttpSession session = httpServletRequest.getSession(true);
        if (loginStateMessage.isLoggedIn()) {
            session.setMaxInactiveInterval(GlobalValue.loggedInSessionTimeInMins * 60);
            session.setAttribute(Vocabulary.YUMMY_USER_INFO, loginStateMessage.getCurrentUser());
        }
        return loginStateMessage;
    }

    @PostMapping("/add/product")
    public CommonStateMessage addProductMessage(@RequestBody AddProductRequest addProductRequest,
                                                HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(true);
        Restaurant restaurant = (Restaurant) session.getAttribute(Vocabulary.YUMMY_USER_INFO);
        return restaurantService.addProductMessage(addProductRequest, restaurant);
    }

    @PostMapping("/revise")
    public CommonStateMessage revise(@RequestBody RestaurantRevise restaurantRevise,
                                     @SessionAttribute(Vocabulary.YUMMY_USER_INFO) Restaurant restaurant) {
        return restaurantService.revise(restaurantRevise, restaurant.getId());
    }

    @GetMapping("/self/info")
    public Restaurant getSelfInfo(@SessionAttribute(Vocabulary.YUMMY_USER_INFO) Restaurant restaurant) {
        return restaurantService.getRestaurant(restaurant.getId());
    }

    @GetMapping("/order")
    public Iterable<FoodOrder> getRest(@SessionAttribute(Vocabulary.YUMMY_USER_INFO) Restaurant restaurant) {
        return restaurantService.getFoodOrder(restaurant);
    }
}
