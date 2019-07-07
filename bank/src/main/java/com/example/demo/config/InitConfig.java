package com.example.demo.config;

import com.example.demo.entity.BankAccount;
import com.example.demo.service.BankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;
import java.util.Random;

@Configuration
public class InitConfig extends WebMvcConfigurationSupport implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(InitConfig.class);
    private static final boolean shouldAdd100 = false;

    @Resource
    private BankAccountService bankAccountService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        String managerAccountStr = "6220152685452632021";
        double balance = 99999999;
        BankAccount managerBankAccount = new BankAccount(managerAccountStr, balance);
        try {
            bankAccountService.addAccount(managerBankAccount.getId(), managerBankAccount.getBalance());
        } catch (Exception e) {
            log.error("初始化Yummy管理员失败");
        }
        int accountNumber = 100;
        log.info("创建Yummy管理员用账户完成");
        if (shouldAdd100) {
            for (int i = 0; i < accountNumber; i++) {
                BankAccount bankAccount = generateRandBankAccount();
                bankAccountService.addAccount(bankAccount.getId(), bankAccount.getBalance());
            }
            log.info("创建" + accountNumber + "个账户完成");
        }
    }

    private BankAccount generateRandBankAccount() {
        //卡号19位
        Random random = new Random();
        String prefix = "6220";
        StringBuilder stringBuffer = new StringBuilder(prefix);
        int cardLength = 19;
        int bound = cardLength - prefix.length();
        for (int i = 0; i < bound; i++) {
            stringBuffer.append(random.nextInt(9));
        }
        String cardNumber = stringBuffer.toString();
        double balance = (100 + 2000 * Math.random());
        balance = Double.parseDouble(String.format("%.2f", balance));
        return new BankAccount(cardNumber, balance);
    }
}
