package com.example.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// không cần thiết có thể xóa
public class RegisterRequest {
	@NotBlank(message = "Tên đăng nhập không được để trống")
	@Size(min = 3, max = 20, message = "Tên đăng nhập phải từ 3-20 ký tự")
	private String username;

	@NotBlank(message = "Mật khẩu không được để trống")
	@Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
	private String password;

	@NotBlank(message = "Họ tên không được để trống")
	@Size(max = 100, message = "Họ tên không được quá 100 ký tự")
	private String fullName;

	@NotBlank(message = "Email không được để trống")
	@Email(message = "Email không hợp lệ")
	private String email;

	@NotNull(message = "Role không được để trống")
	private Long roleId;

	// Constructors
	public RegisterRequest() {
	}

	public RegisterRequest(String username, String password, String fullName, String email, Long roleId) {
		this.username = username;
		this.password = password;
		this.fullName = fullName;
		this.email = email;
		this.roleId = roleId;
	}

	// Getters and Setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}
