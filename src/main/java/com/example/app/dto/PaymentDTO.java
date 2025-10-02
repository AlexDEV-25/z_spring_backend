package com.example.app.dto;

import java.time.LocalDateTime;

import com.example.app.enumvalue.Status;

public class PaymentDTO {
	private Long id;
	private Long studentId;
	private Long semesterId;
	private LocalDateTime paymentDate;
	private Status status;

	// Constructors
	public PaymentDTO() {
	}

	public PaymentDTO(Long id, Long studentId, Long semesterId, LocalDateTime paymentDate, Status status) {
		this.id = id;
		this.studentId = studentId;
		this.semesterId = semesterId;
		this.paymentDate = paymentDate;
		this.status = status;
	}

	// Getters and Setters
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

	public Long getSemesterId() {
		return semesterId;
	}

	public void setSemesterId(Long semesterId) {
		this.semesterId = semesterId;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
