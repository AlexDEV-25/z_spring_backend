package com.example.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "enrollments")
public class Enrollment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "student_id", nullable = true)
	private Long studentId;

	@Column(name = "course_id", nullable = true)
	private Long courseId;

	@Column(name = "grade")
	private String grade;

	@Column(name = "component_score_1")
	private Double componentScore1;

	@Column(name = "component_score_2")
	private Double componentScore2;

	@Column(name = "final_exam_score")
	private Double finalExamScore;

	@Column(name = "total_score")
	private Double totalScore;

	@Column(name = "score_coefficient_4")
	private Double scoreCoefficient4;

	public Enrollment(Long id, Long studentId, Long courseId, String grade, Double componentScore1,
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

	public Enrollment() {
	}

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