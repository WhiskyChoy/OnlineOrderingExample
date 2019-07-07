package com.example.demo.serviceImpl;

import com.example.demo.dao.BankAccountRepository;
import com.example.demo.entity.BankAccount;
import com.example.demo.pojo.send.AddAccountMessage;
import com.example.demo.pojo.send.FindAccountMessage;
import com.example.demo.pojo.send.GetAllAccountsMessage;
import com.example.demo.pojo.send.TransitMessage;
import com.example.demo.service.BankAccountService;
import com.example.demo.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private BankAccountRepository bankAccountRepository;

    @Autowired
    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public TransitMessage doTransit(String from, String to, double value) {
        TransitMessage transitMessage = new TransitMessage();
        try {
            BankAccount bankAccountFrom = bankAccountRepository.findById(from).orElse(null);
            BankAccount bankAccountTo = bankAccountRepository.findById(to).orElse(null);
            if (bankAccountFrom == null) {
                throw new Exception("支付方账户不存在");
            }
            if (bankAccountTo == null) {
                throw new Exception("收款方账户不存在");
            }
            if (bankAccountFrom.getBalance() - value >= 0) {
                bankAccountFrom.setBalance(NumberUtil.doubleFormat(bankAccountFrom.getBalance() - value));
                bankAccountTo.setBalance(NumberUtil.doubleFormat(bankAccountTo.getBalance() + value));
            } else {
                throw new Exception("支付方账户余额不足");
            }
            bankAccountRepository.saveAndFlush(bankAccountFrom);
            bankAccountRepository.saveAndFlush(bankAccountTo);
        } catch (Exception e) {
            transitMessage.setSuccessful(false);
            transitMessage.setInfo(e.getMessage());
            return transitMessage;
        }
        transitMessage.setSuccessful(true);
        transitMessage.setInfo("转账成功");
        return transitMessage;
    }

    @Override
    public AddAccountMessage addAccount(String id, double balance) {
        AddAccountMessage addAccountMessage = new AddAccountMessage();
        try {
            bankAccountRepository.saveAndFlush(new BankAccount(id, balance));
        } catch (Exception e) {
            addAccountMessage.setSuccessful(false);
            addAccountMessage.setInfo(e.getMessage());
            return addAccountMessage;
        }
        addAccountMessage.setSuccessful(true);
        addAccountMessage.setInfo("创建银行账户成功");
        return addAccountMessage;
    }

    @Override
    public GetAllAccountsMessage getAllAccounts() {
        GetAllAccountsMessage getAllAccountsMessage = new GetAllAccountsMessage();
        try {
            List<BankAccount> bankAccounts = bankAccountRepository.findAll();
            getAllAccountsMessage.setBankAccounts(bankAccounts);
        } catch (Exception e) {
            getAllAccountsMessage.setSuccessful(false);
            getAllAccountsMessage.setInfo(e.getMessage());
            return getAllAccountsMessage;
        }
        getAllAccountsMessage.setSuccessful(true);
        getAllAccountsMessage.setInfo("获取所有银行账户信息成功");
        return getAllAccountsMessage;
    }

    @Override
    public FindAccountMessage findAccount(String account) {
        System.out.println("called find account");
        FindAccountMessage findAccountMessage = new FindAccountMessage();
        try {
            BankAccount bankAccount = bankAccountRepository.findById(account).orElse(null);
            if (bankAccount == null) {
                throw new Exception("账户不存在");
            }
            findAccountMessage.setExisted(true);
        } catch (Exception e) {
            findAccountMessage.setExisted(false);
        }
        return findAccountMessage;
    }
}
