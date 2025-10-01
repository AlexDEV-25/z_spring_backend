package com.example.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class LecturerDTO {

	private Long id;

	@NotNull(message = "User ID không được để trống")
	private Long userId;

	@NotBlank(message = "Mã giảng viên không được để trống")
	@Pattern(regexp = "^[A-Z0-9]{4,8}$", message = "Mã giảng viên phải có 4-8 ký tự chữ hoa và số")
	private String lecturerCode;

	// Constructors
	public LecturerDTO() {
	}

	public LecturerDTO(Long id, Long userId, String lecturerCode) {
		this.id = id;
		this.userId = userId;
		this.lecturerCode = lecturerCode;
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

	public String getLecturerCode() {
		return lecturerCode;
	}

	public void setLecturerCode(String lecturerCode) {
		this.lecturerCode = lecturerCode;
	}

	@Override
	public String toString() {
		return "LecturerDTO{" + "id=" + id + ", userId=" + userId + ", lecturerCode='" + lecturerCode + '\'' + '}';
	}

	// DTO với thông tin đầy đủ cho teachings
	public static class LecturerWithDetailsDTO extends LecturerDTO {
		private String fullName;
		private String department;

		public LecturerWithDetailsDTO() {
			super();
		}

		public LecturerWithDetailsDTO(Long id, Long userId, String lecturerCode, String fullName, String department) {
			super(id, userId, lecturerCode);
			this.fullName = fullName;
			this.department = department;
		}

		public String getFullName() {
			return fullName;
		}

		public void setFullName(String fullName) {
			this.fullName = fullName;
		}

		public String getDepartment() {
			return department;
		}

		public void setDepartment(String department) {
			this.department = department;
		}
	}
}
