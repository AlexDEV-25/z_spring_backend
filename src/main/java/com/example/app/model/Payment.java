package com.example.app.model;

import java.time.LocalDateTime;

import com.example.app.enumvalue.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "student_id", nullable = true)
	private Long studentId;

	@Column(name = "semester_id", nullable = true)
	private Long semesterId;

	@Column(name = "payment_date", nullable = false)
	private LocalDateTime paymentDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private Status status;

	public Payment(Long id, Long studentId, Long semesterId, LocalDateTime paymentDate, Status status) {
		this.id = id;
		this.studentId = studentId;
		this.semesterId = semesterId;
		this.paymentDate = paymentDate;
		this.status = status;
	}

	// Constructors
	public Payment() {
		this.paymentDate = LocalDateTime.now();
		this.status = Status.PENDING;
	}

	public Payment(Long studentId, Long semesterId) {
		this();
		this.studentId = studentId;
		this.semesterId = semesterId;
	}

	public Payment(Long studentId, Long semesterId, Status status) {
		this(studentId, semesterId);
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

	@Override
	public String toString() {
		return "Payment{" + "id=" + id + ", studentId=" + studentId + ", semesterId=" + semesterId + ", paymentDate="
				+ paymentDate + ", status=" + status + '}';
	}
}
