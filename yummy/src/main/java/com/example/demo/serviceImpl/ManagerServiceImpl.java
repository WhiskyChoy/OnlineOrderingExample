package com.example.demo.serviceImpl;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.global.GlobalValue;
import com.example.demo.pojo.send.CommonStateMessage;
import com.example.demo.pojo.send.LoginStateMessage;
import com.example.demo.pojo.send.ManagerStatistic;
import com.example.demo.service.ManagerService;
import com.example.demo.util.BankUtil;
import com.example.demo.util.EmailUtil;
import com.example.demo.util.RestaurantAuthnCodeGetter;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Calendar;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Resource
    private ManagerRepository managerRepository;
    @Resource
    private AuthnCodeRepository authnCodeRepository;
    @Resource
    private RestaurantRepository restaurantRepository;
    @Resource
    private RestaurantReviseRepository restaurantReviseRepository;
    @Resource
    private CustomerRepository customerRepository;
    @Resource
    private FoodOrderRepository foodOrderRepository;

    @Override
    public LoginStateMessage managerLogin(String username, String passcode) {
        LoginStateMessage loginStateMessage = new LoginStateMessage();
        try {
            Manager manager = managerRepository.findManagerByUsername(username);
            if (manager == null) {
                loginStateMessage.setLoggedIn(false);
                loginStateMessage.setInfo("用户名不存在");
                return loginStateMessage;
            }

            if (!manager.getPasscode().equals(passcode)) {
                loginStateMessage.setLoggedIn(false);
                loginStateMessage.setInfo("密码不正确");
                return loginStateMessage;
            }

            loginStateMessage.setCurrentUser(manager);
            loginStateMessage.setLoggedIn(true);
            loginStateMessage.setInfo("登录成功");
            loginStateMessage.setUserType(UserType.MANAGER.toString());
            return loginStateMessage;
        } catch (Exception e) {
            loginStateMessage.setLoggedIn(false);
            loginStateMessage.setInfo("数据库出错");
            return loginStateMessage;
        }

    }

    @Transactional
    @Override
    public CommonStateMessage approveRestaurant(int restaurantId) {
        CommonStateMessage commonStateMessage = new CommonStateMessage();
        try {
            Restaurant restaurant = restaurantRepository.getOne(restaurantId);
            if (restaurant == null) {
                throw new Exception("该餐厅不存在");
            }
            AuthnCode authnCode;
            authnCode = restaurant.getAuthnCode();
            if (authnCode == null) {
                authnCode = new AuthnCode();
                authnCode.setRestaurant(restaurant);
                authnCode = authnCodeRepository.save(authnCode);
                if (authnCode == null) {
                    throw new Exception("识别码预写入失败");
                }
                String actualCode = RestaurantAuthnCodeGetter.getAuthnCode(authnCode.getId());
                authnCode.setActualCode(actualCode);
                authnCode = authnCodeRepository.saveAndFlush(authnCode);
                if (authnCode == null) {
                    throw new Exception("识别码实际写入失败");
                }
            }

            try {
                EmailUtil.sendRestaurantCodeEmail(restaurant.getEmail(), authnCode.getActualCode());
            } catch (EmailException e) {
                throw new EmailException("发送邮件失败");
            }

            restaurant.setValid(true);
            restaurant = restaurantRepository.saveAndFlush(restaurant);
            if (restaurant == null) {
                throw new Exception("审批通过失败");
            }

            commonStateMessage.setSuccessful(true);
            commonStateMessage.setInfo("审批通过成功");
            return commonStateMessage;
        } catch (Exception e) {
            commonStateMessage.setSuccessful(false);
            commonStateMessage.setInfo(e.getMessage());
            e.printStackTrace();
            return commonStateMessage;
        }
    }

    @Transactional
    @Override
    public CommonStateMessage disapproveRestaurant(int restaurantId) {
        CommonStateMessage commonStateMessage = new CommonStateMessage();
        try {
            Restaurant restaurant = restaurantRepository.getOne(restaurantId);
            if (restaurant == null) {
                throw new Exception("该餐厅不存在");
            }
            if (restaurant.isValid()) {
                throw new Exception("对已通过的餐厅不能重复审批");
            }
            EmailUtil.sendRestaurantFailEmail(restaurant.getEmail());
            restaurantRepository.delete(restaurant);
            commonStateMessage.setSuccessful(true);
            commonStateMessage.setInfo("审批拒绝成功");
            return commonStateMessage;
        } catch (Exception e) {
            commonStateMessage.setSuccessful(false);
            commonStateMessage.setInfo(e.getMessage());
            e.printStackTrace();
            return commonStateMessage;
        }
    }

    @Transactional
    @Override
    public CommonStateMessage approveRevise(int restaurantId) {
        CommonStateMessage commonStateMessage = new CommonStateMessage();
        try {
            Restaurant restaurant = restaurantRepository.getOne(restaurantId);
            RestaurantRevise restaurantRevise = restaurantReviseRepository.getOne(restaurantId);
            String oldEmail = restaurant.getEmail();
            restaurant.setEmail(restaurantRevise.getEmail());
            restaurant.setName(restaurantRevise.getName());
            restaurant.setLocation(restaurantRevise.getLocation());
            restaurant.setBankAccountId(restaurantRevise.getBankAccountId());
            restaurantRepository.saveAndFlush(restaurant);
            restaurantRevise.setApproved(true);
            restaurantReviseRepository.saveAndFlush(restaurantRevise);
            EmailUtil.sendRestaurantChange(oldEmail, restaurant.getEmail());
            commonStateMessage.setSuccessful(true);
            commonStateMessage.setInfo("修改已写入");
            return commonStateMessage;
        } catch (Exception e) {
            e.printStackTrace();
            commonStateMessage.setSuccessful(false);
            commonStateMessage.setInfo("数据库出错：" + e.getMessage());
            return commonStateMessage;
        }
    }

    @Transactional
    @Override
    public CommonStateMessage rejectRevise(int restaurantId) {
        CommonStateMessage commonStateMessage = new CommonStateMessage();
        try {
            Restaurant restaurant = restaurantRepository.getOne(restaurantId);
            RestaurantRevise restaurantRevise = restaurantReviseRepository.getOne(restaurantId);
            restaurantReviseRepository.delete(restaurantRevise);
            EmailUtil.sendRestaurantFailEmail(restaurant.getEmail());
            commonStateMessage.setSuccessful(true);
            commonStateMessage.setInfo("修改申请已删除");
            return commonStateMessage;
        } catch (Exception e) {
            e.printStackTrace();
            commonStateMessage.setSuccessful(false);
            commonStateMessage.setInfo("数据库出错：" + e.getMessage());
            return commonStateMessage;
        }
    }

    @Transactional
    @Override
    public Iterable<Restaurant> getRestaurantWaitRegister() {
        return restaurantRepository.getRestaurantsByValid(false);
    }

    @Transactional
    @Override
    public Iterable<RestaurantRevise> getWaitRevise() {
        return restaurantReviseRepository.findByApproved(false);
    }


    @Transactional
    @Override
    public ManagerStatistic getStatistic() {
        ManagerStatistic managerStatistic = new ManagerStatistic();
        int customerNumber = (int) customerRepository.count();
        int restaurantNumber = (int) restaurantRepository.count();
        int orderNumber = (int) foodOrderRepository.count();
        managerStatistic.setCustomerNumber(customerNumber);
        managerStatistic.setRestaurantNumber(restaurantNumber);
        managerStatistic.setOrderNumber(orderNumber);
        return managerStatistic;
    }
}
