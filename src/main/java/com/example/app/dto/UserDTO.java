package com.example.app.dto;

import com.example.app.enumvalue.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDTO {

	private Gender gender;

	private Long id;

	@NotBlank(message = "Username không được để trống")
	@Size(min = 3, max = 20, message = "Username phải từ 3-20 ký tự")
	private String username;

	@NotBlank(message = "Password không được để trống")
	@Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
	private String password;

	@NotBlank(message = "Họ tên không được để trống")
	@Size(max = 100, message = "Họ tên không được quá 100 ký tự")
	private String fullName;

	@NotBlank(message = "Email không được để trống")
	@Email(message = "Email không hợp lệ")
	private String email;

	@NotNull(message = "Role không được để trống")
	private Long roleId;

	@Size(max = 20, message = "Số điện thoại không được quá 20 ký tự")
	private String phone;

	private Long departmentId;

	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Ngày sinh phải có định dạng YYYY-MM-DD")
	private String dateOfBirth;

	@Size(max = 255, message = "Địa chỉ không được quá 255 ký tự")
	private String address;

	// ===== Constructors =====
	public UserDTO() {
	}

	public UserDTO(Long id, String username, String password, String fullName, String email, Long roleId, Gender gender,
			String phone, Long departmentId, String dateOfBirth, String address) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.fullName = fullName;
		this.email = email;
		this.roleId = roleId;
		this.gender = gender;
		this.phone = phone;
		this.departmentId = departmentId;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
	}

	// ===== Getters & Setters =====
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
