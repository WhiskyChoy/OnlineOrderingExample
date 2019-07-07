package com.example.demo.util;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JSONHelper {
    public static JSONObject requestToJson(HttpServletRequest request) {
        JSONObject jsonObject = null;
        try {
            jsonObject = streamToJson(request.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject streamToJson(InputStream inputStream) throws IOException {
        StringBuilder jsonStr = new StringBuilder();
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            jsonStr.append(inputStr);
        }
        streamReader.close();
        return new JSONObject(jsonStr.toString());
    }
}
