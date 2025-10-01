package com.example.app.dto;

import java.time.LocalDateTime;

// Tạo khối thông tin token (được dùng trong AuthController)
public class AuthResponse {
	private String token;
	private String tokenType = "Bearer";
	private LocalDateTime expiresAt;
	private String username;
	private String role;

	public AuthResponse() {
	}

	public AuthResponse(String token) {
		this.token = token;
	}

	public AuthResponse(String token, String username, String role, LocalDateTime expiresAt) {
		this.token = token;
		this.username = username;
		this.role = role;
		this.expiresAt = expiresAt;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
