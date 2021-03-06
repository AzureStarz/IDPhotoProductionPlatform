package com.example.springbootexample.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {
    @Override
    //qxk 跨域访问
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("*")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins("http://localhost:80","http://localhost:81","http://192.168.48.105/");
    }
}
