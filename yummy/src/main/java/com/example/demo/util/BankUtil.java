package com.example.demo.util;

import com.example.demo.global.GlobalValue;
import com.example.demo.pojo.send.CommonStateMessage;
import org.json.JSONObject;

public class BankUtil {
    public static CommonStateMessage checkAccountExists(String bankAccountId) {
        CommonStateMessage commonStateMessage = new CommonStateMessage();
        JSONObject send = new JSONObject();
        send.put("bankAccountId", bankAccountId);
        JSONObject message = HttpRequest.post(GlobalValue.bankServerUrlPrefix + "/api/bank/find", send);

        if (message == null) {
            commonStateMessage.setSuccessful(false);
            commonStateMessage.setInfo("访问外部银行服务出错，请重试");
            return commonStateMessage;
        }

        if (!message.get("existed").equals(true)) {
            commonStateMessage.setSuccessful(false);
            commonStateMessage.setInfo("该银行账号不存在，请重试");
            return commonStateMessage;
        }
        commonStateMessage.setSuccessful(true);
        commonStateMessage.setInfo("该银行账号存在");
        return commonStateMessage;
    }

    public static CommonStateMessage transit(String from, String to, double value) {
        CommonStateMessage commonStateMessage = new CommonStateMessage();
        JSONObject send = new JSONObject();
        send.put("from", from);
        send.put("to", to);
        send.put("value", value);
        JSONObject message = HttpRequest.post(GlobalValue.bankServerUrlPrefix + "/api/bank/transit", send);

        if (message == null) {
            commonStateMessage.setSuccessful(false);
            commonStateMessage.setInfo("访问外部银行服务出错，请重试");
            return commonStateMessage;
        }

        commonStateMessage.setSuccessful(message.getBoolean("successful"));
        commonStateMessage.setInfo(message.getString("info"));
        return commonStateMessage;
    }
}
