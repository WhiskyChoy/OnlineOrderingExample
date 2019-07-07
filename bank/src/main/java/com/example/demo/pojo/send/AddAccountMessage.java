package com.example.demo.pojo.send;

public class AddAccountMessage {
    private boolean successful;
    private String info;

    public AddAccountMessage() {
        successful = false;
        info = "Info is not set";
    }

    public AddAccountMessage(boolean successful, String info) {
        this.successful = successful;
        this.info = info;
    }

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
}
