package com.example.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.dto.LecturerDTO;
import com.example.app.model.Lecturer;
import com.example.app.model.Teaching;
import com.example.app.model.User;
import com.example.app.repository.LecturerRepository;
import com.example.app.repository.TeachingRepository;
import com.example.app.repository.UserRepository;

@Service
public class LecturerService {

	private final LecturerRepository lecturerRepository;
	private final TeachingRepository teachingRepository;
	private final UserRepository userRepository;

	public LecturerService(LecturerRepository lecturerRepository, TeachingRepository teachingRepository,
			UserRepository userRepository) {
		this.lecturerRepository = lecturerRepository;
		this.teachingRepository = teachingRepository;
		this.userRepository = userRepository;
	}

	// Convert Entity -> DTO
	private LecturerDTO convertToDTO(Lecturer entity) {
		return new LecturerDTO(entity.getId(), entity.getUserId(), entity.getLecturerCode());
	}

	// Convert DTO -> Entity
	private Lecturer convertToEntity(LecturerDTO dto) {
		return new Lecturer(dto.getId(), dto.getUserId(), dto.getLecturerCode());
	}

	public Lecturer saveLecturer(Lecturer lecturer) {
		return lecturerRepository.save(lecturer);
	}

	public Optional<LecturerDTO> getLecturerById(Long id) {
		return lecturerRepository.findById(id).map(this::convertToDTO);
	}

	public Lecturer getLecturerByUserId(Long id) {
		return lecturerRepository.findByUserId(id);
	}

	public List<LecturerDTO> getAllLecturers() {
		return lecturerRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public List<LecturerDTO.LecturerWithDetailsDTO> getAllLecturersWithDetails() {
		return lecturerRepository.findAll().stream().map(this::convertToDetailDTO).collect(Collectors.toList());
	}

	private LecturerDTO.LecturerWithDetailsDTO convertToDetailDTO(Lecturer lecturer) {
		User user = userRepository.findById(lecturer.getUserId()).orElse(null);
		String fullName = user != null ? user.getFullName() : lecturer.getLecturerCode();
		String department = user != null && user.getDepartmentId() != null ? 
			"Khoa " + user.getDepartmentId() : "N/A";
		
		return new LecturerDTO.LecturerWithDetailsDTO(
			lecturer.getId(),
			lecturer.getUserId(),
			lecturer.getLecturerCode(),
			fullName,
			department
		);
	}

	public LecturerDTO saveLecturer(LecturerDTO dto) {
		Lecturer saved = lecturerRepository.save(convertToEntity(dto));
		return convertToDTO(saved);
	}

	public Optional<LecturerDTO> updateLecturer(Long id, LecturerDTO dto) {
		return lecturerRepository.findById(id).map(entity -> {
			entity.setUserId(dto.getUserId());
			entity.setLecturerCode(dto.getLecturerCode());
			return convertToDTO(lecturerRepository.save(entity));
		});
	}

	@Transactional
	public void deleteLecturer(Long id) {
		// B1: tìm tất cả student thuộc class này
		List<Teaching> teachings = teachingRepository.findByLecturerId(id);

		// B2: set classId = null cho từng student
		for (Teaching t : teachings) {
			t.setLecturerId(null);
		}
		teachingRepository.saveAll(teachings);

		// B3: xóa class
		lecturerRepository.deleteById(id);
	}

	public Optional<Lecturer> getLecturerByUsername(String username) {
		Optional<User> userOpt = userRepository.findByUsername(username);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			return lecturerRepository.findAll().stream().filter(lecturer -> lecturer.getUserId().equals(user.getId()))
					.findFirst();
		}
		return Optional.empty();
	}
}
