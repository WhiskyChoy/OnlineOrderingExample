package com.example.demo;

import com.example.demo.dao.CustomerRepository;
import com.example.demo.dao.FoodOrderRepository;
import com.example.demo.dao.ManagerRepository;
import com.example.demo.dao.RestaurantRepository;
import com.example.demo.pojo.send.CommonStateMessage;
import com.example.demo.service.ManagerService;
import com.example.demo.service.RestaurantService;
import com.example.demo.util.EmailUtil;
import com.example.demo.util.HttpRequest;
import com.example.demo.util.LocationUtil;
import org.apache.commons.mail.EmailException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    @Resource
    private CustomerRepository customerRepository;
    @Resource
    private ManagerService managerService;
    @Resource
    private RestaurantRepository restaurantRepository;
    @Resource
    private FoodOrderRepository foodOrderRepository;


    @Test
    public void contextLoads() {
    }

    //    @Test
//    public void sendEmail() {
//        try {
//            EmailUtil.sendAuthCodeEmail("1261249659@qq.com", EmailUtil.achieveCode(4), 1);
//        } catch (EmailException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void doPost() {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("email", "1261249659@qq.com");
//            JSONObject jsonObject1 = HttpRequest.post("http://127.0.0.1:8080/api/customer/code", jsonObject);
//            System.out.println(jsonObject1);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//    @Test
//    public void doFindCustomer(){
//        System.out.println(customerRepository.findCustomerByEmail("1261249659@qq.com"));
//        System.out.println(customerRepository.findCustomerByEmail("1261249659@qq.com").getEmail());
//    }
//    @Test
//    public void approveRest() {
//        CommonStateMessage commonStateMessage = managerService.approveRestaurant(2);
//        System.out.println(commonStateMessage.getInfo());
//    }
//    @Test
//    public void approveRest() {
//        CommonStateMessage commonStateMessage = managerService.disapproveRestaurant(5);
//        System.out.println(commonStateMessage.getInfo());
//    }
//
//    @Test
//    public void testDistance(){
//        System.out.println(LocationUtil.getDistance(133, 125, 1, 3));
//
//    }
}

