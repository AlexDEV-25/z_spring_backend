package com.example.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class StudentDTO {

	private Long id;

	@NotNull(message = "User ID không được để trống")
	private Long userId;

	@NotBlank(message = "Mã sinh viên không được để trống")
	@Pattern(regexp = "^[A-Z0-9]{6,10}$", message = "Mã sinh viên phải có 6-10 ký tự chữ hoa và số")
	private String studentCode;

	private Long classId;

	// Constructors
	public StudentDTO() {
	}

	public StudentDTO(Long id, Long userId, String studentCode, Long classId) {
		this.id = id;
		this.userId = userId;
		this.studentCode = studentCode;
		this.classId = classId;
	}

	// Getter & Setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getStudentCode() {
		return studentCode;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	@Override
	public String toString() {
		return "StudentDTO{" + "id=" + id + ", userId=" + userId + ", studentCode='" + studentCode + '\'' + ", classId="
				+ classId + '}';
	}
}
