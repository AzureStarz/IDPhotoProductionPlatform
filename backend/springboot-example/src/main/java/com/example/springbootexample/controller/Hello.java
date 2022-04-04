package com.example.springbootexample.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.http.HttpClient;

@RestController
public class Hello {
    @GetMapping("/hello")
    public String Hello(){

        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/body_seg";
        try {
            // 本地文件路径
            String filePath = "D:\\test\\2.jpg";
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AuthService.getAuth();

            String result = HttpUtil.post(url, accessToken, param);
            JsonObject obj = GsonUtils.fromJson(result, JsonObject.class);
            //System.out.println(result);
            JsonElement foreground = obj.get("foreground");
            Renew.GenerateImage(GsonUtils.toJson(foreground),"D:\\test\\3.jpg");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

        //return "hello world <img src=\"images/test.jpg\">";
    }

}
