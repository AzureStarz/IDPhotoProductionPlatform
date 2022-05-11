package com.example.springbootexample.controller;

import com.example.springbootexample.dao.UserDao;
import com.example.springbootexample.model.User;
import com.example.springbootexample.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserManagementController {
    final
    LoginService loginService;
    @Autowired
    private UserDao userDao;

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
    @GetMapping("/api/getUser")
    public int productInfo(){
        String currentUser = "";
        int userId = 1;
        Object principl = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        currentUser = ((UserDetails)principl).getUsername();
        User current = this.userDao.findDistinctByUsername(currentUser);
        userId = current.getUserId();
        return userId;
    }
}
