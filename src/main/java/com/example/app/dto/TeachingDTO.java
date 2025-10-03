package com.example.app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TeachingDTO {
	private Long id;

	// Cho phép null vì trong entity bạn cũng để nullable = true
	private Long lecturerId;

	@NotNull(message = "Course ID không được để trống")
	private Long courseId;

	@Size(max = 50, message = "Tiết học không được quá 50 ký tự")
	private String period;

	@Size(max = 20, message = "Thứ trong tuần không được quá 20 ký tự")
	private String dayOfWeek;


	@Size(max = 100, message = "Tên phòng học không được quá 100 ký tự")
	private String classRoom;

	// ===== Constructors =====
	public TeachingDTO() {
	}

	public TeachingDTO(Long id, Long lecturerId, Long courseId, String period, String dayOfWeek, String classRoom) {
		this.id = id;
		this.lecturerId = lecturerId;
		this.courseId = courseId;
		this.period = period;
		this.dayOfWeek = dayOfWeek;
		this.classRoom = classRoom;
	}

	// ===== Getters & Setters =====
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLecturerId() {
		return lecturerId;
	}

	public void setLecturerId(Long lecturerId) {
		this.lecturerId = lecturerId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}


	public String getClassRoom() {
		return classRoom;
	}

	public void setClassRoom(String classRoom) {
		this.classRoom = classRoom;
	}
}
