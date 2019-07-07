package com.example.demo.pojo.receive;

public class FindRestaurantRequest {
    private int locationId;
    private double timeInMills;

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public double getTimeInMills() {
        return timeInMills;
    }

    public void setTimeInMills(double timeInMills) {
        this.timeInMills = timeInMills;
    }
}
