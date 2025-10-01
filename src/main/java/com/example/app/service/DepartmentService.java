package com.example.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.dto.DepartmentDTO;
import com.example.app.model.Department;
import com.example.app.model.User;
import com.example.app.repository.DepartmentRepository;
import com.example.app.repository.UserRepository;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;
	private UserRepository userRepository;

	public DepartmentService(DepartmentRepository departmentRepository, UserRepository userRepository) {
		this.departmentRepository = departmentRepository;
		this.userRepository = userRepository;
	}

	// Convert Entity -> DTO
	private DepartmentDTO convertToDTO(Department entity) {
		return new DepartmentDTO(entity.getId(), entity.getName(), entity.getCode());
	}

	// Convert DTO -> Entity
	private Department convertToEntity(DepartmentDTO dto) {
		return new Department(dto.getId(), dto.getName(), dto.getCode());
	}

	public Optional<DepartmentDTO> getDepartmentById(Long id) {
		return departmentRepository.findById(id).map(this::convertToDTO);
	}

	public List<DepartmentDTO> getAllDepartments() {
		return departmentRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public Optional<DepartmentDTO> getDepartmentByCode(String code) {
		return departmentRepository.findByCode(code).map(this::convertToDTO);
	}

	public DepartmentDTO saveDepartment(DepartmentDTO dto) {
		Department saved = departmentRepository.save(convertToEntity(dto));
		return convertToDTO(saved);
	}

	public Optional<DepartmentDTO> updateDepartment(Long id, DepartmentDTO dto) {
		return departmentRepository.findById(id).map(entity -> {
			entity.setName(dto.getName());
			entity.setCode(dto.getCode());
			return convertToDTO(departmentRepository.save(entity));
		});
	}

	@Transactional
	public void deleteDepartment(Long id) {

		List<User> users = userRepository.findByDepartmentId(id);

		// B2: set classId = null cho từng student
		for (User u : users) {
			u.setDepartmentId(null);
		}
		userRepository.saveAll(users);
		departmentRepository.deleteById(id);
	}
}
