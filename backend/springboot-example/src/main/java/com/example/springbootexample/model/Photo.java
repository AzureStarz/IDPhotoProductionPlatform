package com.example.springbootexample.model;

import lombok.Data;

@Data
public class Photo {
    private Integer photoID;
    private Integer userId;
    private String photoName;
    private String photoPath;
}
