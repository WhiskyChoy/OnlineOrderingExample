package com.example.demo.controller.restcontroller;

import com.example.demo.entity.*;
import com.example.demo.global.GlobalValue;
import com.example.demo.global.Vocabulary;
import com.example.demo.pojo.receive.*;
import com.example.demo.pojo.send.*;
import com.example.demo.service.CustomerService;
import com.example.demo.service.RestaurantService;
import com.example.demo.util.BankUtil;
import com.example.demo.util.EmailUtil;
import com.example.demo.util.JSONHelper;
import org.apache.commons.mail.EmailException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private static final int waitMinute = 3;
    private static final int authnCodeLength = 4;

    @Resource
    private CustomerService customerService;
    @Resource
    private RestaurantService restaurantService;

    @RequestMapping(value = "/meta", method = RequestMethod.GET)
    public CustLoginMeta custLoginMeta() {
        return new CustLoginMeta(waitMinute, authnCodeLength);
    }

    @RequestMapping(value = "/code", method = RequestMethod.POST)
    public CommonStateMessage customerCode(HttpServletRequest request) {
        CommonStateMessage result = new CommonStateMessage();
        String email = (String) JSONHelper.requestToJson(request).get("email");
        boolean isEmail = EmailUtil.judgeEmail(email);
        if (!isEmail) {
            result.setSuccessful(false);
            result.setInfo("提供的email格式不对");
        } else {
            try {
                String code = EmailUtil.achieveCode(authnCodeLength);
                EmailUtil.sendAuthCodeEmail(email, code, waitMinute);
                HttpSession session = request.getSession(true);
                session.setAttribute(Vocabulary.YUMMY_CUSTOMER_CODE, code);
                session.setMaxInactiveInterval(waitMinute * 60);
                result.setSuccessful(true);
                result.setInfo("登录码已发送，请在" + waitMinute + "分钟内输入");
            } catch (EmailException e) {
                result.setSuccessful(false);
                result.setInfo("发送邮件失败");
                e.printStackTrace();
                return result;
            }
        }
        return result;
    }

    // 传值坚持用post
    @PostMapping("/exist")
    public CustomerExistMessage checkExist(@RequestBody CheckCustomerExistRequest checkCustomerExistRequest) {
        return customerService.checkExist(checkCustomerExistRequest.getEmail());
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginStateMessage loginStateMessage(@RequestBody CustomerLoginRequest customerLoginRequest, HttpServletRequest httpServletRequest) {
        LoginStateMessage loginStateMessage = new LoginStateMessage();
        HttpSession session = httpServletRequest.getSession(true);
        String authnCode = (String) session.getAttribute(Vocabulary.YUMMY_CUSTOMER_CODE);
        System.out.println(authnCode);
        if (!GlobalValue.inTestMode && !customerLoginRequest.getAuthnCode().equals(authnCode)) {
            loginStateMessage.setLoggedIn(false);
            loginStateMessage.setInfo("登录授权码不正确");
            return loginStateMessage;
        }
        String email = customerLoginRequest.getEmail();
        Customer customer = customerService.getCustomer(email);
        if (customer != null) {
            if (customer.isValid()) {
                return getLoginStateMessage(loginStateMessage, session, customer);
            } else {
                loginStateMessage.setInfo("您的账户已被注销，不可恢复");
                loginStateMessage.setLoggedIn(false);
                return loginStateMessage;
            }
        }

        String bankAccountId = customerLoginRequest.getBankAccountId();
        CommonStateMessage commonStateMessage = BankUtil.checkAccountExists(bankAccountId);
        if (!commonStateMessage.isSuccessful()) {
            loginStateMessage.setLoggedIn(false);
            loginStateMessage.setInfo(commonStateMessage.getInfo());
            return loginStateMessage;
        }
        Customer updateCustomer = customerService.updateCustomer(email, customerLoginRequest.getName(), bankAccountId);
        if (updateCustomer == null) {
            loginStateMessage.setLoggedIn(false);
            loginStateMessage.setInfo("数据库访问出错，请重试");
            return loginStateMessage;
        }
        return getLoginStateMessage(loginStateMessage, session, updateCustomer);
    }

    private LoginStateMessage getLoginStateMessage(LoginStateMessage loginStateMessage, HttpSession session, Customer customer) {
        session.setMaxInactiveInterval(GlobalValue.loggedInSessionTimeInMins * 60);
        session.setAttribute(Vocabulary.YUMMY_USER_INFO, customer);
        loginStateMessage.setUserType(UserType.CUSTOMER.toString());
        loginStateMessage.setLoggedIn(true);
        return loginStateMessage;
    }

    @PostMapping("/add/location")
    public CommonStateMessage addLocation(@RequestBody Location location, @SessionAttribute(Vocabulary.YUMMY_USER_INFO) Customer customer) {
        return customerService.addLocation(location, customer);
    }

    @GetMapping("/get/location")
    public Iterable<Location> getLocation(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(true);
        Customer customer = (Customer) session.getAttribute(Vocabulary.YUMMY_USER_INFO);
        return customerService.getLocations(customer);
    }

    @PostMapping("/delete/location")
    public CommonStateMessage deleteLocation(@RequestBody Location location, @SessionAttribute(Vocabulary.YUMMY_USER_INFO) Customer customer) {
        return customerService.deleteLocation(customer, location);
    }

    @PostMapping("/revise")
    public CommonStateMessage revise(@RequestBody CustomerReviseRequest request, @SessionAttribute(Vocabulary.YUMMY_USER_INFO) Customer customer) {
        return customerService.reviseMessage(request, customer.getId());
    }

    @GetMapping("/self/info")
    public Customer getSelfInfo(@SessionAttribute(Vocabulary.YUMMY_USER_INFO) Customer customer) {
        return customerService.getCustomer(customer.getId());
    }

    @PostMapping("/find/restaurant")
    public Iterable<Restaurant> findRestaurantNear(@RequestBody FindRestaurantRequest findRestaurantRequest) {
        return restaurantService.getRestaurantNear(findRestaurantRequest.getLocationId(), findRestaurantRequest.getTimeInMills());
    }

    @PostMapping("/find/food")
    public Iterable<FoodProduct> findFoodProduct(@RequestBody CustomerFindFoodRequest customerFindFoodRequest) {
        return restaurantService.getFoodProduct(customerFindFoodRequest.getRestaurantId());
    }

    @PostMapping("/add/order/wait")
    public CommonStateMessage addOderWait(@RequestBody CustomerOrderRequest customerOrderRequest, @SessionAttribute(Vocabulary.YUMMY_USER_INFO) Customer customer) {
        return customerService.addOrder(customerOrderRequest, customer.getId());
    }

    @PostMapping("/pay/order")
    public CommonStateMessage payOrder(@RequestBody OrderRequest orderRequest, @SessionAttribute(Vocabulary.YUMMY_USER_INFO) Customer customer) {
        return customerService.payOrder(orderRequest.getOrderId(), customer.getId());
    }

    @PostMapping("/cancel/order")
    public CommonStateMessage cancelOrder(@RequestBody OrderRequest orderRequest, @SessionAttribute(Vocabulary.YUMMY_USER_INFO) Customer customer) {
        return customerService.cancelOrder(orderRequest.getOrderId(), customer.getId());
    }

    @PostMapping("/confirm/order")
    public CommonStateMessage confirmOrder(@RequestBody OrderRequest orderRequest, @SessionAttribute(Vocabulary.YUMMY_USER_INFO) Customer customer) {
        return customerService.confirmOrder(orderRequest.getOrderId(), customer.getId());
    }

    @PostMapping("/refund/order")
    public CommonStateMessage refundOrder(@RequestBody OrderRequest orderRequest, @SessionAttribute(Vocabulary.YUMMY_USER_INFO) Customer customer) {
        return customerService.refundOrder(orderRequest.getOrderId(), customer.getId());
    }

    @GetMapping("/all/order")
    public Iterable<FoodOrder> CommonStateMessage(@SessionAttribute(Vocabulary.YUMMY_USER_INFO) Customer customer) {
        return customerService.getFoodOrders(customer);
    }
}
