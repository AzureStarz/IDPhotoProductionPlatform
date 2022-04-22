package com.example.springbootexample.controller;

import com.example.springbootexample.service.LoginService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserManagementController {
    final
    LoginService loginService;

    public UserManagementController(LoginService loginService) {
        this.loginService = loginService;
    }
    @PostMapping("/api/register")
    /*public String register(@Param("username") String username, @Param("password") String password) {
        return loginService.reg(username, password);
    }*/
    public String register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        return loginService.reg(username, password);
    }

    @PostMapping("/api/login")
    public String login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        return loginService.login(username, password);
    }
}
