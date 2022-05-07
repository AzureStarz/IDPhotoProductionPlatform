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

    public String reg(String username, String password, String email) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String encodePassword = encoder.encode(password);

        if(userDao.findDistinctByUsername(username)!=null){
            return "usernameAlready";
        }

        User user = new User();
        user.setCreateTime(new Date());
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodePassword);
        userDao.save(user);

        return "success";
    }

}
