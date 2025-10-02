package com.example.app.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.app.enumvalue.Status;

public class PrincipalPortalInfo {
	// PAYMENT
	public static class PaymentStatusUpdateRequest {
		private String status;
		private String reason;

		public PaymentStatusUpdateRequest() {
		}

		public PaymentStatusUpdateRequest(String status, String reason) {
			this.status = status;
			this.reason = reason;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}
	}

	public static class PaymentStatusUpdateResponse {
		private boolean success;
		private String message;
		private PaymentDTO paymentDTO;

		public PaymentStatusUpdateResponse() {
		}

		public PaymentStatusUpdateResponse(boolean success, String message, PaymentDTO paymentDTO) {
			this.success = success;
			this.message = message;
			this.paymentDTO = paymentDTO;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public PaymentDTO getPayment() {
			return paymentDTO;
		}

		public void setPayment(PaymentDTO paymentDTO) {
			this.paymentDTO = paymentDTO;
		}
	}

	public static class PaymentStatistics {
		private long totalPayments;
		private long paidPayments;
		private long pendingPayments;
		private long failedPayments;
		private double totalAmount;
		private double paidAmount;
		private double pendingAmount;

		public PaymentStatistics() {
		}

		public PaymentStatistics(long totalPayments, long paidPayments, long pendingPayments, long failedPayments,
				double totalAmount, double paidAmount, double pendingAmount) {
			this.totalPayments = totalPayments;
			this.paidPayments = paidPayments;
			this.pendingPayments = pendingPayments;
			this.failedPayments = failedPayments;
			this.totalAmount = totalAmount;
			this.paidAmount = paidAmount;
			this.pendingAmount = pendingAmount;
		}

		// Getters and setters
		public long getTotalPayments() {
			return totalPayments;
		}

		public void setTotalPayments(long totalPayments) {
			this.totalPayments = totalPayments;
		}

		public long getPaidPayments() {
			return paidPayments;
		}

		public void setPaidPayments(long paidPayments) {
			this.paidPayments = paidPayments;
		}

		public long getPendingPayments() {
			return pendingPayments;
		}

		public void setPendingPayments(long pendingPayments) {
			this.pendingPayments = pendingPayments;
		}

		public long getFailedPayments() {
			return failedPayments;
		}

		public void setFailedPayments(long failedPayments) {
			this.failedPayments = failedPayments;
		}

		public double getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(double totalAmount) {
			this.totalAmount = totalAmount;
		}

		public double getPaidAmount() {
			return paidAmount;
		}

		public void setPaidAmount(double paidAmount) {
			this.paidAmount = paidAmount;
		}

		public double getPendingAmount() {
			return pendingAmount;
		}

		public void setPendingAmount(double pendingAmount) {
			this.pendingAmount = pendingAmount;
		}
	}

	public static class PaymentDetailResponse {
		private Long id;
		private Long studentId;
		private String studentName;
		private String studentClass;
		private Long semesterId;
		private String semesterName;
		private String paymentDate;
		private String status;
		private List<CoursePaymentDetail> courses;
		private double totalAmount;

		public PaymentDetailResponse() {
		}

		public PaymentDetailResponse(Long id, Long studentId, String studentName, String studentClass, Long semesterId,
				String semesterName, String paymentDate, String status, List<CoursePaymentDetail> courses,
				double totalAmount) {
			this.id = id;
			this.studentId = studentId;
			this.studentName = studentName;
			this.studentClass = studentClass;
			this.semesterId = semesterId;
			this.semesterName = semesterName;
			this.paymentDate = paymentDate;
			this.status = status;
			this.courses = courses;
			this.totalAmount = totalAmount;
		}

		// Getters and setters
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

		public String getStudentName() {
			return studentName;
		}

		public void setStudentName(String studentName) {
			this.studentName = studentName;
		}

		public String getStudentClass() {
			return studentClass;
		}

		public void setStudentClass(String studentClass) {
			this.studentClass = studentClass;
		}

		public Long getSemesterId() {
			return semesterId;
		}

		public void setSemesterId(Long semesterId) {
			this.semesterId = semesterId;
		}

		public String getSemesterName() {
			return semesterName;
		}

		public void setSemesterName(String semesterName) {
			this.semesterName = semesterName;
		}

		public String getPaymentDate() {
			return paymentDate;
		}

		public void setPaymentDate(String paymentDate) {
			this.paymentDate = paymentDate;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public List<CoursePaymentDetail> getCourses() {
			return courses;
		}

		public void setCourses(List<CoursePaymentDetail> courses) {
			this.courses = courses;
		}

		public double getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(double totalAmount) {
			this.totalAmount = totalAmount;
		}
	}

	public static class CoursePaymentDetail {
		private Long courseId;
		private String courseCode;
		private String courseName;
		private Integer credits;
		private double fee;

		public CoursePaymentDetail() {
		}

		public CoursePaymentDetail(Long courseId, String courseCode, String courseName, Integer credits, double fee) {
			this.courseId = courseId;
			this.courseCode = courseCode;
			this.courseName = courseName;
			this.credits = credits;
			this.fee = fee;
		}

		// Getters and setters
		public Long getCourseId() {
			return courseId;
		}

		public void setCourseId(Long courseId) {
			this.courseId = courseId;
		}

		public String getCourseCode() {
			return courseCode;
		}

		public void setCourseCode(String courseCode) {
			this.courseCode = courseCode;
		}

		public String getCourseName() {
			return courseName;
		}

		public void setCourseName(String courseName) {
			this.courseName = courseName;
		}

		public Integer getCredits() {
			return credits;
		}

		public void setCredits(Integer credits) {
			this.credits = credits;
		}

		public double getFee() {
			return fee;
		}

		public void setFee(double fee) {
			this.fee = fee;
		}
	}

	public static class PaymentWithDetails {
		private Long id;
		private Long studentId;
		private String studentCode;
		private Long semesterId;
		private String semesterName;
		private LocalDateTime paymentDate;
		private Status status;

		public PaymentWithDetails() {
		}

		// Getters and setters
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

		public String getStudentCode() {
			return studentCode;
		}

		public void setStudentCode(String studentCode) {
			this.studentCode = studentCode;
		}

		public Long getSemesterId() {
			return semesterId;
		}

		public void setSemesterId(Long semesterId) {
			this.semesterId = semesterId;
		}

		public String getSemesterName() {
			return semesterName;
		}

		public void setSemesterName(String semesterName) {
			this.semesterName = semesterName;
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

	// ENROLLMENT
	public static class ScholarshipCandidate {
		private Long studentId;
		private String studentCode;
		private String fullName;
		private String className;
		private String departmentName;
		private Double gpa;
		private Integer totalCredits;
		private Integer completedCredits;
		private String semester;
		private Integer rank;

		public ScholarshipCandidate() {
		}

		public ScholarshipCandidate(Long studentId, String studentCode, String fullName, String className,
				String departmentName, Double gpa, Integer totalCredits, Integer completedCredits, String semester,
				Integer rank) {
			this.studentId = studentId;
			this.studentCode = studentCode;
			this.fullName = fullName;
			this.className = className;
			this.departmentName = departmentName;
			this.gpa = gpa;
			this.totalCredits = totalCredits;
			this.completedCredits = completedCredits;
			this.semester = semester;
			this.rank = rank;
		}

		// Getters and Setters
		public Long getStudentId() {
			return studentId;
		}

		public void setStudentId(Long studentId) {
			this.studentId = studentId;
		}

		public String getStudentCode() {
			return studentCode;
		}

		public void setStudentCode(String studentCode) {
			this.studentCode = studentCode;
		}

		public String getFullName() {
			return fullName;
		}

		public void setFullName(String fullName) {
			this.fullName = fullName;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getDepartmentName() {
			return departmentName;
		}

		public void setDepartmentName(String departmentName) {
			this.departmentName = departmentName;
		}

		public Double getGpa() {
			return gpa;
		}

		public void setGpa(Double gpa) {
			this.gpa = gpa;
		}

		public Integer getTotalCredits() {
			return totalCredits;
		}

		public void setTotalCredits(Integer totalCredits) {
			this.totalCredits = totalCredits;
		}

		public Integer getCompletedCredits() {
			return completedCredits;
		}

		public void setCompletedCredits(Integer completedCredits) {
			this.completedCredits = completedCredits;
		}

		public String getSemester() {
			return semester;
		}

		public void setSemester(String semester) {
			this.semester = semester;
		}

		public Integer getRank() {
			return rank;
		}

		public void setRank(Integer rank) {
			this.rank = rank;
		}
	}
}
