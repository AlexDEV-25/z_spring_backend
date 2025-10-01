package com.example.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

//DTO đơn giản cho Student Portal - tương tự TeacherClassInfo
public class StudentPortalInfo {

	// thông tin thời khóa biểu
	public static class StudentScheduleInfo {
		private Long studentId;
		private String studentCode;
		private String studentName;
		private String semester;
		private Integer totalCredits;
		private List<ScheduleItem> scheduleItems;

		// Constructors
		public StudentScheduleInfo() {
		}

		public StudentScheduleInfo(Long studentId, String studentCode, String studentName, String semester,
				Integer totalCredits, List<ScheduleItem> scheduleItems) {
			this.studentId = studentId;
			this.studentCode = studentCode;
			this.studentName = studentName;
			this.semester = semester;
			this.totalCredits = totalCredits;
			this.scheduleItems = scheduleItems;
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

		public String getStudentName() {
			return studentName;
		}

		public void setStudentName(String studentName) {
			this.studentName = studentName;
		}

		public String getSemester() {
			return semester;
		}

		public void setSemester(String semester) {
			this.semester = semester;
		}

		public Integer getTotalCredits() {
			return totalCredits;
		}

		public void setTotalCredits(Integer totalCredits) {
			this.totalCredits = totalCredits;
		}

		public List<ScheduleItem> getScheduleItems() {
			return scheduleItems;
		}

		public void setScheduleItems(List<ScheduleItem> scheduleItems) {
			this.scheduleItems = scheduleItems;
		}
	}

	// thông tin về lịch học
	public static class ScheduleItem {
		private Long courseId;
		private String courseCode;
		private String courseName;
		private Integer credit;
		private String period;
		private String dayOfWeek;
		private String lecturerName;
		private String className;
		private String room;

		// Constructors
		public ScheduleItem() {
		}

		public ScheduleItem(Long courseId, String courseCode, String courseName, Integer credit, String period,
				String dayOfWeek, String lecturerName, String className, String room) {
			this.courseId = courseId;
			this.courseCode = courseCode;
			this.courseName = courseName;
			this.credit = credit;
			this.period = period;
			this.dayOfWeek = dayOfWeek;
			this.lecturerName = lecturerName;
			this.className = className;
			this.room = room;
		}

		// Getters and Setters
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

		public Integer getCredit() {
			return credit;
		}

		public void setCredit(Integer credit) {
			this.credit = credit;
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

		public String getLecturerName() {
			return lecturerName;
		}

		public void setLecturerName(String lecturerName) {
			this.lecturerName = lecturerName;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getRoom() {
			return room;
		}

		public void setRoom(String room) {
			this.room = room;
		}
	}

	// thông tin về điểm
	public static class StudentGradesInfo {
		private Long studentId;
		private String studentCode;
		private String studentName;
		private Double gpa;
		private Integer totalCredits;
		private Integer completedCredits;
		private List<GradeItem> gradeItems;

		// Constructors
		public StudentGradesInfo() {
		}

		public StudentGradesInfo(Long studentId, String studentCode, String studentName, Double gpa,
				Integer totalCredits, Integer completedCredits, List<GradeItem> gradeItems) {
			this.studentId = studentId;
			this.studentCode = studentCode;
			this.studentName = studentName;
			this.gpa = gpa;
			this.totalCredits = totalCredits;
			this.completedCredits = completedCredits;
			this.gradeItems = gradeItems;
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

		public String getStudentName() {
			return studentName;
		}

		public void setStudentName(String studentName) {
			this.studentName = studentName;
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

		public List<GradeItem> getGradeItems() {
			return gradeItems;
		}

		public void setGradeItems(List<GradeItem> gradeItems) {
			this.gradeItems = gradeItems;
		}
	}

	// điểm của từng môn
	public static class GradeItem {
		private Long courseId;
		private String courseCode;
		private String courseName;
		private Integer credit;
		private Double componentScore1;
		private Double componentScore2;
		private Double finalExamScore;
		private String grade;
		private String semester;
		private String status;

		// Constructors
		public GradeItem() {
		}

		public GradeItem(Long courseId, String courseCode, String courseName, Integer credit, Double componentScore1,
				Double componentScore2, Double finalExamScore, String grade, String semester, String status) {
			this.courseId = courseId;
			this.courseCode = courseCode;
			this.courseName = courseName;
			this.credit = credit;
			this.componentScore1 = componentScore1;
			this.componentScore2 = componentScore2;
			this.finalExamScore = finalExamScore;
			this.grade = grade;
			this.semester = semester;
			this.status = status;
		}

		// Getters and Setters
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

		public Integer getCredit() {
			return credit;
		}

		public void setCredit(Integer credit) {
			this.credit = credit;
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

		public String getGrade() {
			return grade;
		}

		public void setGrade(String grade) {
			this.grade = grade;
		}

		public String getSemester() {
			return semester;
		}

		public void setSemester(String semester) {
			this.semester = semester;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}

	// học phần có thể đăng ký
	public static class AvailableCourseInfo {
		private Long courseId;
		private String courseCode;
		private String courseName;
		private Integer credit;
		private boolean canRegister;
		private String reason;
		private Integer availableSlots;
		private Integer maxSlots;
		private String lecturerName;
		private String period;
		private String dayOfWeek;
		private String classroom;
		private String semester;
		private boolean canUnregister;

		// Constructors
		public AvailableCourseInfo() {
		}

		public AvailableCourseInfo(Long courseId, String courseCode, String courseName, Integer credit,
				boolean canRegister, String reason, Integer availableSlots, Integer maxSlots, String lecturerName,
				String period, String dayOfWeek, String classroom, String semester, boolean canUnregister) {
			this.courseId = courseId;
			this.courseCode = courseCode;
			this.courseName = courseName;
			this.credit = credit;
			this.canRegister = canRegister;
			this.reason = reason;
			this.availableSlots = availableSlots;
			this.maxSlots = maxSlots;
			this.lecturerName = lecturerName;
			this.period = period;
			this.dayOfWeek = dayOfWeek;
			this.classroom = classroom;
			this.semester = semester;
			this.canUnregister = canUnregister;
		}

		// Getters and Setters
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

		public Integer getCredit() {
			return credit;
		}

		public void setCredit(Integer credit) {
			this.credit = credit;
		}

		public boolean isCanRegister() {
			return canRegister;
		}

		public void setCanRegister(boolean canRegister) {
			this.canRegister = canRegister;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}

		public Integer getAvailableSlots() {
			return availableSlots;
		}

		public void setAvailableSlots(Integer availableSlots) {
			this.availableSlots = availableSlots;
		}

		public Integer getMaxSlots() {
			return maxSlots;
		}

		public void setMaxSlots(Integer maxSlots) {
			this.maxSlots = maxSlots;
		}

		public String getLecturerName() {
			return lecturerName;
		}

		public void setLecturerName(String lecturerName) {
			this.lecturerName = lecturerName;
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

		public String getClassroom() {
			return classroom;
		}

		public void setClassroom(String classroom) {
			this.classroom = classroom;
		}

		public String getSemester() {
			return semester;
		}

		public void setSemester(String semester) {
			this.semester = semester;
		}

		public boolean isCanUnregister() {
			return canUnregister;
		}

		public void setCanUnregister(boolean canUnregister) {
			this.canUnregister = canUnregister;
		}
	}

	// đăng ký học phần
	public static class CourseRegistrationRequest {
		private Long courseId;
		private String semester;

		// Constructors
		public CourseRegistrationRequest() {
		}

		public CourseRegistrationRequest(Long courseId, String semester) {
			this.courseId = courseId;
			this.semester = semester;
		}

		// Getters and Setters
		public Long getCourseId() {
			return courseId;
		}

		public void setCourseId(Long courseId) {
			this.courseId = courseId;
		}

		public String getSemester() {
			return semester;
		}

		public void setSemester(String semester) {
			this.semester = semester;
		}
	}

	// phản hồi khi đăng ký học phần
	public static class CourseRegistrationResponse {
		private boolean success;
		private String message;

		// Constructors
		public CourseRegistrationResponse() {
		}

		public CourseRegistrationResponse(boolean success, String message) {
			this.success = success;
			this.message = message;
		}

		// Getters and Setters
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
	}

	// thông tin về semester filtering
	public static class SemesterFilterInfo {
		private String currentSemester;
		private String latestSemester;
		private boolean isFilteredByLatest;

		// Constructors
		public SemesterFilterInfo() {
		}

		public SemesterFilterInfo(String currentSemester, String latestSemester, boolean isFilteredByLatest) {
			this.currentSemester = currentSemester;
			this.latestSemester = latestSemester;
			this.isFilteredByLatest = isFilteredByLatest;
		}

		// Getters and Setters
		public String getCurrentSemester() {
			return currentSemester;
		}

		public void setCurrentSemester(String currentSemester) {
			this.currentSemester = currentSemester;
		}

		public String getLatestSemester() {
			return latestSemester;
		}

		public void setLatestSemester(String latestSemester) {
			this.latestSemester = latestSemester;
		}

		public boolean isFilteredByLatest() {
			return isFilteredByLatest;
		}

		public void setFilteredByLatest(boolean isFilteredByLatest) {
			this.isFilteredByLatest = isFilteredByLatest;
		}
	}

	// thông tin semester cho dropdown
	public static class SemesterInfo {
		private Long id;
		private String semester;
		private String displayName;

		// Constructors
		public SemesterInfo() {
		}

		public SemesterInfo(Long id, String semester, String displayName) {
			this.id = id;
			this.semester = semester;
			this.displayName = displayName;
		}

		// Getters and Setters
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getSemester() {
			return semester;
		}

		public void setSemester(String semester) {
			this.semester = semester;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
	}

	// DTO cho thông tin cá nhân sinh viên
	public static class StudentProfile {
		private Long studentId;
		private String studentCode;
		private String fullName;
		private String email;
		private String phone;
		private String className;
		private String major;
		private String academicYear;

		// Constructors
		public StudentProfile() {
		}

		public StudentProfile(Long studentId, String studentCode, String fullName, String email, String phone,
				String className, String major, String academicYear) {
			this.studentId = studentId;
			this.studentCode = studentCode;
			this.fullName = fullName;
			this.email = email;
			this.phone = phone;
			this.className = className;
			this.major = major;
			this.academicYear = academicYear;
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

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getMajor() {
			return major;
		}

		public void setMajor(String major) {
			this.major = major;
		}

		public String getAcademicYear() {
			return academicYear;
		}

		public void setAcademicYear(String academicYear) {
			this.academicYear = academicYear;
		}
	}

	// DTO cho yêu cầu thay đổi mật khẩu (không cần mật khẩu hiện tại)
	public static class ChangePasswordRequest {
		private String newPassword;
		private String confirmPassword;

		// Constructors
		public ChangePasswordRequest() {
		}

		public ChangePasswordRequest(String newPassword, String confirmPassword) {
			this.newPassword = newPassword;
			this.confirmPassword = confirmPassword;
		}

		// Getters and Setters
		public String getNewPassword() {
			return newPassword;
		}

		public void setNewPassword(String newPassword) {
			this.newPassword = newPassword;
		}

		public String getConfirmPassword() {
			return confirmPassword;
		}

		public void setConfirmPassword(String confirmPassword) {
			this.confirmPassword = confirmPassword;
		}
	}

	// DTO cho response thay đổi mật khẩu
	public static class ChangePasswordResponse {
		private boolean success;
		private String message;

		// Constructors
		public ChangePasswordResponse() {
		}

		public ChangePasswordResponse(boolean success, String message) {
			this.success = success;
			this.message = message;
		}

		// Getters and Setters
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
	}

	// DTO cho thông tin thanh toán học phí
	public static class PaymentInfo {
		private Long semesterId;
		private String semester;
		private String semesterDisplayName;
		private BigDecimal totalAmount;
		private BigDecimal paidAmount;
		private BigDecimal remainingAmount;
		private String paymentStatus;
		private LocalDateTime paymentDate;
		private List<CoursePaymentDetail> courseDetails;

		// Constructors
		public PaymentInfo() {
		}

		public PaymentInfo(Long semesterId, String semester, String semesterDisplayName, BigDecimal totalAmount,
				BigDecimal paidAmount, String paymentStatus, LocalDateTime paymentDate,
				List<CoursePaymentDetail> courseDetails) {
			this.semesterId = semesterId;
			this.semester = semester;
			this.semesterDisplayName = semesterDisplayName;
			this.totalAmount = totalAmount;
			this.paidAmount = paidAmount;
			this.remainingAmount = totalAmount.subtract(paidAmount);
			this.paymentStatus = paymentStatus;
			this.paymentDate = paymentDate;
			this.courseDetails = courseDetails;
		}

		// Getters and Setters
		public Long getSemesterId() {
			return semesterId;
		}

		public void setSemesterId(Long semesterId) {
			this.semesterId = semesterId;
		}

		public String getSemester() {
			return semester;
		}

		public void setSemester(String semester) {
			this.semester = semester;
		}

		public String getSemesterDisplayName() {
			return semesterDisplayName;
		}

		public void setSemesterDisplayName(String semesterDisplayName) {
			this.semesterDisplayName = semesterDisplayName;
		}

		public BigDecimal getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(BigDecimal totalAmount) {
			this.totalAmount = totalAmount;
		}

		public BigDecimal getPaidAmount() {
			return paidAmount;
		}

		public void setPaidAmount(BigDecimal paidAmount) {
			this.paidAmount = paidAmount;
		}

		public BigDecimal getRemainingAmount() {
			return remainingAmount;
		}

		public void setRemainingAmount(BigDecimal remainingAmount) {
			this.remainingAmount = remainingAmount;
		}

		public String getPaymentStatus() {
			return paymentStatus;
		}

		public void setPaymentStatus(String paymentStatus) {
			this.paymentStatus = paymentStatus;
		}

		public LocalDateTime getPaymentDate() {
			return paymentDate;
		}

		public void setPaymentDate(LocalDateTime paymentDate) {
			this.paymentDate = paymentDate;
		}

		public List<CoursePaymentDetail> getCourseDetails() {
			return courseDetails;
		}

		public void setCourseDetails(List<CoursePaymentDetail> courseDetails) {
			this.courseDetails = courseDetails;
		}
	}

	// DTO cho chi tiết thanh toán từng môn học
	public static class CoursePaymentDetail {
		private Long courseId;
		private String courseCode;
		private String courseName;
		private Integer credits;
		private BigDecimal fee;
		private String enrollmentStatus;

		// Constructors
		public CoursePaymentDetail() {
		}

		public CoursePaymentDetail(Long courseId, String courseCode, String courseName, Integer credits, BigDecimal fee,
				String enrollmentStatus) {
			this.courseId = courseId;
			this.courseCode = courseCode;
			this.courseName = courseName;
			this.credits = credits;
			this.fee = fee;
			this.enrollmentStatus = enrollmentStatus;
		}

		// Getters and Setters
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

		public BigDecimal getFee() {
			return fee;
		}

		public void setFee(BigDecimal fee) {
			this.fee = fee;
		}

		public String getEnrollmentStatus() {
			return enrollmentStatus;
		}

		public void setEnrollmentStatus(String enrollmentStatus) {
			this.enrollmentStatus = enrollmentStatus;
		}
	}
}
