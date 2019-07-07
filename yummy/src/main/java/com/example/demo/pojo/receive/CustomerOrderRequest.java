package com.example.demo.pojo.receive;

public class CustomerOrderRequest {
    private Iterable<WebOrderItem> orderItems;
    private int customerLocationId;
    private int restaurantId;
    //TODO 其实业务上应该查所有的orderItem是不是来自同一家餐厅

    public Iterable<WebOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Iterable<WebOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public int getCustomerLocationId() {
        return customerLocationId;
    }

    public void setCustomerLocationId(int customerLocationId) {
        this.customerLocationId = customerLocationId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }
}
