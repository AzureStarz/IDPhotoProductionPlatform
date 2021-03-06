package com.example.springbootexample.controller;

import com.example.springbootexample.dao.PhotoDao;
import com.example.springbootexample.model.Photo;
import com.google.gson.JsonArray;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class Hello {
    @Autowired(required=false)
    private PhotoDao photoDao;
    private static final Map<Integer, String> filters = new HashMap<Integer, String>();
    static{
        filters.put(1, "Fa0145JwLfwjjbch");
        filters.put(2, "Fa0415o4g8OfwAw8");
        filters.put(3, "Fa0436P7QKK0zKRL");
        filters.put(4, "Fa0108HAz4MlDLVx");
        filters.put(5, "Fa0535samdIjz0Wq");
        filters.put(6, "Fa0727hdRPZAndOR");
    }
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


        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try{
            URL url = new URL("https://openapi.mtlab.meitu.com/v1/beauty?api_key=89f67c2b65374dc79eb311377335d71e&api_secret=b120fcf85ba8467b805fdd8fc46c2b5b");
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
                System.out.println(line);
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
        JsonObject obj = GsonUtils.fromJson(result.toString(), JsonObject.class);
        JsonArray media_info_list = obj.getAsJsonArray("media_info_list");
        JsonObject media_info = (JsonObject) media_info_list.get(0);
        String base64NewImg = GsonUtils.toJson(media_info.get("media_data"));
        Renew.GenerateImage(base64NewImg, newPath);
        Photo newPhoto = new Photo();
        newPhoto.setUserId(userId);
        newPhoto.setPhotoPath(newPath);
        newPhoto.setPhotoName(newFilename);
        photoDao.savePhoto(newPhoto);
        return newFilename;
    }

    @PostMapping("/api/save")
    public void save(@RequestBody Map<String, String> params) {
        String imgStr = params.get("imgStr");
        String fileName = params.get("fileName");
        Integer userId = Integer.parseInt(params.get("userId"));
        String originPath = FileUtil.GetFrontEndPath(fileName);
        Renew.GenerateImage(imgStr, originPath);
        Photo originPhoto = new Photo();
        originPhoto.setUserId(userId);
        originPhoto.setPhotoName(fileName);
        originPhoto.setPhotoPath(originPath);
        photoDao.savePhoto(originPhoto);
    }
    @PostMapping("/api/makeup")
    public String makeup(@RequestBody Map<String, String> params){
        String imgStr = params.get("imgStr");
        String fileName = params.get("fileName");
        Integer userId = Integer.parseInt(params.get("userId"));


        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try{
            URL url = new URL("https://openapi.mtlab.meitu.com/v3/makeup?api_key=89f67c2b65374dc79eb311377335d71e&api_secret=b120fcf85ba8467b805fdd8fc46c2b5b");
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
                System.out.println(line);
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
        String newFilename = "makeup-"+ fileName;
        String newPath = FileUtil.GetFrontEndPath(newFilename);
        JsonObject obj = GsonUtils.fromJson(result.toString(), JsonObject.class);
        JsonArray media_info_list = obj.getAsJsonArray("media_info_list");
        JsonObject media_info = (JsonObject) media_info_list.get(0);
        String base64NewImg = GsonUtils.toJson(media_info.get("media_data"));
        Renew.GenerateImage(base64NewImg, newPath);
        Photo newPhoto = new Photo();
        newPhoto.setUserId(userId);
        newPhoto.setPhotoPath(newPath);
        newPhoto.setPhotoName(newFilename);
        photoDao.savePhoto(newPhoto);
        return newFilename;
    }

    @PostMapping("/api/beauty_shape")
    public String beauty_shape(@RequestBody Map<String, String> params){
        String imgStr = params.get("imgStr");
        String fileName = params.get("fileName");
        Integer userId = Integer.parseInt(params.get("userId"));


        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try{
            URL url = new URL("https://openapi.mtlab.meitu.com/v1/beauty_shape?api_key=89f67c2b65374dc79eb311377335d71e&api_secret=b120fcf85ba8467b805fdd8fc46c2b5b");
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
                System.out.println(line);
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
        String newFilename = "beauty_shape-"+ fileName;
        String newPath = FileUtil.GetFrontEndPath(newFilename);
        JsonObject obj = GsonUtils.fromJson(result.toString(), JsonObject.class);
        JsonArray media_info_list = obj.getAsJsonArray("media_info_list");
        JsonObject media_info = (JsonObject) media_info_list.get(0);
        String base64NewImg = GsonUtils.toJson(media_info.get("media_data"));
        Renew.GenerateImage(base64NewImg, newPath);
        Photo newPhoto = new Photo();
        newPhoto.setUserId(userId);
        newPhoto.setPhotoPath(newPath);
        newPhoto.setPhotoName(newFilename);
        photoDao.savePhoto(newPhoto);
        return newFilename;
    }

    @PostMapping("/api/filter")
    public String filter(@RequestBody Map<String, String> params){
        String imgStr = params.get("imgStr");
        String fileName = params.get("fileName");
        Integer userId = Integer.parseInt(params.get("userId"));
        Integer filter_id = Integer.parseInt(params.get("filter_id"));
        String filter_str = filters.get(filter_id);
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try{
            URL url = new URL("https://openapi.mtlab.meitu.com/v1/filter?api_key=89f67c2b65374dc79eb311377335d71e&api_secret=b120fcf85ba8467b805fdd8fc46c2b5b");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setConnectTimeout(30000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");

            out = new OutputStreamWriter(conn.getOutputStream());
            String jsonStr = "{\"parameter\":{\"filterType\": \"" + filter_str + "\",\"filterAlpha\": 70,\"beautyAlpha\":70},\"extra\":{},\"media_info_list\":[{\"media_data\":\""+ imgStr +"\",\"media_profiles\":{\"media_data_type\":\"jpg\"}}]}";
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
                System.out.println(line);
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
        String newFilename = "filter-"+ fileName;
        String newPath = FileUtil.GetFrontEndPath(newFilename);
        JsonObject obj = GsonUtils.fromJson(result.toString(), JsonObject.class);
        JsonArray media_info_list = obj.getAsJsonArray("media_info_list");
        JsonObject media_info = (JsonObject) media_info_list.get(0);
        String base64NewImg = GsonUtils.toJson(media_info.get("media_data"));
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
