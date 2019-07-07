package com.example.demo.dao;

import com.example.demo.entity.RestaurantRevise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantReviseRepository extends JpaRepository<RestaurantRevise, Integer> {
    Iterable<RestaurantRevise> findByApproved(boolean approved);
}
