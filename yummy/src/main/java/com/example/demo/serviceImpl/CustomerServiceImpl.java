package com.example.demo.serviceImpl;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.global.GlobalValue;
import com.example.demo.pojo.receive.CustomerOrderRequest;
import com.example.demo.pojo.receive.CustomerReviseRequest;
import com.example.demo.pojo.receive.WebOrderItem;
import com.example.demo.pojo.send.CommonStateMessage;
import com.example.demo.pojo.send.CustomerExistMessage;
import com.example.demo.service.CustomerService;
import com.example.demo.util.BankUtil;
import com.example.demo.util.NumberUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Set;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerRepository customerRepository;
    @Resource
    private LocationRepository locationRepository;
    @Resource
    private RestaurantRepository restaurantRepository;
    @Resource
    private FoodProductRepository foodProductRepository;
    @Resource
    private FoodOrderRepository foodOrderRepository;
    @Resource
    private OrderItemRepository orderItemRepository;

    @Override
    public Customer updateCustomer(String email, String name, String bankAccountId) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer != null) {
            return getCustomer(email, name, bankAccountId, customer);
        } else {
            Customer newCustomer = new Customer();
            return getCustomer(email, name, bankAccountId, newCustomer);
        }
    }

    public Customer getCustomer(String email) {
        return customerRepository.findCustomerByEmail(email);
    }

    private Customer getCustomer(String email, String name, String bankAccountId, Customer customer) {
        customer.setEmail(email);
        customer.setName(name);
        customer.setBankAccountId(bankAccountId);
        customer.setValid(true);
        try {
            customer = customerRepository.saveAndFlush(customer);
            return customer;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public CustomerExistMessage checkExist(String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        CustomerExistMessage customerExistMessage = new CustomerExistMessage();
        if (customer != null) {
            customerExistMessage.setExisted(true);
        } else {
            customerExistMessage.setExisted(false);
        }
        return customerExistMessage;
    }

    @Transactional
    @Override
    public CommonStateMessage addLocation(Location location, Customer customer) {
        CommonStateMessage commonStateMessage = new CommonStateMessage();
        try {
            customer = customerRepository.getOne(customer.getId());
            Set<Location> locations = customer.getLocations();
            for (Location location1 : locations) {
                if (location1.equals(location)) {
                    commonStateMessage.setSuccessful(false);
                    commonStateMessage.setInfo("该用户已有该地址");
                    return commonStateMessage;
                }
            }
            locationRepository.saveAndFlush(location);
            locations.add(location);
            customerRepository.saveAndFlush(customer);
            commonStateMessage.setSuccessful(true);
            commonStateMessage.setInfo("地址已写入数据库");
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
    public Iterable<Location> getLocations(Customer customer) {
        customer = customerRepository.getOne(customer.getId());
        return customer.getLocations();
    }

    @Transactional
    @Override
    public CommonStateMessage deleteLocation(Customer customer, Location location) {
        CommonStateMessage commonStateMessage = new CommonStateMessage();
        try {
            customer = customerRepository.getOne(customer.getId());
            if (customer == null) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("用户不存在");
                return commonStateMessage;
            }
            customer.getLocations().removeIf(location1 -> location1.equals(location));
            customerRepository.saveAndFlush(customer);
            commonStateMessage.setInfo("已删除地址链接，但不删除地址");
            commonStateMessage.setSuccessful(true);
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
    public CommonStateMessage addOrder(CustomerOrderRequest customerOrderRequest, int customerId) {
        CommonStateMessage commonStateMessage = new CommonStateMessage();
        try {
            Customer customer = customerRepository.getOne(customerId);
            Location to = locationRepository.getOne(customerOrderRequest.getCustomerLocationId());
            Restaurant restaurant = restaurantRepository.getOne(customerOrderRequest.getRestaurantId());
            Location from = restaurant.getLocation();
            Iterable<WebOrderItem> webOrderItems = customerOrderRequest.getOrderItems();
            double finalCost = 0;
            FoodOrder foodOrder = new FoodOrder();
            foodOrder = foodOrderRepository.saveAndFlush(foodOrder);

            //注意mapped by 关系存储要在 有 mapped by 属性对的那边做
            for (WebOrderItem webOrderItem : webOrderItems) {
                FoodProduct foodProduct = foodProductRepository.getOne(webOrderItem.getProductId());
                if (foodProduct.getAmount() < webOrderItem.getOrderNumber()) {
                    throw new Exception("剩余数量不足");
                } else {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setFoodProduct(foodProduct);
                    orderItem.setProductNumber(webOrderItem.getOrderNumber());
                    //这一步很关键
                    orderItem.setFoodOrder(foodOrder);
                    orderItemRepository.saveAndFlush(orderItem);
                    finalCost += foodProduct.getPrice() * foodProduct.getRate() * webOrderItem.getOrderNumber();
                }
            }
            finalCost *= (1 - 0.1 * customer.getLevel());
            foodOrder.setCustomer(customer);
            foodOrder.setFrom(from);
            foodOrder.setTo(to);
            foodOrder.setRestaurant(restaurant);
            foodOrder.setFinalCost(finalCost);

            foodOrder = foodOrderRepository.saveAndFlush(foodOrder);
            commonStateMessage.setInfo("增加待支付订单成功");
            commonStateMessage.setSuccessful(true);
            commonStateMessage.setTargetId(foodOrder.getId());
            return commonStateMessage;
        } catch (Exception e) {
            e.printStackTrace();
            commonStateMessage.setInfo(e.getMessage());
            commonStateMessage.setSuccessful(false);
            return commonStateMessage;
        }
    }

    @Transactional
    @Override
    public CommonStateMessage reviseMessage(CustomerReviseRequest customerReviseRequest, int customerId) {
        CommonStateMessage commonStateMessage = new CommonStateMessage();
        String bankAccountId = customerReviseRequest.getBankAccountId();
        CommonStateMessage checkBankAccountMessage = BankUtil.checkAccountExists(bankAccountId);
        if (!checkBankAccountMessage.isSuccessful()) {
            commonStateMessage.setSuccessful(false);
            commonStateMessage.setInfo("修改回撤，因为" + checkBankAccountMessage.getInfo());
            return commonStateMessage;
        }
        try {
            Customer customer = customerRepository.getOne(customerId);
            customer.setName(customerReviseRequest.getName());
            customer.setBankAccountId(bankAccountId);
            customerRepository.saveAndFlush(customer);
            commonStateMessage.setSuccessful(true);
            commonStateMessage.setInfo("信息写入数据库成功");
            return commonStateMessage;
        } catch (Exception e) {
            e.printStackTrace();
            commonStateMessage.setInfo(e.getMessage());
            commonStateMessage.setSuccessful(false);
            return commonStateMessage;
        }
    }

    @Transactional
    @Override
    public Customer getCustomer(int customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    @Transactional
    @Override
    public CommonStateMessage payOrder(int orderId, int userId) {
        CommonStateMessage commonStateMessage = new CommonStateMessage();
        try {
            FoodOrder foodOrder = foodOrderRepository.findById(orderId).orElse(null);
            if (foodOrder == null) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("订单未找到，无法支付");
                return commonStateMessage;
            }
            if (foodOrder.getCustomer().getId() != userId) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("您未发起该订单，无法支付");
                return commonStateMessage;
            }
            if (foodOrder.isPaid()) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("订单已支付，无须支付");
                return commonStateMessage;
            }
            if (foodOrder.isActualCanceled()) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("订单已取消，无法支付");
                return commonStateMessage;
            }
            String fromBankAccountId = foodOrder.getCustomer().getBankAccountId();
            String toBankAccountId = GlobalValue.systemBankAccount;
            double value = foodOrder.getFinalCost();
            CommonStateMessage transitMessage;
            if (GlobalValue.useMonthlyBatch) {
                transitMessage = BankUtil.transit(fromBankAccountId, toBankAccountId, value);
            } else {
                transitMessage = BankUtil.transit(fromBankAccountId, foodOrder.getRestaurant().getBankAccountId(), value);
            }
            if (!transitMessage.isSuccessful()) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo(transitMessage.getInfo());
                return commonStateMessage;
            }
            foodOrder.setPaid(true);
            if (!GlobalValue.useMonthlyBatch) {
                foodOrder.setQuickPaidToRestaurant(true);
            }
            Customer customer = customerRepository.findById(foodOrder.getCustomer().getId()).orElse(null);
            if (customer != null) {
                customer.setConsumption(customer.getConsumption() + value);
                customerRepository.saveAndFlush(customer);
            }
            foodOrderRepository.saveAndFlush(foodOrder);
            commonStateMessage.setSuccessful(true);
            commonStateMessage.setInfo("网银系统已扣款");
            return commonStateMessage;
        } catch (Exception e) {
            commonStateMessage.setInfo(e.getMessage());
            commonStateMessage.setSuccessful(false);
            return commonStateMessage;
        }
    }

    @Transactional
    @Override
    public CommonStateMessage cancelOrder(int orderId, int userId) {
        CommonStateMessage commonStateMessage = new CommonStateMessage();
        try {
            FoodOrder foodOrder = foodOrderRepository.findById(orderId).orElse(null);
            if (foodOrder == null) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("订单未找到，无法取消");
                return commonStateMessage;
            }
            if (foodOrder.getCustomer().getId() != userId) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("您未发起该订单，无法取消");
                return commonStateMessage;
            }
            if (foodOrder.isPaid()) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("订单已支付，无法取消");
                return commonStateMessage;
            }
            if (foodOrder.isActualCanceled()) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("订单已取消，不能再次取消");
                return commonStateMessage;
            }
            foodOrder.setUserCanceled(true);
            foodOrderRepository.saveAndFlush(foodOrder);
            foodOrderRepository.saveAndFlush(foodOrder);
            commonStateMessage.setSuccessful(true);
            commonStateMessage.setInfo("订单已被取消，仍有记录");
            return commonStateMessage;
        } catch (Exception e) {
            commonStateMessage.setInfo(e.getMessage());
            commonStateMessage.setSuccessful(false);
            return commonStateMessage;
        }
    }


    @Transactional
    @Override
    public CommonStateMessage confirmOrder(int orderId, int userId) {
        CommonStateMessage commonStateMessage = new CommonStateMessage();
        try {
            FoodOrder foodOrder = foodOrderRepository.findById(orderId).orElse(null);
            if (foodOrder == null) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("订单未找到，无法确认");
                return commonStateMessage;
            }
            if (foodOrder.getCustomer().getId() != userId) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("您未发起该订单，无法确认");
                return commonStateMessage;
            }
            if (!foodOrder.isPaid()) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("订单未支付，无法确认");
                return commonStateMessage;
            }
            if (foodOrder.isActualCanceled()) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("订单已取消，无法确认");
                return commonStateMessage;
            }
            if (foodOrder.isActualConfirmed()) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("订单已确认，无法再次确认");
                return commonStateMessage;
            }
            if (foodOrder.isRefunded()) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("订单已退款，无法确认");
                return commonStateMessage;
            }

            foodOrder.setUserConfirmed(true);
            foodOrderRepository.saveAndFlush(foodOrder);

            commonStateMessage.setSuccessful(true);
            commonStateMessage.setInfo("订单确认成功");
            return commonStateMessage;
        } catch (Exception e) {
            commonStateMessage.setInfo(e.getMessage());
            commonStateMessage.setSuccessful(false);
            return commonStateMessage;
        }
    }

    @Transactional
    @Override
    public CommonStateMessage refundOrder(int orderId, int userId) {
        CommonStateMessage commonStateMessage = new CommonStateMessage();
        try {
            FoodOrder foodOrder = foodOrderRepository.findById(orderId).orElse(null);
            if (foodOrder == null) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("订单未找到，无法退款");
                return commonStateMessage;
            }
            if (foodOrder.getCustomer().getId() != userId) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("您未发起该订单，无法退款");
                return commonStateMessage;
            }
            if (!foodOrder.isPaid()) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("订单未支付，无法退款");
                return commonStateMessage;
            }
            if (foodOrder.isActualCanceled()) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("订单已取消，无法退款");
                return commonStateMessage;
            }
            if (foodOrder.isActualConfirmed()) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("订单已确认，无法退款");
                return commonStateMessage;
            }
            if (foodOrder.isRefunded()) {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo("订单已退款，无法再次退款");
                return commonStateMessage;
            }

            String toBankAccountId = foodOrder.getCustomer().getBankAccountId();
            String fromBankAccountId = GlobalValue.systemBankAccount;
            double confirmTimeInMillis = GlobalValue.confirmInMins * 60 * 1000;
            double refundRate = (confirmTimeInMillis - (System.currentTimeMillis() - foodOrder.getOrderDate().getTime())) / confirmTimeInMillis;
            double value = foodOrder.getFinalCost() * refundRate;
            value = NumberUtil.doubleFormat(value);
            CommonStateMessage transitMessage;
            if (GlobalValue.useMonthlyBatch) {
                transitMessage = BankUtil.transit(fromBankAccountId, toBankAccountId, value);
            } else {
                transitMessage = BankUtil.transit(foodOrder.getRestaurant().getBankAccountId(), toBankAccountId, value);
            }
            if (transitMessage.isSuccessful()) {
                foodOrder.setRefunded(true);
                foodOrder.setFinalCost(NumberUtil.doubleFormat(foodOrder.getFinalCost() - value));
                Customer customer = customerRepository.findById(foodOrder.getCustomer().getId()).orElse(null);
                if (customer != null) {
                    customer.setConsumption(customer.getConsumption() - value);
                    customerRepository.saveAndFlush(customer);
                }
                foodOrderRepository.saveAndFlush(foodOrder);
                commonStateMessage.setSuccessful(true);
                commonStateMessage.setInfo("退款成功，退款额为" + value + "元");
                return commonStateMessage;
            } else {
                commonStateMessage.setSuccessful(false);
                commonStateMessage.setInfo(transitMessage.getInfo());
                return commonStateMessage;
            }

        } catch (Exception e) {
            commonStateMessage.setInfo(e.getMessage());
            commonStateMessage.setSuccessful(false);
            return commonStateMessage;
        }
    }

    @Transactional
    @Override
    public Iterable<FoodOrder> getFoodOrders(Customer customer) {
        return foodOrderRepository.findByCustomer(customer);
    }
}
