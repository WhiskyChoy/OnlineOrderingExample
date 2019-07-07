package com.example.demo.serviceImpl;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.global.GlobalValue;
import com.example.demo.pojo.receive.AddProductRequest;
import com.example.demo.pojo.receive.RestaurantRegisterRequest;
import com.example.demo.pojo.send.LoginStateMessage;
import com.example.demo.pojo.send.CommonStateMessage;
import com.example.demo.service.RestaurantService;
import com.example.demo.util.BankUtil;
import com.example.demo.util.LocationUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    @Resource
    private RestaurantRepository restaurantRepository;
    @Resource
    private AuthnCodeRepository authnCodeRepository;
    @Resource
    private FoodProductRepository foodProductRepository;
    @Resource
    private LocationRepository locationRepository;
    @Resource
    private FoodOrderRepository foodOrderRepository;
    @Resource
    private RestaurantReviseRepository restaurantReviseRepository;

    @Override
    public CommonStateMessage registerStateMessage(RestaurantRegisterRequest registerRequest) {
        CommonStateMessage commonStateMessage = new CommonStateMessage();

        Location location = registerRequest.getLocation();
        String name = registerRequest.getName();
        String email = registerRequest.getEmail();
        String bankAccountId = registerRequest.getBankAccountId();
        Restaurant restaurant = new Restaurant();
        restaurant.setLocation(location);
        restaurant.setName(name);
        restaurant.setEmail(email);
        restaurant.setBankAccountId(bankAccountId);

        try {
            restaurantRepository.saveAndFlush(restaurant);
            commonStateMessage.setSuccessful(true);
            commonStateMessage.setInfo("信息已存入数据库");
            return commonStateMessage;
        } catch (Exception e) {
            commonStateMessage.setSuccessful(false);
            commonStateMessage.setInfo("将信息存入数据库时出错");
            e.printStackTrace();
            return commonStateMessage;
        }
    }

    @Transactional
    @Override
    public LoginStateMessage loginStateMessage(String authnCodeStr) {
        LoginStateMessage loginStateMessage = new LoginStateMessage();
        try {
            AuthnCode authnCode = authnCodeRepository.findAuthnCodeByActualCode(authnCodeStr);
            if (authnCode == null) {
                loginStateMessage.setLoggedIn(false);
                loginStateMessage.setInfo("输入了不存在的识别码");
                return loginStateMessage;
            }
            Restaurant restaurant = authnCode.getRestaurant();
            if (restaurant == null) {
                loginStateMessage.setLoggedIn(false);
                loginStateMessage.setInfo("数据库映射错误");
                return loginStateMessage;
            }
            if (!restaurant.isValid()) {
                loginStateMessage.setLoggedIn(false);
                loginStateMessage.setInfo("该账户已被设为无效");
                return loginStateMessage;
            }
            loginStateMessage.setLoggedIn(true);
            loginStateMessage.setInfo("已查找到相关信息");
            loginStateMessage.setCurrentUser(restaurant);
            loginStateMessage.setUserType(UserType.RESTAURANT.toString());
            return loginStateMessage;
        } catch (Exception e) {
            loginStateMessage.setLoggedIn(false);
            loginStateMessage.setInfo("查询数据库出错");
            return loginStateMessage;
        }
    }

    @Override
    public CommonStateMessage addProductMessage(AddProductRequest addProductRequest, Restaurant restaurant) {
        CommonStateMessage commonStateMessage = new CommonStateMessage();

        FoodProduct foodProduct = new FoodProduct();
        foodProduct.setAmount(addProductRequest.getAmount());
        foodProduct.setName(addProductRequest.getName());
        foodProduct.setStartDate(addProductRequest.getStartDate());
        foodProduct.setEndDate(addProductRequest.getEndDate());
        foodProduct.setPrice(addProductRequest.getPrice());
        foodProduct.setDescription(addProductRequest.getDescription());
        foodProduct.setRestaurant(restaurant);
        foodProduct.setRate(addProductRequest.getRate());
        ProductType productType = ProductType.getProductType(addProductRequest.getType());
        if (productType == null) {
            commonStateMessage.setSuccessful(false);
            commonStateMessage.setInfo("产品类型出错");
            return commonStateMessage;
        }
        foodProduct.setType(productType.toString());
        try {
            foodProductRepository.saveAndFlush(foodProduct);
            commonStateMessage.setSuccessful(true);
            commonStateMessage.setInfo("菜品信息已写入数据库");
            return commonStateMessage;
        } catch (Exception e) {
            e.printStackTrace();
            commonStateMessage.setSuccessful(false);
            commonStateMessage.setInfo("数据库出错");
            return commonStateMessage;
        }
    }

    @Transactional
    @Override
    public Iterable<Restaurant> getRestaurantNear(int locationId, double timeInMills) {
        Location location = locationRepository.getOne(locationId);
        double estimateDistance = timeInMills * GlobalValue.speedKMPerHour / 3600;
        List<Restaurant> restaurants = restaurantRepository.findAll();
        restaurants.removeIf(restaurant -> {
            Location location1 = restaurant.getLocation();
            double distance = LocationUtil.getDistance(location1.getLatitude(), location1.getLongitude(), location.getLatitude(), location.getLongitude());
            int estimateTime = (int) (distance / 1000 / GlobalValue.speedKMPerHour * 60);
            if (distance < estimateDistance) {
                restaurant.setDistance(distance);
                restaurant.setEstimateMin(estimateTime);
            }
            return !restaurant.isValid() || distance > estimateDistance;
        });
        return restaurants;
    }

    @Transactional
    @Override
    public Iterable<FoodProduct> getFoodProduct(int restaurantId) {
        Restaurant restaurant = restaurantRepository.getOne(restaurantId);
        long currentTime = System.currentTimeMillis();
        Set<FoodProduct> foodProducts = restaurant.getFoodProducts();
        foodProducts.removeIf(foodProduct -> foodProduct.getStartDate().getTime() > currentTime || foodProduct.getEndDate().getTime() < currentTime || foodProduct.getAmount() == 0);
        return foodProducts;
    }


    @Transactional
    @Override
    public CommonStateMessage revise(RestaurantRevise restaurantRevise, Integer restaurantId) {
        CommonStateMessage commonStateMessage = new CommonStateMessage();
        try {
            Restaurant restaurant = restaurantRepository.getOne(restaurantId);
            if (!restaurant.checkReviseValid(restaurantRevise)) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("未作信息修改");
                return commonStateMessage;
            }
            if (!BankUtil.checkAccountExists(restaurantRevise.getBankAccountId()).isSuccessful()) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("银行账号不存在");
                return commonStateMessage;
            }
            restaurantRevise.setRestaurantId(restaurantId);
            restaurantRevise.setApproved(false);
            restaurantReviseRepository.saveAndFlush(restaurantRevise);
            commonStateMessage.setSuccessful(true);
            commonStateMessage.setInfo("修改申请记录成功，待审核");
            return commonStateMessage;
        } catch (Exception e) {
            e.printStackTrace();
            commonStateMessage.setSuccessful(false);
            commonStateMessage.setInfo("数据库出错:" + e.getMessage());
            return commonStateMessage;
        }
    }

    @Transactional
    @Override
    public Restaurant getRestaurant(int restaurantId) {
        return restaurantRepository.findById(restaurantId).orElse(null);
    }

    @Transactional
    @Override
    public Iterable<FoodOrder> getFoodOrder(Restaurant restaurant) {
        return foodOrderRepository.findByRestaurant(restaurant);
    }
}
