package com.example.springbootexample.dao;

import com.example.springbootexample.model.Photo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PhotoDao {
    void savePhoto(Photo photo);
}
