package com.example.demo.pojo.receive;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AddProductRequest {
    private String name;
    private String type;
    private String description;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")//东八区
    private Date startDate;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")//东八区
    private Date endDate;
    private double price;
    private double rate = 1.0;
    private int amount;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
