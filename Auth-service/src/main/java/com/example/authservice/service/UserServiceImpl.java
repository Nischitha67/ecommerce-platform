package com.example.authservice.service;

import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(String username, String password, String role) {
        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists: " + username);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user);
    }
}
