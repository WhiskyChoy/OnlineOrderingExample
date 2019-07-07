package com.example.demo.dao;

import com.example.demo.entity.FoodProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodProductRepository extends JpaRepository<FoodProduct, Integer> {

}
