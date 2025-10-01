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

import com.example.app.dto.SemesterDTO;
import com.example.app.service.SemesterService;

@RestController
@RequestMapping("/api/semesters")
@CrossOrigin(origins = "http://localhost:4200")
public class SemesterController {

	private final SemesterService semesterService;

	public SemesterController(SemesterService semesterService) {
		this.semesterService = semesterService;
	}

	@GetMapping
	public List<SemesterDTO> getAllSemestes() {
		return semesterService.getAllSemesters();
	}

	@GetMapping("/{id}")
	public ResponseEntity<SemesterDTO> getSemesterById(@PathVariable Long id) {
		return semesterService.getSemesterById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<SemesterDTO> saveSemester(@RequestBody SemesterDTO dto) {
		SemesterDTO saved = semesterService.saveSemester(dto);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<SemesterDTO> updateSemester(@PathVariable Long id, @RequestBody SemesterDTO dto) {
		return semesterService.updateSemester(id, dto).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSemester(@PathVariable Long id) {
		return semesterService.getSemesterById(id).map(existing -> {
			semesterService.deleteSemester(id);
			return ResponseEntity.noContent().<Void>build();
		}).orElse(ResponseEntity.notFound().build());
	}
}
