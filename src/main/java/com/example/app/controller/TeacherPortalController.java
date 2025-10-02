package com.example.app.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.example.app.dto.EnrollmentDTO;
import com.example.app.dto.TeacherPortalInfo;
import com.example.app.dto.TeacherPortalInfo.StudentInfo;
import com.example.app.model.Enrollment;
import com.example.app.model.Lecturer;
import com.example.app.model.Teaching;
import com.example.app.repository.TeachingRepository;
import com.example.app.service.LecturerService;
import com.example.app.service.TeacherPortalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/teacher")
@CrossOrigin(origins = "http://localhost:4200")
public class TeacherPortalController {

	private static final Logger logger = LoggerFactory.getLogger(TeacherPortalController.class);

	private final TeacherPortalService teacherService;
	private final LecturerService lecturerService;
	private final TeachingRepository teachingRepository;

	public TeacherPortalController(TeacherPortalService teacherService, LecturerService lecturerService,
			TeachingRepository teachingRepository) {
		this.teacherService = teacherService;
		this.lecturerService = lecturerService;
		this.teachingRepository = teachingRepository;
	}

	/**
	 * Lấy danh sách tất cả semesters từ database
	 */
	@GetMapping("/semesters")
	public ResponseEntity<List<TeacherPortalInfo.SemesterInfo>> getAllSemesters() {
		try {
			List<TeacherPortalInfo.SemesterInfo> semesters = teacherService.getAllSemesters();
			logger.info("Retrieved {} semesters for teacher", semesters.size());
			return ResponseEntity.ok(semesters);
		} catch (Exception e) {
			logger.error("Error getting semesters: ", e);
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * Lấy danh sách lớp học mà giảng viên được phân công dạy theo semester
	 */
	@GetMapping("/classes")
	public ResponseEntity<List<TeacherPortalInfo.TeacherScheduleInfo>> getMyClasses(
			@RequestParam(required = false) String semester) {
		try {
			Long lecturerId = getCurrentLecturerId();
			logger.info("Getting classes for lecturer ID: {} in semester: {}", lecturerId, semester);

			List<TeacherPortalInfo.TeacherScheduleInfo> classes = teacherService.getTeacherClasses(lecturerId,
					semester);
			return ResponseEntity.ok(classes);
		} catch (Exception e) {
			logger.error("Error getting teacher classes", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Lấy danh sách sinh viên trong một lớp học cụ thể
	 */
	@GetMapping("/classes/{teachingId}/students")
	public ResponseEntity<List<StudentInfo>> getStudentsInClass(@PathVariable Long teachingId) {
		try {
			Long lecturerId = getCurrentLecturerId();
			logger.info("Getting students for teaching ID: {} by lecturer ID: {}", teachingId, lecturerId);

			List<StudentInfo> students = teacherService.getStudentsForClass(teachingId);
			return ResponseEntity.ok(students);
		} catch (Exception e) {
			logger.error("Error getting students for class", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Chấm điểm cho sinh viên
	 */
	@PostMapping("/grade")
	public ResponseEntity<Enrollment> gradeStudent(@Valid @RequestBody EnrollmentDTO enrollmentDTO) {
		try {
			Long lecturerId = getCurrentLecturerId();
			logger.info("Grading student by lecturer ID: {}", lecturerId);

			// Kiểm tra xem giảng viên có được phân công dạy môn học này không
			boolean isAssigned = teacherService.isTeacherAssignedToCourse(lecturerId, enrollmentDTO.getCourseId());

			if (!isAssigned) {
				logger.warn("Lecturer {} is not assigned to course {} in semester {}", lecturerId,
						enrollmentDTO.getCourseId());
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

			Enrollment enrollment = teacherService.gradeStudent(enrollmentDTO);
			return ResponseEntity.ok(enrollment);
		} catch (Exception e) {
			logger.error("Error grading student", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Debug endpoint để kiểm tra thông tin giảng viên
	 */
	@GetMapping("/debug")
	public ResponseEntity<Object> debugInfo() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();

			logger.info("Debug - Username from JWT: {}", username);

			Optional<Lecturer> lecturerOpt = lecturerService.getLecturerByUsername(username);
			if (lecturerOpt.isPresent()) {
				Lecturer lecturer = lecturerOpt.get();
				logger.info("Debug - Found lecturer: ID={}, Code={}", lecturer.getId(), lecturer.getLecturerCode());

				// Kiểm tra có teaching nào không
				List<Teaching> teachings = teachingRepository.findByLecturerId(lecturer.getId());
				logger.info("Debug - Found {} teachings for lecturer", teachings.size());

				return ResponseEntity.ok(Map.of("username", username, "lecturerId", lecturer.getId(), "lecturerCode",
						lecturer.getLecturerCode(), "teachingsCount", teachings.size(), "teachings", teachings));
			} else {
				logger.warn("Debug - No lecturer found for username: {}", username);
				return ResponseEntity.ok(Map.of("username", username, "error", "No lecturer found for this username"));
			}
		} catch (Exception e) {
			logger.error("Debug error", e);
			return ResponseEntity.ok(Map.of("error", e.getMessage()));
		}
	}

	/**
	 * Lấy thông tin cá nhân của giảng viên
	 */
	@GetMapping("/profile")
	public ResponseEntity<TeacherPortalInfo.TeacherProfile> getProfile() {
		try {
			Long lecturerId = getCurrentLecturerId();
			logger.info("Getting profile for lecturer ID: {}", lecturerId);

			TeacherPortalInfo.TeacherProfile profile = teacherService.getTeacherProfile(lecturerId);
			return ResponseEntity.ok(profile);
		} catch (Exception e) {
			logger.error("Error getting teacher profile", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Thay đổi mật khẩu cho giảng viên
	 */
	@PostMapping("/change-password")
	public ResponseEntity<TeacherPortalInfo.ChangePasswordResponse> changePassword(
			@Valid @RequestBody TeacherPortalInfo.ChangePasswordRequest request) {
		try {
			Long lecturerId = getCurrentLecturerId();
			logger.info("Changing password for lecturer ID: {}", lecturerId);

			TeacherPortalInfo.ChangePasswordResponse response = teacherService.changePassword(lecturerId, request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error changing password", e);
			return ResponseEntity
					.ok(new TeacherPortalInfo.ChangePasswordResponse(false, "Lỗi hệ thống: " + e.getMessage()));
		}
	}

	/**
	 * Xuất bảng điểm lớp học ra file CSV
	 */
	@GetMapping("/classes/{teachingId}/export")
	public ResponseEntity<byte[]> exportClassGrades(@PathVariable Long teachingId) {
		try {
			Long lecturerId = getCurrentLecturerId();
			logger.info("Exporting grades for teaching ID: {} by lecturer: {}", teachingId, lecturerId);

			byte[] csvData = teacherService.exportClassGradesToCsv(teachingId, lecturerId);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
			headers.setContentDispositionFormData("attachment", "bang_diem_lop.csv");
			
			return ResponseEntity.ok()
					.headers(headers)
					.body(csvData);
		} catch (Exception e) {
			logger.error("Error exporting class grades", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Lấy ID của giảng viên hiện tại từ Security Context
	 */
	private Long getCurrentLecturerId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		return lecturerService.getLecturerByUsername(username).map(Lecturer::getId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin giảng viên"));
	}
}
