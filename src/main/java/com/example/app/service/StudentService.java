package com.example.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.dto.StudentDTO;
import com.example.app.exception.DuplicateResourceException;
import com.example.app.exception.ResourceNotFoundException;
import com.example.app.model.Student;
import com.example.app.model.User;
import com.example.app.repository.EnrollmentRepository;
import com.example.app.repository.PaymentRepository;
import com.example.app.repository.StudentRepository;
import com.example.app.repository.UserRepository;

@Service
@Transactional
public class StudentService {

	private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

	private final StudentRepository studentRepository;
	private final EnrollmentRepository enrollmentRepository;
	private final UserRepository userRepository;
	private final PaymentRepository paymentRepository;

	public StudentService(StudentRepository studentRepository, EnrollmentRepository enrollmentRepository,
			UserRepository userRepository, PaymentRepository paymentRepository) {
		this.studentRepository = studentRepository;
		this.enrollmentRepository = enrollmentRepository;
		this.userRepository = userRepository;
		this.paymentRepository = paymentRepository;
	}

	// Convert Entity -> DTO
	private StudentDTO convertToDTO(Student entity) {
		return new StudentDTO(entity.getId(), entity.getUserId(), entity.getStudentCode(), entity.getClassId());
	}

	// Convert DTO -> Entity
	private Student convertToEntity(StudentDTO dto) {
		return new Student(dto.getId(), dto.getUserId(), dto.getStudentCode(), dto.getClassId());
	}

	public Optional<StudentDTO> getStudentById(Long id) {
		return studentRepository.findById(id).map(this::convertToDTO);
	}

	public Student getStudentByUserId(Long id) {
		return studentRepository.findByUserId(id);
	}

	public List<StudentDTO> getAllStudents() {
		return studentRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public StudentDTO saveStudent(StudentDTO dto) {
		logger.info("Saving student: {}", dto.getStudentCode());

		// Check if student code already exists
		if (dto.getId() == null && studentRepository.existsByStudentCode(dto.getStudentCode())) {
			logger.warn("Student code already exists: {}", dto.getStudentCode());
			throw new DuplicateResourceException("Mã sinh viên đã tồn tại: " + dto.getStudentCode());
		}

		Student saved = studentRepository.save(convertToEntity(dto));
		return convertToDTO(saved);
	}

	public Optional<StudentDTO> updateStudent(Long id, StudentDTO dto) {
		return studentRepository.findById(id).map(entity -> {
			entity.setUserId(dto.getUserId());
			entity.setStudentCode(dto.getStudentCode());
			entity.setClassId(dto.getClassId());
			return convertToDTO(studentRepository.save(entity));
		});
	}

	@Transactional
	public void deleteStudent(Long id) {
		logger.info("Deleting student with ID: {}", id);
		if (!studentRepository.existsById(id)) {
			logger.warn("Student not found for deletion: {}", id);
			throw new ResourceNotFoundException("Không tìm thấy sinh viên với ID: " + id);
		}
		paymentRepository.setStudentIdNullByStudentId(id);
		enrollmentRepository.deleteByStudentId(id);
		studentRepository.deleteById(id);
		logger.info("Student deleted successfully: {}", id);
	}

	public StudentDTO getStudentByIdOrThrow(Long id) {
		return getStudentById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên với ID: " + id));
	}

	public Optional<StudentDTO> getStudentByStudentCode(String studentCode) {
		logger.debug("Finding student by student code: {}", studentCode);
		return studentRepository.findByStudentCode(studentCode).map(this::convertToDTO);
	}

	public Optional<StudentDTO> getStudentByUsername(String username) {
		logger.debug("Finding student by username: {}", username);
		Optional<User> userOpt = userRepository.findByUsername(username);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			return studentRepository.findAll().stream().filter(student -> student.getUserId().equals(user.getId()))
					.findFirst().map(this::convertToDTO);
		}
		return Optional.empty();
	}

}
