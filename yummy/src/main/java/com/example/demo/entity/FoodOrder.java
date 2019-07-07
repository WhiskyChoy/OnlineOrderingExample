package com.example.demo.entity;

import com.example.demo.global.GlobalValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@Entity
//order是mysql中的关键字？？？
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    private Customer customer;

    @OneToOne(fetch = FetchType.EAGER)
    private Restaurant restaurant;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Location from;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Location to;

    @OneToMany(mappedBy = "foodOrder", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems;

    @Column
    private double finalCost;

    @Column
    private boolean paid = false;

    @Column
    private boolean userCanceled = false;

    @Column
    private boolean userConfirmed = false;

    @Column
    private boolean refunded = false;

    @Column
    private int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

    @Column
    private boolean quickPaidToRestaurant;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//格式：年-月-日
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")//东八区
    @Column
    private Date orderDate = new Date();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public double getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(double finalCost) {
        this.finalCost = finalCost;
    }

    public boolean isRefunded() {
        return refunded;
    }

    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isUserCanceled() {
        return userCanceled;
    }

    @JsonProperty
    public boolean isSystemCanceled() {
        return !userCanceled && (!paid && (System.currentTimeMillis() - orderDate.getTime() > GlobalValue.cancelInMins * 60 * 1000));
    }

    @JsonProperty
    public boolean isActualCanceled() {
        return userCanceled || (!paid && (System.currentTimeMillis() - orderDate.getTime() > GlobalValue.cancelInMins * 60 * 1000));
    }

    public void setUserCanceled(boolean userCanceled) {
        this.userCanceled = userCanceled;
    }

    public boolean isUserConfirmed() {
        return userConfirmed;
    }

    public void setUserConfirmed(boolean userConfirmed) {
        this.userConfirmed = userConfirmed;
    }

    @JsonProperty
    public boolean isSystemConfirmed() {
        return !userConfirmed && (paid && !refunded && (System.currentTimeMillis() - orderDate.getTime() > GlobalValue.confirmInMins * 60 * 1000));
    }

    @JsonProperty
    public boolean isActualConfirmed() {
        return userConfirmed || (paid && !refunded && (System.currentTimeMillis() - orderDate.getTime() > GlobalValue.confirmInMins * 60 * 1000));
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public boolean isQuickPaidToRestaurant() {
        return quickPaidToRestaurant;
    }

    public void setQuickPaidToRestaurant(boolean quickPaidToRestaurant) {
        this.quickPaidToRestaurant = quickPaidToRestaurant;
    }
}