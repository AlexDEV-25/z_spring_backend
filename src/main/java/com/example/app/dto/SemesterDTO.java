package com.example.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class SemesterDTO {

	private Long id;

	@NotBlank(message = "Học kỳ không được để trống")
	@Pattern(regexp = "^[0-9]{4}-[0-9]{1}$", message = "Học kỳ phải có định dạng YYYY-X (ví dụ: 2024-1)")
	private String semester;

	// Constructors
	public SemesterDTO() {
	}

	public SemesterDTO(Long id, String semester) {
		this.id = id;
		this.semester = semester;
	}

	// Getter & Setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	@Override
	public String toString() {
		return "SemesterDTO{" + "id=" + id + ", semester='" + semester + '\'' + '}';
	}
}
