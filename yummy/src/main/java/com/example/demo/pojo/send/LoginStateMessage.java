package com.example.demo.pojo.send;


import com.example.demo.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class LoginStateMessage {
    private boolean loggedIn = false;
    private String userType = "";
    private String info = "";
    private User currentUser = null;

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    @JsonIgnore
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
