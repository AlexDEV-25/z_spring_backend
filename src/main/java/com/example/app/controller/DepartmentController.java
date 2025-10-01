package com.example.app.controller;

import java.util.List;

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

import com.example.app.dto.DepartmentDTO;
import com.example.app.service.DepartmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "http://localhost:4200")
public class DepartmentController {

	private final DepartmentService departmentService;

	public DepartmentController(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@GetMapping
	public List<DepartmentDTO> getAllDepartments() {
		return departmentService.getAllDepartments();
	}

	@GetMapping("/{id}")
	public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long id) {
		return departmentService.getDepartmentById(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<DepartmentDTO> saveDepartment(@Valid @RequestBody DepartmentDTO dto) {
		DepartmentDTO saved = departmentService.saveDepartment(dto);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<DepartmentDTO> updateDepartment(@Valid @PathVariable Long id,
			@RequestBody DepartmentDTO dto) {
		return departmentService.updateDepartment(id, dto).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
		return departmentService.getDepartmentById(id).map(existing -> {
			departmentService.deleteDepartment(id);
			return ResponseEntity.noContent().<Void>build();
		}).orElse(ResponseEntity.notFound().build());
	}
}
