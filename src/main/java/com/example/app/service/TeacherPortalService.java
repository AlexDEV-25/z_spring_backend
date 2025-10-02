package com.example.app.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.dto.EnrollmentDTO;
import com.example.app.dto.TeacherPortalInfo;
import com.example.app.dto.TeacherPortalInfo.StudentInfo;
import com.example.app.exception.ResourceNotFoundException;
import com.example.app.model.ClassEntity;
import com.example.app.model.Course;
import com.example.app.model.Enrollment;
import com.example.app.model.Lecturer;
import com.example.app.model.Student;
import com.example.app.model.Teaching;
import com.example.app.model.User;
import com.example.app.repository.ClassRepository;
import com.example.app.repository.CourseRepository;
import com.example.app.repository.EnrollmentRepository;
import com.example.app.repository.LecturerRepository;
import com.example.app.repository.SemesterRepository;
import com.example.app.repository.StudentRepository;
import com.example.app.repository.TeachingRepository;
import com.example.app.repository.UserRepository;

@Service
@Transactional
public class TeacherPortalService {

	private static final Logger logger = LoggerFactory.getLogger(TeacherPortalService.class);

	private final TeachingRepository teachingRepository;
	private final CourseRepository courseRepository;
	private final StudentRepository studentRepository;
	private final EnrollmentRepository enrollmentRepository;
	private final UserRepository userRepository;
	private final ClassRepository classRepository;
	private final SemesterRepository semesterRepository;
	private final LecturerRepository lecturerRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final GradeCalculationService gradeCalculationService;

	public TeacherPortalService(TeachingRepository teachingRepository, CourseRepository courseRepository,
			StudentRepository studentRepository, EnrollmentRepository enrollmentRepository,
			UserRepository userRepository, ClassRepository classRepository, SemesterRepository semesterRepository,
			LecturerRepository lecturerRepository, GradeCalculationService gradeCalculationService) {
		this.teachingRepository = teachingRepository;
		this.courseRepository = courseRepository;
		this.studentRepository = studentRepository;
		this.enrollmentRepository = enrollmentRepository;
		this.userRepository = userRepository;
		this.classRepository = classRepository;
		this.semesterRepository = semesterRepository;
		this.lecturerRepository = lecturerRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
		this.gradeCalculationService = gradeCalculationService;
	}

	/**
	 * Lấy danh sách lớp học mà giảng viên được phân công dạy theo semester được
	 * chọn
	 */
	public List<TeacherPortalInfo.TeacherScheduleInfo> getTeacherClasses(Long lecturerId, String semester) {
		logger.info("Getting teacher classes for lecturer ID: {} in semester: {}", lecturerId, semester);

		List<Teaching> teachings = teachingRepository.findByLecturerId(lecturerId);

		// Xác định semesterId để lọc (sử dụng semester được truyền vào hoặc lấy mới
		// nhất)
		Long targetSemesterId;
		if (semester != null && !semester.trim().isEmpty()) {
			// Tìm semesterId từ semester string
			targetSemesterId = semesterRepository.findAll().stream().filter(s -> s.getSemester().equals(semester))
					.map(s -> s.getId()).findFirst().orElse(getLatestSemesterId(teachings));
		} else {
			targetSemesterId = getLatestSemesterId(teachings);
		}

		logger.info("Filtering teacher classes by semesterId: {}", targetSemesterId);

		List<TeacherPortalInfo.TeacherScheduleInfo> teacherClasses = new ArrayList<>();
		for (Teaching teaching : teachings) {
			Long courseId = teaching.getCourseId();
			Long courseSemesterId = courseRepository.findById(courseId).map(Course::getSemesterId).orElse(null);

			// Bỏ qua nếu không thuộc semester được chọn
			if (!Objects.equals(courseSemesterId, targetSemesterId))
				continue;

			Course course = courseRepository.findById(courseId).orElse(null);
			if (course == null)
				continue;

			List<StudentInfo> students = getStudentsForCourse(courseId);

			Long classId = teaching.getClassId();
			String className = null;
			if (classId != null) {
				className = classRepository.findById(classId).map(ClassEntity::getName).orElse(null);
			}

			TeacherPortalInfo.TeacherScheduleInfo classInfo = new TeacherPortalInfo.TeacherScheduleInfo(
					teaching.getId(), course.getId(), course.getCourseCode(), course.getName(), course.getCredit(),
					teaching.getPeriod(), teaching.getDayOfWeek(), teaching.getClassRoom(), classId, className,
					students);

			teacherClasses.add(classInfo);
		}

		return teacherClasses;
	}

	/*
	 * chưa lọc public List<TeacherClassInfo> getTeacherClasses(Long lecturerId) {
	 * logger.info("Getting classes for lecturer ID: {}", lecturerId);
	 * 
	 * List<Teaching> teachings = teachingRepository.findByLecturerId(lecturerId);
	 * List<TeacherClassInfo> teacherClasses = new ArrayList<>();
	 * 
	 * for (Teaching teaching : teachings) { Optional<Course> courseOpt =
	 * courseRepository.findById(teaching.getCourseId()); if (courseOpt.isEmpty())
	 * continue;
	 * 
	 * Course course = courseOpt.get();
	 * 
	 * // students attached to this course List<StudentInfo> students =
	 * getStudentsForCourse(teaching.getCourseId());
	 * 
	 * Long classId = teaching.getClassId(); String className = null; if (classId !=
	 * null) { className =
	 * classRepository.findById(classId).map(ClassEntity::getName) // adjust getter
	 * if needed .orElse(null); }
	 * 
	 * TeacherClassInfo classInfo = new TeacherClassInfo(teaching.getId(),
	 * course.getId(), course.getCourseCode(), course.getName(), course.getCredit(),
	 * teaching.getPeriod(), teaching.getDayOfWeek(), teaching.getClassRoom(),
	 * classId, className, students);
	 * 
	 * teacherClasses.add(classInfo); }
	 * 
	 * logger.info("Found {} classes for lecturer ID: {}", teacherClasses.size(),
	 * lecturerId); return teacherClasses; }
	 */
	/**
	 * Lấy danh sách sinh viên trong một lớp học cụ thể (class tồn tại ,student tồn
	 * tại)
	 */
	public List<StudentInfo> getStudentsForClass(Long teachingId) {
		logger.info("Getting students for teaching ID: {}", teachingId);

		Teaching teaching = teachingRepository.findById(teachingId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp học với ID: " + teachingId));

		return getStudentsForCourse(teaching.getCourseId());
	}

	// Dùng trong teacherportalservice
	public Enrollment saveEnrollment(Enrollment entity) {
		// Compute grades and scores if component scores are present
		if (entity != null) {
			Double totalScore = gradeCalculationService.calculateTotalScore(entity.getComponentScore1(),
					entity.getComponentScore2(), entity.getFinalExamScore());

			if (totalScore != null) {
				entity.setTotalScore(totalScore);
				entity.setScoreCoefficient4(gradeCalculationService.convertToCoefficient4(totalScore));
				entity.setGrade(gradeCalculationService.convertToLetterGrade(totalScore));
			}
		}
		Enrollment saved = enrollmentRepository.save(entity);
		return saved;
	}

	/**
	 * Chấm điểm cho sinh viên (sửa enrollment)
	 */
	public Enrollment gradeStudent(EnrollmentDTO enrollmentDTO) {
		logger.info("Grading student ID: {} for course ID: {} in semester: {}", enrollmentDTO.getStudentId(),
				enrollmentDTO.getCourseId(), enrollmentDTO);

		// Tìm enrollment tương ứng
		List<Enrollment> enrollments = enrollmentRepository.findByStudentId(enrollmentDTO.getStudentId());
		Enrollment enrollment = enrollments.stream()
				.filter(e -> e.getCourseId() != null && e.getCourseId().equals(enrollmentDTO.getCourseId())).findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đăng ký môn học"));

		// Cập nhật điểm thành phần và thi cuối kỳ
		enrollment.setComponentScore1(enrollmentDTO.getComponentScore1());
		enrollment.setComponentScore2(enrollmentDTO.getComponentScore2());
		enrollment.setFinalExamScore(enrollmentDTO.getFinalExamScore());

		// Lưu và để EnrollmentService tính grade
		Enrollment updatedEnrollment = saveEnrollment(enrollment);

		logger.info("Grade updated successfully for student ID: {}", enrollmentDTO.getStudentId());
		return updatedEnrollment;
	}

	/**
	 * Lấy danh sách sinh viên đăng ký một môn học
	 */
	private List<StudentInfo> getStudentsForCourse(Long courseId) {
		List<Enrollment> enrollments = enrollmentRepository.findAll().stream()
				.filter(e -> e.getCourseId() != null && e.getCourseId().equals(courseId)).toList();

		List<StudentInfo> students = new ArrayList<>();
		// lấy kì học
		Optional<Course> courseOpt = courseRepository.findById(courseId);
		Long semesterId = courseOpt.map(Course::getSemesterId).orElse(null);

		for (Enrollment enrollment : enrollments) {
			Optional<Student> studentOpt = studentRepository.findById(enrollment.getStudentId());
			if (studentOpt.isEmpty())
				continue;
			Student student = studentOpt.get();
			Optional<User> userOpt = userRepository.findById(student.getUserId());
			if (userOpt.isEmpty())
				continue;

			User user = userOpt.get();

			String className = "Chưa phân lớp";
			if (student.getClassId() != null) {
				className = classRepository.findById(student.getClassId()).map(ClassEntity::getName)
						.orElse("Lớp " + student.getClassId());
			}

			StudentInfo studentInfo = new StudentInfo(student.getId(), student.getStudentCode(), user.getFullName(),
					user.getEmail(), className, enrollment.getGrade(), enrollment.getComponentScore1(),
					enrollment.getComponentScore2(), enrollment.getFinalExamScore(), semesterId);

			// Set calculated scores
			studentInfo.setTotalScore(enrollment.getTotalScore());
			studentInfo.setScoreCoefficient4(enrollment.getScoreCoefficient4());

			students.add(studentInfo);
		}

		return students;

	}

	/**
	 * Kiểm tra xem giảng viên có được phân công dạy môn học này không
	 */
	public boolean isTeacherAssignedToCourse(Long lecturerId, Long courseId) {
		return teachingRepository.findByLecturerId(lecturerId).stream()
				.anyMatch(t -> t.getCourseId() != null && t.getCourseId().equals(courseId));
	}

	/**
	 * Lấy semesterId mới nhất từ danh sách teachings
	 */
	private Long getLatestSemesterId(List<Teaching> teachings) {
		return teachings.stream().map(Teaching::getCourseId)
				.map(courseId -> courseRepository.findById(courseId).map(Course::getSemesterId).orElse(null))
				.filter(Objects::nonNull).max(Long::compareTo).orElse(1L); // fallback nếu không có
	}

	/**
	 * Lấy danh sách tất cả semesters từ database (để Controller có thể gọi)
	 */
	public List<TeacherPortalInfo.SemesterInfo> getAllSemesters() {
		logger.info("Getting all semesters from database for teacher");

		return semesterRepository.findAll().stream().map(semester -> {
			String displayName = generateDisplayName(semester.getSemester());
			return new TeacherPortalInfo.SemesterInfo(semester.getId(), semester.getSemester(), displayName);
		}).sorted((s1, s2) -> s2.getSemester().compareTo(s1.getSemester())) // Sort descending (newest first)
				.collect(ArrayList::new, (list, item) -> list.add(item), (list1, list2) -> list1.addAll(list2));
	}

	/**
	 * Tạo display name cho semester (ví dụ: 2024-1 -> Học kỳ 1 (2024-2025))
	 */
	private String generateDisplayName(String semester) {
		if (semester == null)
			return "Không xác định";

		try {
			String[] parts = semester.split("-");
			if (parts.length == 2) {
				String year = parts[0];
				String term = parts[1];
				int yearInt = Integer.parseInt(year);

				switch (term) {
				case "1":
					return "Học kỳ 1 (" + year + "-" + (yearInt + 1) + ")";
				case "2":
					return "Học kỳ 2 (" + year + "-" + (yearInt + 1) + ")";
				case "3":
					return "Học kỳ hè (" + year + "-" + (yearInt + 1) + ")";
				default:
					return "Học kỳ " + term + " (" + year + "-" + (yearInt + 1) + ")";
				}
			}
		} catch (Exception e) {
			logger.warn("Could not parse semester: {}", semester);
		}

		return semester; // fallback to original string
	}

	/**
	 * Lấy thông tin cá nhân của giảng viên
	 */
	public TeacherPortalInfo.TeacherProfile getTeacherProfile(Long lecturerId) {
		logger.info("Getting profile for lecturer ID: {}", lecturerId);

		Lecturer lecturer = lecturerRepository.findById(lecturerId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên với ID: " + lecturerId));

		User user = userRepository.findById(lecturer.getUserId()).orElseThrow(
				() -> new ResourceNotFoundException("Không tìm thấy thông tin user cho giảng viên ID: " + lecturerId));

		return new TeacherPortalInfo.TeacherProfile(lecturer.getId(), lecturer.getLecturerCode(), user.getFullName(),
				user.getEmail(), user.getPhone(), "Khoa Công nghệ thông tin", // Default - có thể thêm field vào User
																				// model sau
				"Giảng viên", // Default
				"Công nghệ phần mềm" // Default
		);
	}

	/**
	 * Thay đổi mật khẩu cho giảng viên (không cần mật khẩu hiện tại)
	 */
	public TeacherPortalInfo.ChangePasswordResponse changePassword(Long lecturerId,
			TeacherPortalInfo.ChangePasswordRequest request) {
		logger.info("Changing password for lecturer ID: {}", lecturerId);

		try {
			// Validate input - chỉ cần mật khẩu mới
			if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
				return new TeacherPortalInfo.ChangePasswordResponse(false, "Mật khẩu mới không được để trống");
			}

			if (request.getNewPassword().length() < 6) {
				return new TeacherPortalInfo.ChangePasswordResponse(false, "Mật khẩu mới phải có ít nhất 6 ký tự");
			}

			if (!request.getNewPassword().equals(request.getConfirmPassword())) {
				return new TeacherPortalInfo.ChangePasswordResponse(false, "Xác nhận mật khẩu không khớp");
			}

			// Get lecturer and user
			Lecturer lecturer = lecturerRepository.findById(lecturerId).orElseThrow(
					() -> new ResourceNotFoundException("Không tìm thấy giảng viên với ID: " + lecturerId));

			User user = userRepository.findById(lecturer.getUserId()).orElseThrow(() -> new ResourceNotFoundException(
					"Không tìm thấy thông tin user cho giảng viên ID: " + lecturerId));

			// Mã hóa mật khẩu mới bằng BCrypt
			String encodedPassword = passwordEncoder.encode(request.getNewPassword());

			// Update password với mật khẩu đã mã hóa
			user.setPassword(encodedPassword);
			userRepository.save(user);

			logger.info("Password changed successfully for lecturer ID: {}", lecturerId);
			return new TeacherPortalInfo.ChangePasswordResponse(true, "Đổi mật khẩu thành công");

		} catch (Exception e) {
			logger.error("Error changing password for lecturer ID: {}", lecturerId, e);
			return new TeacherPortalInfo.ChangePasswordResponse(false, "Lỗi hệ thống: " + e.getMessage());
		}
	}

	/**
	 * Xuất bảng điểm lớp học ra file CSV
	 */
	public byte[] exportClassGradesToCsv(Long teachingId, Long lecturerId) {
		try {
			// Lấy danh sách sinh viên trong lớp
			List<StudentInfo> students = getStudentsForClass(teachingId);

			StringBuilder csv = new StringBuilder();
			// Add BOM for UTF-8
			csv.append('\ufeff');

			// Headers
			csv.append("STT,Mã SV,Họ tên,Email,Điểm TP1,Điểm TP2,Điểm thi CK,Điểm tổng kết,Hệ số 4,Điểm chữ\n");

			// Data rows
			for (int i = 0; i < students.size(); i++) {
				StudentInfo student = students.get(i);
				csv.append(i + 1).append(",");
				csv.append(escapeCSV(student.getStudentCode())).append(",");
				csv.append(escapeCSV(student.getFullName())).append(",");
				csv.append(escapeCSV(student.getEmail())).append(",");
				csv.append(student.getComponentScore1() != null ? student.getComponentScore1() : "").append(",");
				csv.append(student.getComponentScore2() != null ? student.getComponentScore2() : "").append(",");
				csv.append(student.getFinalExamScore() != null ? student.getFinalExamScore() : "").append(",");
				csv.append(student.getTotalScore() != null ? student.getTotalScore() : "").append(",");
				csv.append(student.getScoreCoefficient4() != null ? student.getScoreCoefficient4() : "").append(",");
				csv.append(escapeCSV(student.getGrade())).append("\n");
			}

			return csv.toString().getBytes(StandardCharsets.UTF_8);
		} catch (Exception e) {
			logger.error("Error exporting class grades to CSV", e);
			throw new RuntimeException("Error exporting class grades", e);
		}
	}

	/**
	 * Helper method để escape CSV values
	 */
	private String escapeCSV(String value) {
		if (value == null)
			return "";
		if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
			return "\"" + value.replace("\"", "\"\"") + "\"";
		}
		return value;
	}
}