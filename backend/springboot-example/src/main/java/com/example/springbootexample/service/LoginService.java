package com.example.springbootexample.service;

import com.example.springbootexample.dao.UserDao;
import com.example.springbootexample.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoginService {
    final
    UserDao userDao;

    public LoginService(UserDao userDao) {
        this.userDao = userDao;
    }

    public String reg(String username, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String encodePassword = encoder.encode(password);

        if(userDao.findDistinctByUsername(username)!=null){
            return "usernameAlready";
        }

        User user = new User();
        user.setCreateTime(new Date());
        user.setUsername(username);
        user.setPassword(encodePassword);
        userDao.save(user);

        return "success";
    }

    public String login(String username, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String encodePassword = encoder.encode(password);
        User user = userDao.findDistinctByUsername(username);
        if (user == null) {
            return "unregistered";
        }
        if (user.getPassword().equals(encodePassword)) {
            user.setLastLoginTime(new Date());
            userDao.save(user);
            return "success";
        } else {
            return "wrong";
        }
    }
}
