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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.StudentDTO;
import com.example.app.dto.StudentPortalInfo;
import com.example.app.enumvalue.Status;
import com.example.app.model.Payment;
import com.example.app.model.Semester;
import com.example.app.repository.PaymentRepository;
import com.example.app.repository.SemesterRepository;
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
	private final SemesterRepository semesterRepository;
	private final PaymentRepository paymentRepository;

	public StudentPortalController(StudentService studentService, StudentPortalService studentPortalService,
			SemesterRepository semesterRepository, PaymentRepository paymentRepository) {
		this.studentService = studentService;
		this.studentPortalService = studentPortalService;
		this.semesterRepository = semesterRepository;
		this.paymentRepository = paymentRepository;
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
	 * Lấy thông tin thanh toán học phí theo semester
	 */
	@GetMapping("/payment")
	public ResponseEntity<StudentPortalInfo.PaymentInfo> getPaymentInfo(
			@RequestParam(required = false) String semester) {
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

				return ResponseEntity.ok("Đã xác nhận thanh toán thành công");
			} else {
				return ResponseEntity.badRequest().body("Không tìm thấy yêu cầu thanh toán");
			}
		} catch (Exception e) {
			logger.error("Error confirming payment", e);
			return ResponseEntity.internalServerError().body("Lỗi khi xác nhận thanh toán: " + e.getMessage());
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
				semester = studentPortalService.getLatestSemesterInfo();
			}

			String result = studentPortalService.createPaymentRequest(studentId, semester);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			logger.error("Error creating payment request", e);
			return ResponseEntity.internalServerError().body("Lỗi khi tạo yêu cầu thanh toán: " + e.getMessage());
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
}
