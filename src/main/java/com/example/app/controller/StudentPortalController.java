package com.example.app.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.StudentDTO;
import com.example.app.dto.StudentPortalInfo;
import com.example.app.dto.UserDTO;
import com.example.app.enumvalue.Status;
import com.example.app.model.Payment;
import com.example.app.model.Semester;
import com.example.app.repository.PaymentRepository;
import com.example.app.repository.SemesterRepository;
import com.example.app.service.StudentPortalService;
import com.example.app.service.StudentService;
import com.example.app.service.UserService;
import com.example.app.share.Share;

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
	private final SemesterRepository semesterRepository;
	private final PaymentRepository paymentRepository;
	private final UserService userService;

	public StudentPortalController(StudentService studentService, StudentPortalService studentPortalService,
			SemesterRepository semesterRepository, PaymentRepository paymentRepository, UserService userService) {
		this.studentService = studentService;
		this.studentPortalService = studentPortalService;
		this.semesterRepository = semesterRepository;
		this.paymentRepository = paymentRepository;
		this.userService = userService;
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
	public ResponseEntity<List<Share.SemesterInfo>> getAllSemesters() {
		try {
			List<Share.SemesterInfo> semesters = studentPortalService.getAllSemesters();
			logger.info("Retrieved {} semesters", semesters.size());
			return ResponseEntity.ok(semesters);
		} catch (Exception e) {
			logger.error("Error getting semesters: ", e);
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
	 * Lấy thông tin thanh toán học phí theo semester
	 */
	@GetMapping("/payment")
	public ResponseEntity<Share.PaymentSummaryDTO> getPaymentInfo(
			@RequestParam(required = false) String semester) {
		try {
			Long studentId = getCurrentStudentId();
			logger.info("Getting payment info for student ID: {} in semester: {}", studentId, semester);
			// Nếu không có semester, lấy semester mới nhất
			if (semester == null || semester.trim().isEmpty()) {
				semester = "2024-1";
			}

			Share.PaymentSummaryDTO paymentInfo = studentPortalService.getPaymentInfo(studentId, semester);
			return ResponseEntity.ok(paymentInfo);
		} catch (Exception e) {
			logger.error("Error getting payment info", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Lấy thông tin thanh toán học phí của tất cả các kỳ học
	 */
	@GetMapping("/payments/all")
	public ResponseEntity<List<Share.PaymentSummaryDTO>> getAllPaymentInfo() {
		try {
			Long studentId = getCurrentStudentId();
			logger.info("Getting all payment info for student ID: {}", studentId);

			List<Share.PaymentSummaryDTO> paymentInfos = studentPortalService.getAllPaymentInfo(studentId);
			return ResponseEntity.ok(paymentInfos);
		} catch (Exception e) {
			logger.error("Error getting all payment info", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Cập nhật trạng thái thanh toán thành công
	 */
	@PostMapping("/payment/confirm")
	public ResponseEntity<String> confirmPayment(@RequestParam String semester) {
		try {
			Long studentId = getCurrentStudentId();
			logger.info("Confirming payment for student ID: {} in semester: {}", studentId, semester);

			// Tìm payment hiện có
			Semester semesterObj = semesterRepository.findAll().stream().filter(s -> s.getSemester().equals(semester))
					.findFirst().orElseThrow(() -> new RuntimeException("Không tìm thấy kỳ học: " + semester));

			Optional<Payment> paymentOpt = paymentRepository.findByStudentIdAndSemesterId(studentId,
					semesterObj.getId());

			if (paymentOpt.isPresent()) {
				Payment payment = paymentOpt.get();
				payment.setStatus(Status.PAID);
				payment.setPaymentDate(LocalDateTime.now());
				paymentRepository.save(payment);

				// Cập nhật trạng thái enrollment và trừ slot
				studentPortalService.updateEnrollmentStatusToEnrolled(studentId, semester);

				return ResponseEntity.ok(Share.ResponseUtils.success("Đã xác nhận thanh toán thành công").getMessage());
			} else {
				return ResponseEntity.badRequest().body(Share.ResponseUtils.error("Không tìm thấy yêu cầu thanh toán").getMessage());
			}
		} catch (Exception e) {
			logger.error("Error confirming payment", e);
			return ResponseEntity.internalServerError().body(Share.ResponseUtils.error("Lỗi khi xác nhận thanh toán: " + e.getMessage()).getMessage());
		}
	}

	/**
	 * Tạo yêu cầu thanh toán học phí
	 */
	@PostMapping("/payment/create")
	public ResponseEntity<String> createPaymentRequest(@RequestParam(required = false) String semester) {
		try {
			Long studentId = getCurrentStudentId();
			logger.info("Creating payment request for student ID: {} in semester: {}", studentId, semester);

			// Nếu không có semester, lấy semester mới nhất
			if (semester == null || semester.trim().isEmpty()) {
				semester = "2024-1";
			}

			String result = studentPortalService.createPaymentRequest(studentId, semester);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			logger.error("Error creating payment request", e);
			return ResponseEntity.internalServerError().body(Share.ResponseUtils.error("Lỗi khi tạo yêu cầu thanh toán: " + e.getMessage()).getMessage());
		}
	}

	@GetMapping("/profile")
	public ResponseEntity<StudentPortalInfo.StudentProfileInfo> getMyProfile() {
		try {
			Long studentId = getCurrentStudentId();
			logger.info("Getting profile for student ID: {}", studentId);

			StudentPortalInfo.StudentProfileInfo profile = studentPortalService.getStudentProfile(studentId);
			return ResponseEntity.ok(profile);
		} catch (Exception e) {
			logger.error("Error getting student profile", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Thay đổi mật khẩu cho giảng viên
	 */
	@PostMapping("/change-password")
	public ResponseEntity<Share.ApiResponse> changePassword(
			@Valid @RequestBody Share.ChangePasswordRequest request) {
		try {
			Long userId = getCurrentUserId();
			logger.info("Changing password for lecturer ID: {}", userId);

			Share.ApiResponse response = studentPortalService.changePassword(userId, request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error changing password", e);
			return ResponseEntity.ok(Share.ResponseUtils.error("Lỗi hệ thống: " + e.getMessage()));
		}
	}

	/**
	 * Xuất bảng điểm ra file CSV
	 */
	@GetMapping("/grades/export")
	public ResponseEntity<byte[]> exportGrades(@RequestParam(required = false) String semester) {
		try {
			Long studentId = getCurrentStudentId();
			logger.info("Exporting grades for student ID: {} in semester: {}", studentId, semester);

			byte[] csvData = studentPortalService.exportGradesToCsv(studentId, semester);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
			headers.setContentDispositionFormData("attachment", "bang_diem.csv");

			return ResponseEntity.ok().headers(headers).body(csvData);
		} catch (Exception e) {
			logger.error("Error exporting grades", e);
			return ResponseEntity.internalServerError().build();
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

	/**
	 * Lấy ID của user hiện tại từ Security Context
	 */
	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		return userService.getUserByUsername(username).map(UserDTO::getId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người dùng"));
	}

	// ==================== STUDENT SCHEDULE ENDPOINTS ====================

	/**
	 * Lấy thời khóa biểu từ bảng student_schedule
	 * GET /api/student/schedule-list?semester=2024-1
	 */
	@GetMapping("/schedule-list")
	public ResponseEntity<?> getStudentScheduleList(@RequestParam(required = false, defaultValue = "2024-1") String semester) {
		try {
			Long studentId = getCurrentStudentId();
			logger.info("Getting schedule list for student ID: {} in semester: {}", studentId, semester);
			
			var schedules = studentPortalService.getStudentScheduleList(studentId, semester);
			return ResponseEntity.ok(schedules);
		} catch (Exception e) {
			logger.error("Error getting schedule list: ", e);
			return ResponseEntity.badRequest().body(Share.ResponseUtils.error("Lỗi khi lấy thời khóa biểu: " + e.getMessage()).getMessage());
		}
	}

	/**
	 * Tạo/cập nhật thời khóa biểu cho sinh viên
	 * POST /api/student/schedule/generate?semester=2024-1
	 */
	@PostMapping("/schedule/generate")
	public ResponseEntity<String> generateSchedule(@RequestParam(required = false, defaultValue = "2024-1") String semester) {
		try {
			Long studentId = getCurrentStudentId();
			logger.info("Generating schedule for student ID: {} in semester: {}", studentId, semester);
			
			studentPortalService.generateScheduleForStudent(studentId, semester);
			return ResponseEntity.ok(Share.ResponseUtils.success("Đã tạo thời khóa biểu thành công cho học kỳ " + semester).getMessage());
		} catch (Exception e) {
			logger.error("Error generating schedule: ", e);
			return ResponseEntity.badRequest().body(Share.ResponseUtils.error("Lỗi khi tạo thời khóa biểu: " + e.getMessage()).getMessage());
		}
	}

	/**
	 * Kiểm tra sinh viên đã có thời khóa biểu chưa
	 * GET /api/student/schedule/exists?semester=2024-1
	 */
	@GetMapping("/schedule/exists")
	public ResponseEntity<Boolean> hasSchedule(@RequestParam(required = false, defaultValue = "2024-1") String semester) {
		try {
			Long studentId = getCurrentStudentId();
			boolean exists = studentPortalService.hasSchedule(studentId, semester);
			return ResponseEntity.ok(exists);
		} catch (Exception e) {
			logger.error("Error checking schedule existence: ", e);
			return ResponseEntity.ok(false);
		}
	}
}