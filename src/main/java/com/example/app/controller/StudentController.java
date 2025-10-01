package com.example.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.StudentDTO;
import com.example.app.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:4200")
public class StudentController {

	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;

	}

	@GetMapping
	public List<StudentDTO> getAllStudents() {
		return studentService.getAllStudents();
	}

	@GetMapping("/{id}")
	public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
		logger.info("Retrieving student with ID: {}", id);
		return studentService.getStudentById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<StudentDTO> saveStudent(@Valid @RequestBody StudentDTO dto) {
		logger.info("Creating new student: {}", dto.getStudentCode());
		StudentDTO saved = studentService.saveStudent(dto);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDTO dto) {
		logger.info("Updating student with ID: {}", id);
		return studentService.updateStudent(id, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// Xóa student kèm enrollments
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteStudent(@PathVariable Long id) {
		logger.info("Deleting student with ID: {}", id);
		return studentService.getStudentById(id).map(existing -> {
			studentService.deleteStudent(id);
			return ResponseEntity.noContent().build();
		}).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/code/{studentCode}")
	public ResponseEntity<StudentDTO> getStudentByCode(@PathVariable String studentCode) {
		logger.info("Retrieving student with code: {}", studentCode);
		return studentService.getStudentByStudentCode(studentCode).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// @GetMapping("/class/{classId}")
	// public ResponseEntity<List<Student>> getStudentsByClass(@PathVariable Long
	// classId) {
	// logger.info("Retrieving students for class ID: {}", classId);
	// List<Student> students = studentService.getStudentsByClassId(classId);
	// return ResponseEntity.ok(students);
	// }
}
