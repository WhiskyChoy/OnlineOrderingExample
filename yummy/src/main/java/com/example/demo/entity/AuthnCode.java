package com.example.demo.entity;

import javax.persistence.*;

@Entity
public class AuthnCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    private String actualCode;

    @OneToOne()
    private Restaurant restaurant;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActualCode() {
        return actualCode;
    }

    public void setActualCode(String actualCode) {
        this.actualCode = actualCode;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}