package com.example.springbootexample.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import com.example.springbootexample.Utils.*;
import java.io.File;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.util.Map;

@RestController
public class Hello {
    @PostMapping("/api/seg")
    public String segment(@RequestBody Map<String, String> params){
        String imgStr = params.get("imgStr");
        String fileName = params.get("fileName");
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/body_seg";
        try {
            // 本地文件路径
            //String filePath = "D:\\test\\2.jpg";
            //获取项目classes/static的地址
            System.out.println(imgStr+ fileName);
            String staticPath = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
            // String fileName = file.getOriginalFilename();  //获取文件名
            imgStr = imgStr.replace("^data:image/w+;base64,", "");
            // 图片存储目录及图片名称
            String url_path = "images" + File.separator + fileName;
            String url_path_new_photo = "images" + File.separator+ "modify-" + fileName;
            //图片保存路径
            String savePath = staticPath + File.separator + url_path;
            String savePathNew = staticPath + File.separator + url_path_new_photo;
            // 访问路径=静态资源路径+文件目录路径
            // String visitPath ="static/" + url_path_new_photo;

            //byte[] imgData = FileUtil.readFileByBytes(filePath);
            //String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AuthService.getAuth();

            String result = HttpUtil.post(url, accessToken, param);
            JsonObject obj = GsonUtils.fromJson(result, JsonObject.class);
            //System.out.println(result);
            JsonElement foreground = obj.get("foreground");
            Renew.GenerateImage(imgStr,savePath);
            Renew.GenerateImage(GsonUtils.toJson(foreground),savePathNew);
            return "modify-" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        // test : return imgStr + fileName;
        //return "hello world <img src=\"images/test.jpg\">";
    }

}
