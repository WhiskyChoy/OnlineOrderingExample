package com.example.demo.config;

import com.example.demo.dao.ManagerRepository;
import com.example.demo.entity.Manager;
import com.example.demo.global.GlobalValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

@EnableScheduling
@Configuration
public class InitConfig extends WebMvcConfigurationSupport implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(InitConfig.class);

    @Resource
    private ManagerRepository managerRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        String managerAccountStr = GlobalValue.systemBankAccount;
        String username = "admin";
        String passcode = "520748";
        Manager findManager = managerRepository.findManagerByUsername("admin");
        if (findManager != null) {
            log.info("管理员已经存在");
            return;
        }
        Manager manager = new Manager();
        manager.setBankAccountId(managerAccountStr);
        manager.setUsername(username);
        manager.setPasscode(passcode);
        try {
            managerRepository.saveAndFlush(manager);
            log.info("管理员初始化完成");
        } catch (Exception e) {
            log.error("管理员初始化失败");
        }
    }
}
