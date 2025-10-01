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

import com.example.app.dto.CourseDTO;
import com.example.app.service.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

	private final CourseService courseService;

	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}

	// Lấy tất cả course
	@GetMapping
	public List<CourseDTO> getAllCourses() {
		return courseService.getAllCourses();
	}

	// Lấy course theo ID
	@GetMapping("/{id}")
	public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
		return courseService.getCourseById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// Tạo mới course
	@PostMapping
	public ResponseEntity<CourseDTO> saveCourse(@Valid @RequestBody CourseDTO dto) {
		CourseDTO saved = courseService.saveCourse(dto);
		return ResponseEntity.status(201).body(saved);
	}

	// Cập nhật course
	@PutMapping("/{id}")
	public ResponseEntity<CourseDTO> updateCourse(@Valid @PathVariable Long id, @RequestBody CourseDTO dto) {
		return courseService.updateCourse(id, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// Xóa course
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
		return courseService.getCourseById(id).map(existing -> {
			courseService.deleteCourse(id);
			return ResponseEntity.noContent().<Void>build();
		}).orElse(ResponseEntity.notFound().build());
	}
}
