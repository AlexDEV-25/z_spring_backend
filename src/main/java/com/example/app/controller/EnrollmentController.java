package com.example.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.DepartmentDTO;
import com.example.app.dto.PrincipalPortalInfo;
import com.example.app.dto.SemesterDTO;
import com.example.app.service.EnrollmentService;

@RestController
@RequestMapping("/api/admin/enrollments")
@CrossOrigin(origins = "http://localhost:4200")
public class EnrollmentController {

	private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);
	private final EnrollmentService enrollmentService;

	public EnrollmentController(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	// Lấy danh sách khoa
	@GetMapping("/departments")
	public ResponseEntity<List<DepartmentDTO>> getDepartments() {
		try {
			List<DepartmentDTO> departments = enrollmentService.getAllDepartments();
			return ResponseEntity.ok(departments);
		} catch (Exception e) {
			logger.error("Error getting departments", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	// Lấy danh sách học kỳ
	@GetMapping("/semesters")
	public ResponseEntity<List<SemesterDTO>> getSemesters() {
		try {
			List<SemesterDTO> semesters = enrollmentService.getAllSemesters();
			return ResponseEntity.ok(semesters);
		} catch (Exception e) {
			logger.error("Error getting semesters", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	// Lấy tất cả sinh viên có GPA >= 3.6 theo khoa và kỳ học để trao học bổng

	@GetMapping("/scholarships/eligible-students")
	public ResponseEntity<List<PrincipalPortalInfo.ScholarshipCandidate>> getStudentsEligibleForScholarship(
			@RequestParam(required = false) Long departmentId, @RequestParam(required = false) String semester) {
		try {
			logger.info("Getting students eligible for scholarship (GPA >= 3.6) - Department: {}, Semester: {}", departmentId, semester);

			List<PrincipalPortalInfo.ScholarshipCandidate> candidates = enrollmentService
					.getStudentsEligibleForScholarship(departmentId, semester);
			return ResponseEntity.ok(candidates);
		} catch (Exception e) {
			logger.error("Error getting students eligible for scholarship", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	// Xuất danh sách học bổng ra CSV
	@GetMapping("/scholarships/export")
	public ResponseEntity<byte[]> exportScholarshipList(@RequestParam(required = false) Long departmentId,
			@RequestParam(required = false) String semester) {
		try {
			logger.info("Exporting scholarship list - Department: {}, Semester: {}", departmentId, semester);

			byte[] csvData = enrollmentService.exportScholarshipListToCsv(departmentId, semester);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
			headers.setContentDispositionFormData("attachment", "danh_sach_hoc_bong.csv");

			return ResponseEntity.ok().headers(headers).body(csvData);
		} catch (Exception e) {
			logger.error("Error exporting scholarship list", e);
			return ResponseEntity.internalServerError().build();
		}
	}
}
