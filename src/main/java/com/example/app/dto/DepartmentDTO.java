package com.example.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DepartmentDTO {

	private Long id;

	@NotBlank(message = "Tên khoa không được để trống")
	@Size(max = 150, message = "Tên khoa không được quá 150 ký tự")
	private String name;

	@NotBlank(message = "Mã khoa không được để trống")
	@Size(max = 50, message = "Mã khoa không được quá 50 ký tự")
	private String code;

	// Constructors
	public DepartmentDTO() {
	}

	public DepartmentDTO(Long id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "DepartmentDTO{" + "id=" + id + ", name='" + name + '\'' + ", code='" + code + '\'' + '}';
	}
}
