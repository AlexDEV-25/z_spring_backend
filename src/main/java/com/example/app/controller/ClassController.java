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

import com.example.app.dto.ClassDTO;
import com.example.app.service.ClassService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

	private final ClassService classService;

	public ClassController(ClassService classService) {
		this.classService = classService;
	}

	// Lấy tất cả class
	@GetMapping
	public List<ClassDTO> getAllClases() {
		return classService.getAllClasses();
	}

	// Lấy class theo ID
	@GetMapping("/{id}")
	public ResponseEntity<ClassDTO> getClassById(@PathVariable Long id) {
		return classService.getClassById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// Tạo mới class
	@PostMapping
	public ResponseEntity<ClassDTO> createclass(@Valid @RequestBody ClassDTO dto) {
		ClassDTO saved = classService.saveClass(dto);
		return ResponseEntity.status(201).body(saved);
	}

	// Cập nhật class
	@PutMapping("/{id}")
	public ResponseEntity<ClassDTO> updateClass(@Valid @PathVariable Long id, @RequestBody ClassDTO dto) {
		return classService.updateClass(id, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// Xóa class
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
		return classService.getClassById(id).map(existing -> {
			classService.deleteClass(id);
			return ResponseEntity.noContent().<Void>build();
		}).orElse(ResponseEntity.notFound().build());
	}

}
