package com.example.springbootexample.controller;

import com.example.springbootexample.service.LoginService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserManagementController {
    final
    LoginService loginService;

    public UserManagementController(LoginService loginService) {
        this.loginService = loginService;
    }
    @PostMapping("/api/register")
    public String register(@Param("username") String username, @Param("password") String password) {
        return loginService.reg(username, password);
    }
}
