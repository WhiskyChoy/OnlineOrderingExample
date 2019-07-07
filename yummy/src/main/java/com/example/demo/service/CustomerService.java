package com.example.demo.service;

import com.example.demo.entity.Customer;
import com.example.demo.entity.FoodOrder;
import com.example.demo.entity.Location;
import com.example.demo.pojo.receive.CustomerOrderRequest;
import com.example.demo.pojo.receive.CustomerReviseRequest;
import com.example.demo.pojo.send.CommonStateMessage;
import com.example.demo.pojo.send.CustomerExistMessage;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

public interface CustomerService {
    Customer updateCustomer(String email, String name, String bankAccountId);
    CustomerExistMessage checkExist(String email);
    Customer getCustomer(String email);
    CommonStateMessage addLocation(Location location, Customer customer);

    Iterable<Location> getLocations(Customer customer);

    CommonStateMessage deleteLocation(Customer customer, Location location);

    CommonStateMessage addOrder(CustomerOrderRequest customerOrderRequest, int customerId);

    CommonStateMessage reviseMessage(CustomerReviseRequest customerReviseRequest, int customerId);

    Customer getCustomer(int customerId);

    CommonStateMessage payOrder(int orderId, int userId);

    CommonStateMessage cancelOrder(int orderId, int userId);

    CommonStateMessage confirmOrder(int orderId, int userId);

    CommonStateMessage refundOrder(int orderId, int userId);

    Iterable<FoodOrder> getFoodOrders(Customer customer);
}
