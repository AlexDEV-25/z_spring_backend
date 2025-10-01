package com.example.app.model;

//import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "course_code", nullable = false)
	private String courseCode;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "credit", nullable = false)
	private Integer credit;

	@Column(name = "slot", nullable = false)
	private Integer slot;

	@Column(name = "fee", nullable = false)
	private BigDecimal fee;

	@Column(name = "semester_id", nullable = true)
	private Long semesterId;

	public Course(Long id, String courseCode, String name, Integer credit, Integer slot, BigDecimal fee,
			Long semesterId) {
		this.id = id;
		this.courseCode = courseCode;
		this.name = name;
		this.credit = credit;
		this.slot = slot;
		this.fee = fee;
		this.semesterId = semesterId;
	}

	public Course() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	public Integer getSlot() {
		return slot;
	}

	public void setSlot(Integer slot) {
		this.slot = slot;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public Long getSemesterId() {
		return semesterId;
	}

	public void setSemesterId(Long semesterId) {
		this.semesterId = semesterId;
	}
}