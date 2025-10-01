package com.example.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lecturers")
public class Lecturer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "lecturer_code", nullable = false)
	private String lecturerCode;

	public Lecturer(Long id, Long userId, String lecturerCode) {
		this.id = id;
		this.userId = userId;
		this.lecturerCode = lecturerCode;
	}

	public Lecturer() {
	}

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
}