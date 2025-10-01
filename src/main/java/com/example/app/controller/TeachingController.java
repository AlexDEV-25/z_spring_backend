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

import com.example.app.dto.TeachingDTO;
import com.example.app.service.TeachingService;

@RestController
@RequestMapping("/api/teachings")
public class TeachingController {

	private final TeachingService teachingService;

	public TeachingController(TeachingService teachingService) {
		this.teachingService = teachingService;
	}

	// Lấy tất cả teaching
	@GetMapping
	public List<TeachingDTO> getAllTeachings() {
		return teachingService.getAllTeachings();
	}

	// Lấy teaching theo ID
	@GetMapping("/{id}")
	public ResponseEntity<TeachingDTO> getTeachingById(@PathVariable Long id) {
		return teachingService.getTeachingById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// Tạo mới teaching
	@PostMapping
	public ResponseEntity<TeachingDTO> saveTeaching(@RequestBody TeachingDTO dto) {
		TeachingDTO saved = teachingService.saveTeaching(dto);
		return ResponseEntity.status(201).body(saved);
	}

	// Cập nhật teaching
	@PutMapping("/{id}")
	public ResponseEntity<TeachingDTO> updateTeaching(@PathVariable Long id, @RequestBody TeachingDTO dto) {
		return teachingService.updateTeaching(id, dto).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Xóa teaching
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTeaching(@PathVariable Long id) {
		return teachingService.getTeachingById(id).map(existing -> {
			teachingService.deleteTeaching(id);
			return ResponseEntity.noContent().<Void>build();
		}).orElse(ResponseEntity.notFound().build());
	}
}
