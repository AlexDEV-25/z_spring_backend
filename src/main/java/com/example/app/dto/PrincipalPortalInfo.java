package com.example.app.dto;

import java.time.LocalDateTime;
import java.util.List;

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

	// chi tiết payment
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

	// chi tiết học phần đã học
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
			return this.gpa != null && this.gpa >= 3.6;
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
}
