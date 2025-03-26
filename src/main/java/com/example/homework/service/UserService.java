package com.example.homework.service;

import com.example.homework.model.User;

public interface UserService {
    public boolean existsByUsername(String username);
    public void saveUser(User user);
}
