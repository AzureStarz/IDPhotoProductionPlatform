package com.example.springbootexample.dao;

import com.example.springbootexample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {
    User findDistinctByUsername(String username);
}
