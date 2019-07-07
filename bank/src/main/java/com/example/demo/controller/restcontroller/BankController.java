package com.example.demo.controller.restcontroller;

import com.example.demo.pojo.receive.AddAccountRequest;
import com.example.demo.pojo.receive.FindAccountRequest;
import com.example.demo.pojo.receive.TransitRequest;
import com.example.demo.pojo.send.AddAccountMessage;
import com.example.demo.pojo.send.FindAccountMessage;
import com.example.demo.pojo.send.GetAllAccountsMessage;
import com.example.demo.pojo.send.TransitMessage;
import com.example.demo.service.BankAccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/bank")
public class BankController {
    @Resource
    private BankAccountService bankAccountService;

    @CrossOrigin
    @RequestMapping(value = "/transit", method = RequestMethod.POST)
    public TransitMessage doTransit(@RequestBody TransitRequest transitRequest) {
        return bankAccountService.doTransit(transitRequest.getFrom(), transitRequest.getTo(), transitRequest.getValue());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public AddAccountMessage addAccount(@RequestBody AddAccountRequest addAccountRequest) {
        return bankAccountService.addAccount(addAccountRequest.getId(), addAccountRequest.getBalance());
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public GetAllAccountsMessage getAllAccounts() {
        return bankAccountService.getAllAccounts();
    }

    @CrossOrigin
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public FindAccountMessage findAccount(@RequestBody FindAccountRequest findAccountRequest) {
        return bankAccountService.findAccount(findAccountRequest.getBankAccountId());
    }
}
