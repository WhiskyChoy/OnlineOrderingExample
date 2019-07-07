package com.example.demo.service;

import com.example.demo.pojo.send.AddAccountMessage;
import com.example.demo.pojo.send.FindAccountMessage;
import com.example.demo.pojo.send.GetAllAccountsMessage;
import com.example.demo.pojo.send.TransitMessage;

public interface BankAccountService {
    TransitMessage doTransit(String from, String to, double value);

    AddAccountMessage addAccount(String id, double balance);

    GetAllAccountsMessage getAllAccounts();

    FindAccountMessage findAccount(String account);
}
