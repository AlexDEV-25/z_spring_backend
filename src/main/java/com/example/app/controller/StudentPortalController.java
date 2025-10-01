package com.example.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.StudentDTO;
import com.example.app.dto.StudentPortalInfo;
import com.example.app.service.StudentPortalService;
import com.example.app.service.StudentService;

import jakarta.validation.Valid;

/**
 * Student Portal Controller - Đơn giản như TeacherController
 */
@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "http://localhost:4200")
public class StudentPortalController {

	private static final Logger logger = LoggerFactory.getLogger(StudentPortalController.class);

	private final StudentService studentService;
	private final StudentPortalService studentPortalService;

	public StudentPortalController(StudentService studentService, StudentPortalService studentPortalService) {
		this.studentService = studentService;
		this.studentPortalService = studentPortalService;
	}

	/**
	 * Lấy thời khóa biểu của sinh viên hiện tại
	 */
	@GetMapping("/schedule")
	public ResponseEntity<StudentPortalInfo.StudentScheduleInfo> getMySchedule(
			@RequestParam(required = false, defaultValue = "2024-1") String semester) {
		try {
			Long studentId = getCurrentStudentId();
			logger.info("Getting schedule for student ID: {} in semester: {}", studentId, semester);

			StudentPortalInfo.StudentScheduleInfo schedule = studentPortalService.getStudentSchedule(studentId,
					semester);
			return ResponseEntity.ok(schedule);
		} catch (Exception e) {
			logger.error("Error getting student schedule: ", e);
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * Lấy bảng điểm của sinh viên hiện tại theo semester
	 */
	@GetMapping("/grades")
	public ResponseEntity<StudentPortalInfo.StudentGradesInfo> getMyGrades(
			@RequestParam(required = false) String semester) {
		try {
			Long studentId = getCurrentStudentId();
			logger.info("Getting grades for student ID: {} in semester: {}", studentId, semester);

			StudentPortalInfo.StudentGradesInfo grades = studentPortalService.getStudentGrades(studentId, semester);
			return ResponseEntity.ok(grades);
		} catch (Exception e) {
			logger.error("Error getting student grades: ", e);
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * Đăng ký môn học
	 */
	@PostMapping("/register-course")
	public ResponseEntity<StudentPortalInfo.CourseRegistrationResponse> registerCourse(
			@Valid @RequestBody StudentPortalInfo.CourseRegistrationRequest request) {
		try {
			Long studentId = getCurrentStudentId();
			logger.info("Registering course {} for student ID: {}", request.getCourseId(), studentId);

			StudentPortalInfo.CourseRegistrationResponse response = studentPortalService.registerCourse(studentId,
					request.getCourseId(), request.getSemester());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error registering course: ", e);
			return ResponseEntity.badRequest().body(
					new StudentPortalInfo.CourseRegistrationResponse(false, "Lỗi đăng ký môn học: " + e.getMessage()));
		}
	}

	/**
	 * Lấy danh sách môn học có thể đăng ký
	 */
	@GetMapping("/available-courses")
	public ResponseEntity<List<StudentPortalInfo.AvailableCourseInfo>> getAvailableCourses(
			@RequestParam(defaultValue = "2024-1") String semester) {
		try {
			Long studentId = getCurrentStudentId();
			logger.info("Getting available courses for student ID: {} in semester: {}", studentId, semester);

			List<StudentPortalInfo.AvailableCourseInfo> courses = studentPortalService.getAvailableCourses(studentId,
					semester);
			return ResponseEntity.ok(courses);
		} catch (Exception e) {
			logger.error("Error getting available courses: ", e);
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * Hủy đăng ký môn học
	 */
	@DeleteMapping("/courses/{courseId}")
	public ResponseEntity<StudentPortalInfo.CourseRegistrationResponse> unregisterCourse(@PathVariable Long courseId) {
		try {
			Long studentId = getCurrentStudentId();
			logger.info("Unregistering course {} for student ID: {}", courseId, studentId);

			StudentPortalInfo.CourseRegistrationResponse response = studentPortalService.unregisterCourse(studentId,
					courseId);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error unregistering course: ", e);
			return ResponseEntity.badRequest().body(new StudentPortalInfo.CourseRegistrationResponse(false,
					"Lỗi hủy đăng ký môn học: " + e.getMessage()));
		}
	}

	/**
	 * Lấy danh sách tất cả semesters từ database
	 */
	@GetMapping("/semesters")
	public ResponseEntity<List<StudentPortalInfo.SemesterInfo>> getAllSemesters() {
		try {
			List<StudentPortalInfo.SemesterInfo> semesters = studentPortalService.getAllSemesters();
			logger.info("Retrieved {} semesters", semesters.size());
			return ResponseEntity.ok(semesters);
		} catch (Exception e) {
			logger.error("Error getting semesters: ", e);
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * Lấy semester mới nhất đang được sử dụng
	 */
	@GetMapping("/latest-semester")
	public ResponseEntity<String> getLatestSemester() {
		try {
			// Lấy semester mới nhất từ service (sẽ tự động lấy từ database)
			String latestSemester = studentPortalService.getLatestSemesterInfo();
			logger.info("Latest semester: {}", latestSemester);
			return ResponseEntity.ok(latestSemester);
		} catch (Exception e) {
			logger.error("Error getting latest semester: ", e);
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * Lấy thông tin cá nhân của sinh viên
	 */
	@GetMapping("/profile")
	public ResponseEntity<StudentPortalInfo.StudentProfile> getProfile() {
		try {
			Long studentId = getCurrentStudentId();
			logger.info("Getting profile for student ID: {}", studentId);

			StudentPortalInfo.StudentProfile profile = studentPortalService.getStudentProfile(studentId);
			return ResponseEntity.ok(profile);
		} catch (Exception e) {
			logger.error("Error getting student profile", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Thay đổi mật khẩu cho sinh viên
	 */
	@PostMapping("/change-password")
	public ResponseEntity<StudentPortalInfo.ChangePasswordResponse> changePassword(
			@Valid @RequestBody StudentPortalInfo.ChangePasswordRequest request) {
		try {
			Long studentId = getCurrentStudentId();
			logger.info("Changing password for student ID: {}", studentId);

			StudentPortalInfo.ChangePasswordResponse response = studentPortalService.changePassword(studentId, request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error changing password", e);
			return ResponseEntity.ok(new StudentPortalInfo.ChangePasswordResponse(false, "Lỗi hệ thống: " + e.getMessage()));
		}
	}

	/**
	 * Lấy thông tin thanh toán học phí theo semester
	 */
	@GetMapping("/payment")
	public ResponseEntity<StudentPortalInfo.PaymentInfo> getPaymentInfo(@RequestParam(required = false) String semester) {
		try {
			Long studentId = getCurrentStudentId();
			logger.info("Getting payment info for student ID: {} in semester: {}", studentId, semester);

			// Nếu không có semester, lấy semester mới nhất
			if (semester == null || semester.trim().isEmpty()) {
				semester = studentPortalService.getLatestSemesterInfo();
			}

			StudentPortalInfo.PaymentInfo paymentInfo = studentPortalService.getPaymentInfo(studentId, semester);
			return ResponseEntity.ok(paymentInfo);
		} catch (Exception e) {
			logger.error("Error getting payment info", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Lấy thông tin thanh toán học phí của tất cả các semester
	 */
	@GetMapping("/payment/all")
	public ResponseEntity<List<StudentPortalInfo.PaymentInfo>> getAllPaymentInfo() {
		try {
			Long studentId = getCurrentStudentId();
			logger.info("Getting all payment info for student ID: {}", studentId);

			List<StudentPortalInfo.PaymentInfo> paymentInfos = studentPortalService.getAllPaymentInfo(studentId);
			return ResponseEntity.ok(paymentInfos);
		} catch (Exception e) {
			logger.error("Error getting all payment info", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Tạo payment record cho semester hiện tại
	 */
	@PostMapping("/payment/create")
	public ResponseEntity<String> createPayment(@RequestParam(required = false) String semester) {
		try {
			Long studentId = getCurrentStudentId();
			logger.info("Creating payment for student ID: {} in semester: {}", studentId, semester);

			// Nếu không có semester, lấy semester mới nhất
			if (semester == null || semester.trim().isEmpty()) {
				semester = studentPortalService.getLatestSemesterInfo();
			}

			studentPortalService.createPayment(studentId, semester);
			return ResponseEntity.ok("Payment record created successfully");
		} catch (Exception e) {
			logger.error("Error creating payment", e);
			return ResponseEntity.internalServerError().body("Error creating payment: " + e.getMessage());
		}
	}

	/**
	 * Lấy ID của sinh viên hiện tại từ Security Context
	 */
	private Long getCurrentStudentId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		return studentService.getStudentByUsername(username).map(StudentDTO::getId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));
	}
}
