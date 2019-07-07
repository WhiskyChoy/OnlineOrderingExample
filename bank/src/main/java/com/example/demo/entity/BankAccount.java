package com.example.demo.entity;

import javax.persistence.*;

@Entity
public class BankAccount {
    @Id
    private String id;

    @Column
    private double balance = 0.0;

    public BankAccount(String id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public BankAccount() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
