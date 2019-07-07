package com.example.demo.service;

import com.example.demo.entity.Restaurant;
import com.example.demo.entity.RestaurantRevise;
import com.example.demo.pojo.send.CommonStateMessage;
import com.example.demo.pojo.send.LoginStateMessage;
import com.example.demo.pojo.send.ManagerStatistic;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;

public interface ManagerService {

    LoginStateMessage managerLogin(String username, String passcode);


    CommonStateMessage approveRestaurant(int restaurantId);


    CommonStateMessage disapproveRestaurant(int restaurantId);


    CommonStateMessage approveRevise(int restaurantId);


    CommonStateMessage rejectRevise(int restaurantId);


    Iterable<Restaurant> getRestaurantWaitRegister();


    Iterable<RestaurantRevise> getWaitRevise();

    ManagerStatistic getStatistic();
}
