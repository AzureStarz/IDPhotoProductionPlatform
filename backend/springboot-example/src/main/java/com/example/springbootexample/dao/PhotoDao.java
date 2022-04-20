package com.example.springbootexample.dao;

import com.example.springbootexample.model.Photo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PhotoDao {
    void savePhoto(Photo photo);

    List<String> getPhotosWithUserId(Integer userId);
}
