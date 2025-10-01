package com.example.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.app.dto.EnrollmentDTO;
import com.example.app.model.Enrollment;
import com.example.app.repository.EnrollmentRepository;

@Service
public class EnrollmentService {

	private final EnrollmentRepository enrollmentRepository;

	public EnrollmentService(EnrollmentRepository enrollmentRepository) {
		this.enrollmentRepository = enrollmentRepository;
	}

	private EnrollmentDTO convertToDTO(Enrollment entity) {
		return new EnrollmentDTO(entity.getId(), entity.getStudentId(), entity.getCourseId(), entity.getGrade(),
				entity.getComponentScore1(), entity.getComponentScore2(), entity.getFinalExamScore());
	}

	public Optional<EnrollmentDTO> getEnrollmentById(Long id) {
		return enrollmentRepository.findById(id).map(this::convertToDTO);
	}

	public List<EnrollmentDTO> getAllEnrollments() {
		return enrollmentRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public Enrollment saveEnrollment(Enrollment entity) {
		// Compute grade if scores are present
		if (entity != null) {
			String computed = computeGrade(entity.getComponentScore1(), entity.getComponentScore2(),
					entity.getFinalExamScore());
			if (computed != null) {
				entity.setGrade(computed);
			}
		}
		Enrollment saved = enrollmentRepository.save(entity);
		return saved;
	}

	private String computeGrade(Double s1, Double s2, Double finalExam) {
		if (s1 == null || s2 == null || finalExam == null)
			return null;
		// Weighted average: ((s1 + s2) * 0.4 / 2) + (final * 0.6)
		double compAvg = (s1 + s2) / 2.0;
		double total = compAvg * 0.4 + (finalExam * 0.6);
		// Map to letter grade per spec
		if (total >= 8.5 && total <= 10.0)
			return "A";
		if (total >= 8.0 && total < 8.5)
			return "B+";
		if (total >= 7.5 && total < 8.0)
			return "B";
		if (total >= 7.0 && total < 7.5)
			return "C";
		if (total >= 4.0 && total < 7.0)
			return "D";
		return "F";
	}

	public Optional<EnrollmentDTO> updateEnrollment(Long id, EnrollmentDTO dto) {
		return enrollmentRepository.findById(id).map(entity -> {
			entity.setStudentId(dto.getStudentId());
			entity.setCourseId(dto.getCourseId());
			entity.setComponentScore1(dto.getComponentScore1());
			entity.setComponentScore2(dto.getComponentScore2());
			entity.setFinalExamScore(dto.getFinalExamScore());
			entity.setGrade(computeGrade(dto.getComponentScore1(), dto.getComponentScore2(), dto.getFinalExamScore()));
			return convertToDTO(enrollmentRepository.save(entity));
		});
	}

	public void deleteEnrollment(Long id) {
		enrollmentRepository.deleteById(id);
	}

}
