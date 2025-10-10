package com.example.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.app.dto.UserDTO;
import com.example.app.model.Lecturer;
import com.example.app.model.Student;
import com.example.app.model.User;
import com.example.app.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final StudentService studentService;
	private final LecturerService lecturerService;

	// Constructor injection (Spring tự inject UserRepository)
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, StudentService studentService,
			LecturerService lecturerService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.studentService = studentService;
		this.lecturerService = lecturerService;
	}

	// Convert Entity -> DTO
	private UserDTO convertToDTO(User entity) {

		return new UserDTO(entity.getId(), entity.getUsername(), entity.getPassword(), entity.getFullName(),
				entity.getEmail(), entity.getRoleId(), entity.getGender(), entity.getPhone(), entity.getDepartmentId(),
				entity.getDateOfBirth(), entity.getAddress());
	}

	// Convert DTO -> Entity
	private User convertToEntity(UserDTO dto) {
		return new User(dto.getId(), dto.getUsername(), dto.getPassword(), dto.getFullName(), dto.getEmail(),
				dto.getRoleId(), dto.getGender(), dto.getPhone(), dto.getDepartmentId(), dto.getDateOfBirth(),
				dto.getAddress());
	}

	public Optional<UserDTO> getUserById(Long id) {
		return userRepository.findById(id).map(this::convertToDTO);
	}

	public Optional<UserDTO> getUserByUsername(String username) {
		return userRepository.findByUsername(username).map(this::convertToDTO);
	}

	public List<UserDTO> getAllUsers() {
		return userRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public UserDTO saveUser(UserDTO dto) {
		dto.setPassword(passwordEncoder.encode(dto.getPassword()));
		User saved = userRepository.save(convertToEntity(dto));
		return convertToDTO(saved);
	}

	public Optional<UserDTO> updateUser(Long id, UserDTO dto) {
		return userRepository.findById(id).map(entity -> {
			entity.setUsername(dto.getUsername());
			entity.setFullName(dto.getFullName());
			if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
				entity.setPassword(passwordEncoder.encode(dto.getPassword()));
			}
			entity.setEmail(dto.getEmail());
			entity.setRoleId(dto.getRoleId());
			entity.setGender(dto.getGender());
			entity.setPhone(dto.getPhone());
			entity.setDepartmentId(dto.getDepartmentId());
			entity.setDateOfBirth(dto.getDateOfBirth());
			entity.setAddress(dto.getAddress());
			return convertToDTO(userRepository.save(entity));
		});
	}

	@Transactional
	public void deleteUser(Long id) {
		Student studentExist = studentService.getStudentByUserId(id);
		Lecturer lecturerExist = lecturerService.getLecturerByUserId(id);

		if (studentExist != null) {
			studentService.deleteStudent(studentExist.getId());
		}
		if (lecturerExist != null) {
			lecturerService.deleteLecturer(lecturerExist.getId());
		}

		userRepository.deleteById(id);
	}

	// Lấy danh sách users có thể assign làm sinh viên - Backend filter thay vì frontend
	public List<UserDTO> getAvailableUsersForStudent(Long excludeStudentId) {
		// Lấy tất cả users có role = 3 (ROLE_SINH_VIEN)
		List<User> allUsers = userRepository.findAll();
		
		// Lấy danh sách user IDs đã được assign làm sinh viên
		List<Student> allStudents = studentService.getAllStudents().stream()
			.map(dto -> {
				Student student = new Student();
				student.setId(dto.getId());
				student.setUserId(dto.getUserId());
				return student;
			})
			.collect(Collectors.toList());
		List<Long> assignedUserIds = allStudents.stream()
			.map(Student::getUserId)
			.filter(userId -> userId != null)
			.collect(Collectors.toList());
		
		// Nếu có excludeStudentId, lấy userId của student đó để cho phép chọn lại
		Long currentUserId = null;
		if (excludeStudentId != null) {
			Optional<Student> currentStudent = allStudents.stream()
				.filter(s -> s.getId().equals(excludeStudentId))
				.findFirst();
			if (currentStudent.isPresent()) {
				currentUserId = currentStudent.get().getUserId();
			}
		}
		
		final Long allowedUserId = currentUserId;
		
		// Filter users: role = 3 AND (chưa được assign HOẶC là current user)
		return allUsers.stream()
			.filter(user -> {
				// Phải có role = 3 (ROLE_SINH_VIEN)
				if (user.getRoleId() == null || !user.getRoleId().equals(3L)) {
					return false;
				}
				
				// Chưa được assign HOẶC là current user được phép chọn lại
				boolean isAssigned = assignedUserIds.contains(user.getId());
				boolean isCurrentUser = allowedUserId != null && user.getId().equals(allowedUserId);
				
				return !isAssigned || isCurrentUser;
			})
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}
}
