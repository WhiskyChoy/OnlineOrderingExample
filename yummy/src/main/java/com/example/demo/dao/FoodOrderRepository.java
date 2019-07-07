package com.example.demo.dao;

import com.example.demo.entity.Customer;
import com.example.demo.entity.FoodOrder;
import com.example.demo.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Integer> {
    Iterable<FoodOrder> findByCustomer(Customer customer);

    Iterable<FoodOrder> findByRestaurant(Restaurant restaurant);

    @Query("select f.customer, f.restaurant, sum(f.finalCost) from FoodOrder f where f.month= :month and f.paid = true and f.quickPaidToRestaurant = false group by f.customer, f.restaurant")
    List getTransitionUseMonth(@Param("month") int month);

    @Query("select f.customer, f.restaurant, f.month, sum(f.finalCost) from FoodOrder f where f.paid = true group by f.customer, f.restaurant, f.month")
    List getAllTransition();
}
