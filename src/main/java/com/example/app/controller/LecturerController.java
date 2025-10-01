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

import com.example.app.dto.LecturerDTO;
import com.example.app.service.LecturerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/lecturers")
public class LecturerController {

	private final LecturerService lecturerService;

	public LecturerController(LecturerService lecturerService) {
		this.lecturerService = lecturerService;
	}

	// Lấy tất cả lecturer
	@GetMapping
	public List<LecturerDTO> getAllLecturers() {
		return lecturerService.getAllLecturers();
	}

	// Lấy tất cả lecturer với thông tin đầy đủ cho teachings
	@GetMapping("/with-details")
	public List<LecturerDTO.LecturerWithDetailsDTO> getAllLecturersWithDetails() {
		return lecturerService.getAllLecturersWithDetails();
	}

	// Lấy lecturer theo ID
	@GetMapping("/{id}")
	public ResponseEntity<LecturerDTO> getLecturerById(@PathVariable Long id) {
		return lecturerService.getLecturerById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// Tạo mới lecturer
	@PostMapping
	public ResponseEntity<LecturerDTO> saveLecturer(@Valid @RequestBody LecturerDTO dto) {
		LecturerDTO saved = lecturerService.saveLecturer(dto);
		return ResponseEntity.status(201).body(saved);
	}

	// Cập nhật lecturer
	@PutMapping("/{id}")
	public ResponseEntity<LecturerDTO> updateLecturer(@PathVariable Long id, @RequestBody LecturerDTO dto) {
		return lecturerService.updateLecturer(id, dto).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Xóa lecturer
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLecturer(@PathVariable Long id) {
		return lecturerService.getLecturerById(id).map(existing -> {
			lecturerService.deleteLecturer(id);
			return ResponseEntity.noContent().<Void>build();
		}).orElse(ResponseEntity.notFound().build());
	}
}
