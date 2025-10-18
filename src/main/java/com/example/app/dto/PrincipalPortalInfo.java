package com.example.app.dto;

import java.time.LocalDateTime;

import com.example.app.enumvalue.Status;
import com.example.app.model.Student;
import com.example.app.model.User;

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

	// lấy số lượng các payment và tình trạng của payment
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

	// payment
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
		private Boolean eligibleForScholarship; // Đủ điều kiện học bổng (GPA >= 3.6)

		public ScholarshipCandidate() {
		}

		public ScholarshipCandidate(Long studentId, String studentCode, String fullName, String className,
				String departmentName, Double gpa, Integer totalCredits, Integer completedCredits, String semester,
				Boolean eligibleForScholarship) {
			this.studentId = studentId;
			this.studentCode = studentCode;
			this.fullName = fullName;
			this.className = className;
			this.departmentName = departmentName;
			this.gpa = gpa;
			this.totalCredits = totalCredits;
			this.completedCredits = completedCredits;
			this.semester = semester;
			this.eligibleForScholarship = eligibleForScholarship;
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

		public Boolean getEligibleForScholarship() {
			return eligibleForScholarship;
		}

		public void setEligibleForScholarship(Boolean eligibleForScholarship) {
			this.eligibleForScholarship = eligibleForScholarship;
		}

		// Helper method để kiểm tra điều kiện học bổng
		public boolean isEligibleForScholarship() {
			return this.gpa != null && this.gpa >= 3.6 && this.totalCredits != null && this.totalCredits >= 18;
		}
	}

	// Helper class để tính GPA
	public static class StudentGpaInfo {
		private Long studentId;
		private String studentCode;
		private String fullName;
		private Long departmentId;
		private Long classId;
		private String semester;
		private double totalGpaWeightedSum = 0; // Tổng (điểm hệ số 4 * số tín chỉ)
		private int totalCredits = 0;
		private int completedCredits = 0;

		public void addScore(double scoreCoefficient4, int credits, double totalScore) {
			totalGpaWeightedSum += scoreCoefficient4 * credits;
			totalCredits += credits;
			// Điểm qua môn >= 2.0 (thang điểm 4)
			if (totalScore >= 2.0) {
				completedCredits += credits;
			}
		}

		public double getGpa() {
			if (totalCredits <= 0)
				return 0.0;
			return totalGpaWeightedSum / totalCredits;
		}

		public void setStudentInfo(Student student, User user, String semester) {
			this.studentId = student.getId();
			this.studentCode = student.getStudentCode();
			this.fullName = user.getFullName();
			this.departmentId = user.getDepartmentId();
			this.classId = student.getClassId();
			this.semester = semester;
		}

		// Getters
		public Long getStudentId() {
			return studentId;
		}

		public String getStudentCode() {
			return studentCode;
		}

		public String getFullName() {
			return fullName;
		}

		public Long getDepartmentId() {
			return departmentId;
		}

		public Long getClassId() {
			return classId;
		}

		public String getSemester() {
			return semester;
		}

		public int getTotalCredits() {
			return totalCredits;
		}

		public int getCompletedCredits() {
			return completedCredits;
		}
	}

	// Statistics cho scholarship candidates
	public static class ScholarshipStatistics {
		private int totalCandidates;
		private double averageGPA;
		private double topGPA;
		private double averageCompletionRate;
		private int totalEligibleForScholarship;

		public ScholarshipStatistics() {
		}

		public ScholarshipStatistics(int totalCandidates, double averageGPA, double topGPA,
				double averageCompletionRate, int totalEligibleForScholarship) {
			this.totalCandidates = totalCandidates;
			this.averageGPA = averageGPA;
			this.topGPA = topGPA;
			this.averageCompletionRate = averageCompletionRate;
			this.totalEligibleForScholarship = totalEligibleForScholarship;
		}

		// Getters and Setters
		public int getTotalCandidates() {
			return totalCandidates;
		}

		public void setTotalCandidates(int totalCandidates) {
			this.totalCandidates = totalCandidates;
		}

		public double getAverageGPA() {
			return averageGPA;
		}

		public void setAverageGPA(double averageGPA) {
			this.averageGPA = averageGPA;
		}

		public double getTopGPA() {
			return topGPA;
		}

		public void setTopGPA(double topGPA) {
			this.topGPA = topGPA;
		}

		public double getAverageCompletionRate() {
			return averageCompletionRate;
		}

		public void setAverageCompletionRate(double averageCompletionRate) {
			this.averageCompletionRate = averageCompletionRate;
		}

		public int getTotalEligibleForScholarship() {
			return totalEligibleForScholarship;
		}

		public void setTotalEligibleForScholarship(int totalEligibleForScholarship) {
			this.totalEligibleForScholarship = totalEligibleForScholarship;
		}
	}
}
