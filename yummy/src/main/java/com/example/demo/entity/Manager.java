package com.example.demo.entity;

import javax.persistence.*;

@Entity
public class Manager extends User {

    @Column
    private String username;

    @Column
    private String passcode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }
}