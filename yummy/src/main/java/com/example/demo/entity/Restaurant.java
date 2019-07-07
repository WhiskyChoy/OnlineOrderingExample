package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Restaurant extends User {
    @OneToOne(mappedBy = "restaurant", cascade = {CascadeType.ALL})
    private AuthnCode authnCode;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Location location;

    @Column
    private String name;

    @Column
    private String email;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "restaurant")
    private Set<FoodProduct> foodProducts;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//格式：年-月-日
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")//东八区
    @Column
    private Date registerDate = new Date();

    // 这个不是column
    @Transient
    private double distance;

    // 这个不是column
    @Transient
    private int estimateMin;

    @JsonProperty
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @JsonIgnore
    public AuthnCode getAuthnCode() {
        return authnCode;
    }

    public void setAuthnCode(AuthnCode authnCode) {
        this.authnCode = authnCode;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    @JsonIgnore
    public Set<FoodProduct> getFoodProducts() {
        return foodProducts;
    }

    public void setFoodProducts(Set<FoodProduct> foodProducts) {
        this.foodProducts = foodProducts;
    }

    @JsonIgnore
    public boolean checkReviseValid(RestaurantRevise restaurantRevise) {
        return !restaurantRevise.getEmail().equals(email) || !restaurantRevise.getName().equals(name)
                || !restaurantRevise.getLocation().equals(location) || restaurantRevise.getBankAccountId().equals(getBankAccountId());
    }

    @JsonProperty
    public int getEstimateMin() {
        return estimateMin;
    }

    public void setEstimateMin(int estimateMin) {
        this.estimateMin = estimateMin;
    }
}