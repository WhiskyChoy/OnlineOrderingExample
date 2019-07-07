package com.example.demo.pojo.receive;

public class CustomerLoginRequest {
    private String name;
    private String email;
    private String authnCode;
    private String bankAccountId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthnCode() {
        return authnCode;
    }

    public void setAuthnCode(String authnCode) {
        this.authnCode = authnCode;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }
}
