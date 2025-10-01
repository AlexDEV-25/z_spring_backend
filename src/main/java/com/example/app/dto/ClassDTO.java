package com.example.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ClassDTO {

	private Long id;

	@NotBlank(message = "Tên lớp không được để trống")
	private String name;

	@NotBlank(message = "Enter year range (e.g., 2022-2023)...")
	@Pattern(regexp = "^[0-9]{4}-[0-9]{4}$", message = "phải đúng định dạng YYYY-YYYY")
	private String year;

	// Constructors
	public ClassDTO() {
	}

	public ClassDTO(Long id, String name, String year) {
		this.id = id;
		this.name = name;
		this.year = year;
	}

	// Getter & Setter
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

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
}
