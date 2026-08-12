package com.hoang.employeemanagement.controller;

import com.hoang.employeemanagement.dto.AuthRequest;
import com.hoang.employeemanagement.dto.AuthResponse;
import com.hoang.employeemanagement.model.User;
import com.hoang.employeemanagement.security.JwtUtil;
import com.hoang.employeemanagement.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> dangKy(@Valid @RequestBody AuthRequest request) {
        logger.info("Registration request: username={}", request.getUsername());

        User user = userService.dangKy(request.getUsername(), request.getPassword(), request.getRole());

        if (user == null) {
            logger.warn("Registration failed: username already exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse("Username already exists"));
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        logger.info("User registered successfully: username={}, role={}", user.getUsername(), user.getRole());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(
                        "Registration successful",
                        user.getUsername(),
                        user.getRole(),
                        token
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> dangNhap(@Valid @RequestBody AuthRequest request) {
        logger.info("Login request: username={}", request.getUsername());

        if (!userService.xacThucMatKhau(request.getUsername(), request.getPassword())) {
            logger.warn("Login failed: invalid credentials for username={}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Invalid username or password"));
        }

        Optional<User> userOpt = userService.timUserTheoUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            logger.warn("User not found after authentication: username={}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("User not found"));
        }

        User user = userOpt.get();
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        logger.info("User logged in successfully: username={}, role={}", user.getUsername(), user.getRole());

        return ResponseEntity.ok(new AuthResponse(
                "Login successful",
                user.getUsername(),
                user.getRole(),
                token
        ));
    }
}
