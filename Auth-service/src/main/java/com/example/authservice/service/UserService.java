package com.example.authservice.service;

import com.example.authservice.model.User;

public interface UserService {
    User createUser(String username, String password, String role);
}
