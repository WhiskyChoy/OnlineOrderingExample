package com.example.demo.dao;

import com.example.demo.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    Iterable<Restaurant> getRestaurantsByValid(boolean valid);
}
