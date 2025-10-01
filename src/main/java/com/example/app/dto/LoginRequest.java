package com.example.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// tạo khung JSON được dùng trong (AuthController)
public class LoginRequest {
	@NotBlank(message = "Tên đăng nhập không được để trống")
	@Size(min = 3, max = 20, message = "Tên đăng nhập phải từ 3-20 ký tự")
	private String username;

	@NotBlank(message = "Mật khẩu không được để trống")
	@Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
	private String password;

	// Getter và Setter cho username
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// Getter và Setter cho password
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
