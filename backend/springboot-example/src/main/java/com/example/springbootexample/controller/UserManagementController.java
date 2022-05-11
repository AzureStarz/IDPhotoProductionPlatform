package com.example.springbootexample.controller;

import com.example.springbootexample.model.User;
import com.example.springbootexample.service.LoginService;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserManagementController {
    final
    LoginService loginService;

    public UserManagementController(LoginService loginService) {
        this.loginService = loginService;
    }
    @PostMapping("/api/register")
    public String register(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();
        return loginService.reg(username, password, email);
    }

    @ResponseBody
    public String productInfo(){
        String currentUser = "";
        Object principl = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principl instanceof UserDetails) {
            currentUser = ((UserDetails)principl).getUsername();
        }else {
            currentUser = principl.toString();
        }
        return " some product info,currentUser is: "+currentUser;
    }
}
