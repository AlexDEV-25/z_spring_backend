package com.example.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.dto.CourseDTO;
import com.example.app.model.Course;
import com.example.app.repository.CourseRepository;
import com.example.app.repository.EnrollmentRepository;
import com.example.app.repository.TeachingRepository;

@Service
public class CourseService {

	private final CourseRepository courseRepository;
	private final EnrollmentRepository enrollmentRepository;
	private final TeachingRepository teachingRepository;

	public CourseService(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository,
			TeachingRepository teachingRepository) {
		this.courseRepository = courseRepository;
		this.enrollmentRepository = enrollmentRepository;
		this.teachingRepository = teachingRepository;
	}

	// Convert Entity -> DTO
	private CourseDTO convertToDTO(Course entity) {
		return new CourseDTO(entity.getId(), entity.getCourseCode(), entity.getName(), entity.getCredit(),
				entity.getSlot(), entity.getFee(), entity.getSemesterId());
	}

	// Convert DTO -> Entity
	private Course convertToEntity(CourseDTO dto) {
		return new Course(dto.getId(), dto.getCourseCode(), dto.getName(), dto.getCredit(), dto.getSlot(), dto.getFee(),
				dto.getSemesterId());
	}

	public Optional<CourseDTO> getCourseById(Long id) {
		return courseRepository.findById(id).map(this::convertToDTO);
	}

	public List<CourseDTO> getAllCourses() {
		return courseRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public CourseDTO saveCourse(CourseDTO dto) {
		Course saved = courseRepository.save(convertToEntity(dto));
		return convertToDTO(saved);
	}

	public Optional<CourseDTO> updateCourse(Long id, CourseDTO dto) {
		return courseRepository.findById(id).map(entity -> {
			entity.setCourseCode(dto.getCourseCode());
			entity.setName(dto.getName());
			entity.setCredit(dto.getCredit());
			entity.setSlot(dto.getSlot());
			entity.setFee(dto.getFee());
			entity.setSemesterId(dto.getSemesterId());
			return convertToDTO(courseRepository.save(entity));
		});
	}

	@Transactional
	public void deleteCourse(Long id) {
		enrollmentRepository.setCourseIdNullByCourseId(id);

		teachingRepository.deleteByCourseId(id);

		courseRepository.deleteById(id);
	}
}
