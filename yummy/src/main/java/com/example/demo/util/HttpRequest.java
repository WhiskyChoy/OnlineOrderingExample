package com.example.demo.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;


public class HttpRequest {

    public static JSONObject post(String url, JSONObject json) {
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        JSONObject response = null;
        try {
            StringEntity s = new StringEntity(json.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");
            post.setEntity(s);

            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                HttpEntity entity = res.getEntity();
                response = JSONHelper.streamToJson(entity.getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
