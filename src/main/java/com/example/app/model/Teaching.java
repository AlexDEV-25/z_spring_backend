package com.example.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "teachings")
public class Teaching {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "lecturer_id", nullable = true)
	private Long lecturerId;

	@Column(name = "course_id", nullable = false)
	private Long courseId;

	@Column(name = "period", nullable = true)
	private String period;

	@Column(name = "day_of_week", nullable = true)
	private String dayOfWeek;

	@Column(name = "class_id", nullable = true)
	private Long classId;

	@Column(name = "classroom", nullable = true)
	private String classRoom;

	public Teaching(Long id, Long lecturerId, Long courseId, String period, String dayOfWeek, Long classId,
			String classRoom) {
		this.id = id;
		this.lecturerId = lecturerId;
		this.courseId = courseId;
		this.period = period;
		this.dayOfWeek = dayOfWeek;
		this.classId = classId;
		this.classRoom = classRoom;
	}

	public Teaching() {
	}

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

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public String getClassRoom() {
		return this.classRoom;
	}

	public void setClassRoom(String classRoom) {
		this.classRoom = classRoom;
	}
}