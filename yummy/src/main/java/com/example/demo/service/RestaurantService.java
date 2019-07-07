package com.example.demo.service;

import com.example.demo.entity.FoodOrder;
import com.example.demo.entity.FoodProduct;
import com.example.demo.entity.Restaurant;
import com.example.demo.entity.RestaurantRevise;
import com.example.demo.pojo.receive.AddProductRequest;
import com.example.demo.pojo.receive.RestaurantRegisterRequest;
import com.example.demo.pojo.send.LoginStateMessage;
import com.example.demo.pojo.send.CommonStateMessage;

public interface RestaurantService {
    CommonStateMessage registerStateMessage(RestaurantRegisterRequest registerRequest);
    LoginStateMessage loginStateMessage(String authnCode);
    CommonStateMessage addProductMessage(AddProductRequest addProductRequest, Restaurant restaurant);

    Iterable<Restaurant> getRestaurantNear(int locationId, double timeInMills);

    Iterable<FoodProduct> getFoodProduct(int restaurantId);

    CommonStateMessage revise(RestaurantRevise restaurantRevise, Integer restaurantId);

    Restaurant getRestaurant(int restaurantId);

    Iterable<FoodOrder> getFoodOrder(Restaurant restaurant);
}
