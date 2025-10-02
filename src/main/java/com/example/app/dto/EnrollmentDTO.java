package com.example.app.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class EnrollmentDTO {

	private Long id;

	@NotNull(message = "Student ID không được để trống")
	private Long studentId;

	@NotNull(message = "Course ID không được để trống")
	private Long courseId;

	@Pattern(regexp = "^[A-F][+-]?$|^[0-9](\\.[0-9])?$", message = "Điểm phải có định dạng A+, A, B+, B, C+, C, D+, D, F hoặc số thập phân (0.0 - 9.9)")
	private String grade;

	@Min(value = 0, message = "Điểm thành phần 1 phải >= 0")
	@Max(value = 10, message = "Điểm thành phần 1 phải <= 10")
	private Double componentScore1;

	@Min(value = 0, message = "Điểm thành phần 2 phải >= 0")
	@Max(value = 10, message = "Điểm thành phần 2 phải <= 10")
	private Double componentScore2;

	@Min(value = 0, message = "Điểm thi cuối kỳ phải >= 0")
	@Max(value = 10, message = "Điểm thi cuối kỳ phải <= 10")
	private Double finalExamScore;

	@Min(value = 0, message = "Điểm tổng kết phải >= 0")
	@Max(value = 10, message = "Điểm tổng kết phải <= 10")
	private Double totalScore;

	@Min(value = 0, message = "Điểm hệ số 4 phải >= 0")
	@Max(value = 4, message = "Điểm hệ số 4 phải <= 4")
	private Double scoreCoefficient4;

	// Constructors
	public EnrollmentDTO() {
	}

	public EnrollmentDTO(Long id, Long studentId, Long courseId, String grade, Double componentScore1,
			Double componentScore2, Double finalExamScore, Double totalScore, Double scoreCoefficient4) {
		this.id = id;
		this.studentId = studentId;
		this.courseId = courseId;
		this.grade = grade;
		this.componentScore1 = componentScore1;
		this.componentScore2 = componentScore2;
		this.finalExamScore = finalExamScore;
		this.totalScore = totalScore;
		this.scoreCoefficient4 = scoreCoefficient4;
	}

	// Getter & Setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Double getComponentScore1() {
		return componentScore1;
	}

	public void setComponentScore1(Double componentScore1) {
		this.componentScore1 = componentScore1;
	}

	public Double getComponentScore2() {
		return componentScore2;
	}

	public void setComponentScore2(Double componentScore2) {
		this.componentScore2 = componentScore2;
	}

	public Double getFinalExamScore() {
		return finalExamScore;
	}

	public void setFinalExamScore(Double finalExamScore) {
		this.finalExamScore = finalExamScore;
	}

	public Double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}

	public Double getScoreCoefficient4() {
		return scoreCoefficient4;
	}

	public void setScoreCoefficient4(Double scoreCoefficient4) {
		this.scoreCoefficient4 = scoreCoefficient4;
	}
}
