package com.example.demo.pojo.send;

import com.example.demo.entity.BankAccount;

import java.util.List;

public class GetAllAccountsMessage {
    private boolean successful;
    private String info;
    private List<BankAccount> bankAccounts;

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }
}
