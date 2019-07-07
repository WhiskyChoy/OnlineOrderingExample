package com.example.demo.task;

import com.example.demo.dao.FoodOrderRepository;
import com.example.demo.entity.FoodOrder;
import com.example.demo.entity.Restaurant;
import com.example.demo.global.GlobalValue;
import com.example.demo.pojo.send.CommonStateMessage;
import com.example.demo.serviceImpl.ManagerServiceImpl;
import com.example.demo.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;

@Component
public class BatchTransition {
    @Resource
    private FoodOrderRepository foodOrderRepository;

    private static final Logger log = LoggerFactory.getLogger(ManagerServiceImpl.class);


    @Scheduled(cron = "0 0 1 1 * ?") //每月1号1点
    @Transactional
    public void doBatchTransition() {
        if (GlobalValue.useMonthlyBatch) {
            log.info("Doing batch transition");
            Iterable transitions = foodOrderRepository.getTransitionUseMonth(getMonth() - 1);
            boolean result = doTransition(transitions);
            System.out.println("All transition success:" + result);
        }
    }


//    public void doQuickTransition(FoodOrder foodOrder) {
//        if (!GlobalValue.useMonthlyBatch) {
//            log.info("Doing quick transition");
//            ArrayList<Object[]> transitions = new ArrayList<>();
//            Object[] cells = new Object[3];
//            cells[0] = null;
//            cells[1] = foodOrder.getRestaurant();
//            cells[2] = foodOrder.getFinalCost();
//            transitions.add(cells);
//            boolean singleResult = doTransition(transitions);
//            if (singleResult) {
//                foodOrder.setQuickPaidToRestaurant(true);
//                foodOrderRepository.saveAndFlush(foodOrder);
//            }
//        }
//    }

    private boolean doTransition(Iterable transitions) {
        boolean result = true;
        for (Object row : transitions) {
            Object[] cells = (Object[]) row;
            Restaurant restaurant = (Restaurant) cells[1];
            double value = (double) cells[2];
            String toBankAccountId = restaurant.getBankAccountId();
            String fromBankAccountId = GlobalValue.systemBankAccount;
            CommonStateMessage commonStateMessage = BankUtil.transit(fromBankAccountId, toBankAccountId, value);
            result = result && commonStateMessage.isSuccessful();
            if (commonStateMessage.isSuccessful()) {
                log.info(commonStateMessage.getInfo());
            } else {
                log.error(commonStateMessage.getInfo());
            }
        }
        return result;
    }


    private int getMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }
}
