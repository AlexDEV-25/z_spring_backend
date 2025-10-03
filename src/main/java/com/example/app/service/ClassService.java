package com.example.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.dto.ClassDTO;
import com.example.app.model.ClassEntity;
import com.example.app.repository.ClassRepository;
import com.example.app.repository.StudentRepository;
import com.example.app.repository.TeachingRepository;

@Service
public class ClassService {

	private final ClassRepository classRepository;
	private final StudentRepository studentRepository;
	private final TeachingRepository teachingRepository;

	public ClassService(ClassRepository classRepository, StudentRepository studentRepository,
			TeachingRepository teachingRepository) {
		this.classRepository = classRepository;
		this.studentRepository = studentRepository;
		this.teachingRepository = teachingRepository;
	}

	// Convert Entity -> DTO
	private ClassDTO convertToDTO(ClassEntity entity) {
		return new ClassDTO(entity.getId(), entity.getName(), entity.getYear());
	}

	// Convert DTO -> Entity
	private ClassEntity convertToEntity(ClassDTO dto) {
		return new ClassEntity(dto.getId(), dto.getName(), dto.getYear());
	}

	public Optional<ClassDTO> getClassById(Long id) {
		return classRepository.findById(id).map(this::convertToDTO);
	}

	public List<ClassDTO> getAllClasses() {
		return classRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public ClassDTO saveClass(ClassDTO classDTO) {
		ClassEntity saved = classRepository.save(convertToEntity(classDTO));
		return convertToDTO(saved);
	}

	public Optional<ClassDTO> updateClass(Long id, ClassDTO dto) {
		return classRepository.findById(id).map(entity -> {
			entity.setName(dto.getName());
			entity.setYear(dto.getYear());
			return convertToDTO(classRepository.save(entity));
		});
	}

	@Transactional
	public void deleteClass(Long id) {
		studentRepository.deleteByClassId(id);
		classRepository.deleteById(id);
	}
}
