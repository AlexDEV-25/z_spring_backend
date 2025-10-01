package com.example.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.AuthResponse;
import com.example.app.dto.LoginRequest;
import com.example.app.dto.RegisterRequest;
import com.example.app.model.User;
import com.example.app.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	private AuthService authService;

	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	public AuthController() {
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		logger.info("Login attempt for user: {}", request.getUsername());
		AuthResponse response = authService.authenticate(request.getUsername(), request.getPassword());
		return ResponseEntity.ok(response);
//		ex
//		{
//		    "token": "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiSEnhu4ZVX1RSxq_hu55ORyIsInN1YiI6ImhpZXV0cnVvbmciLCJpYXQiOjE3NTg5NzEzMTMsImV4cCI6MTc1OTU3NjExM30.wz_Zj7VWqJ6LCVWWY_gn9prV7bylvzobusOvkm1wPu3MVEdXl6lhlbGTM0OYcnTZ5KwzEKILDjecico7iAJUvA",
//		    "tokenType": "Bearer",
//		    "expiresAt": "2025-10-04T18:08:33",
//		    "username": "hieutruong",
//		    "role": "HIỆU_TRƯỞNG"
//		}
	}

	@PostMapping("/register")
	public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
		logger.info("Registration attempt for user: {}", request.getUsername());
		User user = authService.register(request);
		return ResponseEntity.status(201).body(user);
	}
}