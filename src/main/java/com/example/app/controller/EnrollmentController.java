package com.example.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.EnrollmentDTO;
import com.example.app.model.Enrollment;
import com.example.app.service.EnrollmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

	private final EnrollmentService enrollmentService;

	public EnrollmentController(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	// Lấy tất cả enrollment
	@GetMapping
	public List<EnrollmentDTO> getAllEnrollments() {
		return enrollmentService.getAllEnrollments();
	}

	// Lấy enrollment theo ID
	@GetMapping("/{id}")
	public ResponseEntity<EnrollmentDTO> getEnrollmentById(@PathVariable Long id) {
		return enrollmentService.getEnrollmentById(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Convert DTO -> Entity
	private Enrollment convertToEntity(EnrollmentDTO dto) {
		return new Enrollment(dto.getId(), dto.getStudentId(), dto.getCourseId(), dto.getGrade(),
				dto.getComponentScore1(), dto.getComponentScore2(), dto.getFinalExamScore());
	}

	// Convert Entity -> DTO
	private EnrollmentDTO convertToDTO(Enrollment entity) {
		return new EnrollmentDTO(entity.getId(), entity.getStudentId(), entity.getCourseId(), entity.getGrade(),
				entity.getComponentScore1(), entity.getComponentScore2(), entity.getFinalExamScore());
	}

	// Tạo mới enrollment( bất hợp lý sẽ sửa)
	@PostMapping
	public ResponseEntity<EnrollmentDTO> saveEnrollment(@Valid @RequestBody EnrollmentDTO dto) {
		Enrollment saved = enrollmentService.saveEnrollment(convertToEntity(dto));
		return ResponseEntity.status(201).body(convertToDTO(saved));
	}

	// Cập nhật enrollment
	@PutMapping("/{id}")
	public ResponseEntity<EnrollmentDTO> updateEnrollment(@Valid @PathVariable Long id,
			@RequestBody EnrollmentDTO dto) {
		return enrollmentService.updateEnrollment(id, dto).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());

	}

	// Xóa enrollment
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteenrollment(@PathVariable Long id) {
		return enrollmentService.getEnrollmentById(id).map(existing -> {
			enrollmentService.deleteEnrollment(id);
			return ResponseEntity.noContent().<Void>build();
		}).orElse(ResponseEntity.notFound().build());
	}
}
