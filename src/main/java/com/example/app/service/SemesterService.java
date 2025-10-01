package com.example.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.dto.SemesterDTO;
import com.example.app.model.Semester;
import com.example.app.repository.CourseRepository;
import com.example.app.repository.PaymentRepository;
import com.example.app.repository.SemesterRepository;

@Service
public class SemesterService {

	private final SemesterRepository semesterRepository;
	private final CourseRepository courseRepository;
	private final PaymentRepository paymentRepository;

	public SemesterService(SemesterRepository semesterRepository, CourseRepository courseRepository,
			PaymentRepository paymentRepository) {
		this.semesterRepository = semesterRepository;
		this.courseRepository = courseRepository;
		this.paymentRepository = paymentRepository;
	}

	// Convert Entity -> DTO
	private SemesterDTO convertToDTO(Semester entity) {
		return new SemesterDTO(entity.getId(), entity.getSemester());
	}

	// Convert DTO -> Entity
	private Semester convertToEntity(SemesterDTO dto) {
		return new Semester(dto.getId(), dto.getSemester());
	}

	public Optional<SemesterDTO> getSemesterById(Long id) {
		return semesterRepository.findById(id).map(this::convertToDTO);
	}

	public List<SemesterDTO> getAllSemesters() {
		return semesterRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public SemesterDTO saveSemester(SemesterDTO dto) {
		Semester saved = semesterRepository.save(convertToEntity(dto));
		return convertToDTO(saved);
	}

	public Optional<SemesterDTO> updateSemester(Long id, SemesterDTO dto) {
		return semesterRepository.findById(id).map(entity -> {
			entity.setSemester(dto.getSemester());
			return convertToDTO(semesterRepository.save(entity));
		});
	}

	@Transactional
	public void deleteSemester(Long id) {
		courseRepository.setSemesterIdNullBySemesterId(id);
		paymentRepository.setSemesterIdNullBySemesterId(id);
		semesterRepository.deleteById(id);
	}
}
