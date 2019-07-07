package com.example.demo.entity;

public enum UserType {
    CUSTOMER(Customer.class.getSimpleName()), MANAGER(Manager.class.getSimpleName()), RESTAURANT(Restaurant.class.getSimpleName());

    private String value;

    UserType(String name) {
        this.value = name;
    }

    @Override
    public String toString() {
        return value;
    }

    public static UserType getUserType(String string) {
        for (UserType userType : values()) {
            if (userType.toString().equals(string)) {
                return userType;
            }
        }
        return null;
    }

}
