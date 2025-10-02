package com.example.app.dto;

import java.util.List;

// thông tin cơ bản của giảng viên
public class TeacherPortalInfo {

	public static class TeacherScheduleInfo {
		private Long teachingId;
		private Long courseId;
		private String courseCode;
		private String courseName;
		private Integer credit;
		private String period;
		private String dayOfWeek;
		private String classroom;
		private Long classId; // added for timetable context
		private String className; // added for timetable context
		private List<StudentInfo> students;

		public TeacherScheduleInfo() {
		}

		public TeacherScheduleInfo(Long teachingId, Long courseId, String courseCode, String courseName, Integer credit,
				String period, String dayOfWeek, String classroom, Long classId, String className,
				List<StudentInfo> students) {
			this.teachingId = teachingId;
			this.courseId = courseId;
			this.courseCode = courseCode;
			this.courseName = courseName;
			this.credit = credit;
			this.period = period;
			this.dayOfWeek = dayOfWeek;
			this.classroom = classroom;
			this.classId = classId;
			this.className = className;
			this.students = students;
		}

		public Long getTeachingId() {
			return teachingId;
		}

		public void setTeachingId(Long teachingId) {
			this.teachingId = teachingId;
		}

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

		public String getClassroom() {
			return classroom;
		}

		public void setClassroom(String classroom) {
			this.classroom = classroom;
		}

		public Long getClassId() {
			return classId;
		}

		public void setClassId(Long classId) {
			this.classId = classId;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public List<StudentInfo> getStudents() {
			return students;
		}

		public void setStudents(List<StudentInfo> students) {
			this.students = students;
		}
	}

	// lấy thông tin sinh viên lớp giảng viên dạy
	public static class StudentInfo {
		private Long studentId;
		private String studentCode;
		private String fullName;
		private String email;
		private String className;
		private String grade;
		private Double componentScore1;
		private Double componentScore2;
		private Double finalExamScore;
		private Double totalScore;
		private Double scoreCoefficient4;

		public StudentInfo() {
		}

		// Backward-compatible constructor (no component scores)
		public StudentInfo(Long studentId, String studentCode, String fullName, String email, String className,
				String grade) {
			this.studentId = studentId;
			this.studentCode = studentCode;
			this.fullName = fullName;
			this.email = email;
			this.className = className;
			this.grade = grade;

		}

		// Extended constructor (with component scores)
		public StudentInfo(Long studentId, String studentCode, String fullName, String email, String className,
				String grade, Double componentScore1, Double componentScore2, Double finalExamScore, Long semesterId) {
			this(studentId, studentCode, fullName, email, className, grade);
			this.componentScore1 = componentScore1;
			this.componentScore2 = componentScore2;
			this.finalExamScore = finalExamScore;
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

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
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

	// DTO cho thông tin cá nhân giảng viên
	public static class TeacherProfile {
		private Long lecturerId;
		private String lecturerCode;
		private String fullName;
		private String email;
		private String phone;
		private String department;
		private String position;
		private String specialization;

		// Constructors
		public TeacherProfile() {
		}

		public TeacherProfile(Long lecturerId, String lecturerCode, String fullName, String email, String phone,
				String department, String position, String specialization) {
			this.lecturerId = lecturerId;
			this.lecturerCode = lecturerCode;
			this.fullName = fullName;
			this.email = email;
			this.phone = phone;
			this.department = department;
			this.position = position;
			this.specialization = specialization;
		}

		// Getters and Setters
		public Long getLecturerId() {
			return lecturerId;
		}

		public void setLecturerId(Long lecturerId) {
			this.lecturerId = lecturerId;
		}

		public String getLecturerCode() {
			return lecturerCode;
		}

		public void setLecturerCode(String lecturerCode) {
			this.lecturerCode = lecturerCode;
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

		public String getDepartment() {
			return department;
		}

		public void setDepartment(String department) {
			this.department = department;
		}

		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
		}

		public String getSpecialization() {
			return specialization;
		}

		public void setSpecialization(String specialization) {
			this.specialization = specialization;
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
}