package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Customer extends User {

    @Column
    private String name = "匿名";

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Location> locations;

    @Column
    private double consumption = 0;

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

    @JsonProperty
    public int getLevel() {
        return Math.max(((int) consumption / 1000) + 1, 5);
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public double getConsumption() {
        return consumption;
    }

    public void setConsumption(double consumption) {
        this.consumption = consumption;
    }
}