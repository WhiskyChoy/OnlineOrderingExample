package com.example.demo.pojo.send;

public class CustLoginMeta {
    private int totalWaitTime = 1;
    private int authnCodeLength = 4;

    public CustLoginMeta(int totalWaitTime, int authnCodeLength) {
        this.totalWaitTime = totalWaitTime;
        this.authnCodeLength = authnCodeLength;
    }

    public CustLoginMeta() {
    }

    public int getTotalWaitTime() {
        return totalWaitTime;
    }

    public void setTotalWaitTime(int totalWaitTime) {
        this.totalWaitTime = totalWaitTime;
    }

    public int getAuthnCodeLength() {
        return authnCodeLength;
    }

    public void setAuthnCodeLength(int authnCodeLength) {
        this.authnCodeLength = authnCodeLength;
    }
}
