package com.example.app.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CourseDTO {

	private Long id;

	@NotBlank(message = "Mã môn học không được để trống")
	@Pattern(regexp = "^[A-Z0-9]{4,8}$", message = "Mã môn học phải có 4-8 ký tự chữ hoa và số")
	private String courseCode;

	@NotBlank(message = "Tên môn học không được để trống")
	private String name;

	@NotNull(message = "Số tín chỉ không được để trống")
	@Min(value = 1, message = "Số tín chỉ phải ít nhất là 1")
	@Max(value = 6, message = "Số tín chỉ không được quá 6")
	private Integer credit;

	@Min(value = 0, message = "Số slot phải >= 0")
	private Integer slot;
	@Min(value = 0, message = "Học phí phải >= 0")
	private BigDecimal fee;

	private Long semesterId;

	// Constructors
	public CourseDTO() {
	}

	public CourseDTO(Long id, String courseCode, String name, Integer credit, Integer slot, BigDecimal fee,
			Long semesterId) {
		this.id = id;
		this.courseCode = courseCode;
		this.name = name;
		this.credit = credit;
		this.slot = slot;
		this.fee = fee;
		this.semesterId = semesterId;
	}

	// Getter & Setter
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
