package com.hoang.employeemanagement.service;

import com.hoang.employeemanagement.model.User;
import com.hoang.employeemanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User dangKy(String username, String password, String role) {
        logger.info("Registering new user: username={}, role={}", username, role);

        if (userRepository.findByUsername(username).isPresent()) {
            logger.warn("Username already exists: username={}", username);
            return null;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role != null ? role : "USER");

        User savedUser = userRepository.save(user);
        logger.info("User registered successfully: username={}, role={}", savedUser.getUsername(), savedUser.getRole());
        return savedUser;
    }

    public Optional<User> timUserTheoUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean xacThucMatKhau(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            logger.warn("User not found for authentication: username={}", username);
            return false;
        }

        User user = userOpt.get();
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        logger.info("Password authentication: username={}, result={}", username, matches);
        return matches;
    }
}
