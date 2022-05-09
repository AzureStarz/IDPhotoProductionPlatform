package com.example.springbootexample.controller;

import com.example.springbootexample.dao.PhotoDao;
import com.example.springbootexample.model.Photo;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import com.example.springbootexample.Utils.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;

@RestController
public class Hello {
    @Autowired(required=false)
    private PhotoDao photoDao;
    @PostMapping("/api/seg")
    public String segment(@RequestBody Map<String, String> params){
        String imgStr = params.get("imgStr");
        String fileName = params.get("fileName");
        Integer userId = Integer.parseInt(params.get("userId"));
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/body_seg";
        try {

            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AuthService.getAuth();

            String result = HttpUtil.post(url, accessToken, param);
            JsonObject obj = GsonUtils.fromJson(result, JsonObject.class);
            //System.out.println(result);
            JsonElement foreground = obj.get("foreground");

            String originPath = FileUtil.GetFrontEndPath(fileName);
            Renew.GenerateImage(imgStr, originPath);
            Photo originPhoto = new Photo();
            originPhoto.setUserId(userId);
            originPhoto.setPhotoName(fileName);
            originPhoto.setPhotoPath(originPath);
            photoDao.savePhoto(originPhoto);

            String base64NewImg = GsonUtils.toJson(foreground);
            String newFilename = "modify-"+ fileName;
            String newPath = FileUtil.GetFrontEndPath(newFilename);
            Renew.GenerateImage(base64NewImg, newPath);
            Photo newPhoto = new Photo();
            newPhoto.setUserId(userId);
            newPhoto.setPhotoPath(newPath);
            newPhoto.setPhotoName(newFilename);
            photoDao.savePhoto(newPhoto);
            return newFilename;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        // test : return imgStr + fileName;
        //return "hello world <img src=\"images/test.jpg\">";
    }

    @PostMapping("/api/beautify")
    public String beatuify(@RequestBody Map<String, String> params){
        String imgStr = params.get("imgStr");
        String fileName = params.get("fileName");
        Integer userId = Integer.parseInt(params.get("userId"));
        String imagePath = "lib/time.jpg";

        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try{
            URL url = new URL("https://openapi.mtlab.meitu.com/v3/makeup?api_key=e39297a28983493185cb529b2fdf70e0&api_secret=95a47f533cce48beb4e253d2028da4b2");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setConnectTimeout(30000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");

            out = new OutputStreamWriter(conn.getOutputStream());
            String jsonStr = "{\"parameter\":{\"beautyAlpha\":70},\"extra\":{},\"media_info_list\":[{\"media_data\":\""+ imgStr +"\",\"media_profiles\":{\"media_data_type\":\"jpg\"}}]}";
            out.write(jsonStr);
            out.flush();
            out.close();

            if (200 == conn.getResponseCode()){
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            }else{
                System.out.println("ResponseCode is an error code:" + conn.getResponseCode());
                in = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
            }
            String line;
            while ((line = in.readLine()) != null){
                result.append(line);
                //System.out.println(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        String newFilename = "beautify-"+ fileName;
        String newPath = FileUtil.GetFrontEndPath(newFilename);
        String base64NewImg=result.toString();
        Renew.GenerateImage(base64NewImg, newPath);
        Photo newPhoto = new Photo();
        newPhoto.setUserId(userId);
        newPhoto.setPhotoPath(newPath);
        newPhoto.setPhotoName(newFilename);
        photoDao.savePhoto(newPhoto);
        return newFilename;
    }
    @PostMapping("/api/getPhotos")
    public List<Photo> getPhotos(@Param("userId") Integer userId){
        return photoDao.getPhotosWithUserId(userId);
    }

    @PostMapping("/api/deletePhoto")
    public void deletePhoto(@Param("photoID") Integer photoID){
        photoDao.deletePhoto(photoID);
    }
}
