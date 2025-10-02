package com.example.app.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.app.dto.DepartmentDTO;
// DTOs will be defined in this service
import com.example.app.dto.EnrollmentDTO;
import com.example.app.dto.PrincipalPortalInfo;
import com.example.app.dto.PrincipalPortalInfo.ScholarshipCandidate;
import com.example.app.dto.SemesterDTO;
import com.example.app.model.ClassEntity;
import com.example.app.model.Course;
import com.example.app.model.Department;
import com.example.app.model.Enrollment;
import com.example.app.model.Semester;
import com.example.app.model.Student;
import com.example.app.model.User;
import com.example.app.repository.ClassRepository;
import com.example.app.repository.CourseRepository;
import com.example.app.repository.DepartmentRepository;
import com.example.app.repository.EnrollmentRepository;
import com.example.app.repository.SemesterRepository;
import com.example.app.repository.StudentRepository;
import com.example.app.repository.UserRepository;

@Service
public class EnrollmentService {

	private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);

	private final EnrollmentRepository enrollmentRepository;
	private final GradeCalculationService gradeCalculationService;
	private final DepartmentRepository departmentRepository;
	private final StudentRepository studentRepository;
	private final UserRepository userRepository;
	private final ClassRepository classRepository;
	private final CourseRepository courseRepository;
	private final SemesterRepository semesterRepository;

	public EnrollmentService(EnrollmentRepository enrollmentRepository, GradeCalculationService gradeCalculationService,
			DepartmentRepository departmentRepository, StudentRepository studentRepository,
			UserRepository userRepository, ClassRepository classRepository, CourseRepository courseRepository,
			SemesterRepository semesterRepository) {
		this.enrollmentRepository = enrollmentRepository;
		this.gradeCalculationService = gradeCalculationService;
		this.departmentRepository = departmentRepository;
		this.studentRepository = studentRepository;
		this.userRepository = userRepository;
		this.classRepository = classRepository;
		this.courseRepository = courseRepository;
		this.semesterRepository = semesterRepository;
	}

	private EnrollmentDTO convertToDTO(Enrollment entity) {
		return new EnrollmentDTO(entity.getId(), entity.getStudentId(), entity.getCourseId(), entity.getGrade(),
				entity.getComponentScore1(), entity.getComponentScore2(), entity.getFinalExamScore(),
				entity.getTotalScore(), entity.getScoreCoefficient4());
	}

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
	 * Lấy danh sách tất cả khoa từ database Lấy các departments có ít nhất 1 user
	 * (student hoặc lecturer)
	 */
	private DepartmentDTO convertToDTO(Department entity) {
		return new DepartmentDTO(entity.getId(), entity.getName(), entity.getCode());
	}

	public List<DepartmentDTO> getAllDepartments() {
		logger.info("Getting all departments from database");
		try {
			return departmentRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("Error getting departments from database", e);
			// Return empty list instead of mock data to force real database usage
			return new ArrayList<>();
		}
	}

	/**
	 * Lấy danh sách tất cả học kỳ từ database
	 */
	// Convert Entity -> DTO
	private SemesterDTO convertToDTO(Semester entity) {
		return new SemesterDTO(entity.getId(), entity.getSemester());
	}

	public List<SemesterDTO> getAllSemesters() {
		logger.info("Getting all semesters from database");
		try {
			return semesterRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("Error getting semesters from database", e);
			// Return empty list instead of mock data to force real database usage
			return new ArrayList<>();
		}
	}

	// Helper class để tính GPA
	private static class StudentGpaInfo {
		private Long studentId;
		private String studentCode;
		private String fullName;
		private Long departmentId;
		private Long classId;
		private String semester;
		private double totalScoreSum = 0;
		private int totalCredits = 0;
		private int completedCredits = 0;

		public void addScore(double score, int credits) {
			totalScoreSum += score * credits;
			totalCredits += credits;
			// Điểm qua môn >= 5.0 (thang điểm 10)
			if (score >= 5.0) {
				completedCredits += credits;
			}
		}

		public double getGpa() {
			if (totalCredits <= 0)
				return 0.0;
			// Tính GPA trung bình có trọng số theo tín chỉ
			// totalScoreSum đã là tổng (điểm * tín chỉ)
			// Chia cho totalCredits để có điểm trung bình
			// Chia 10 nhân 4 để chuyển từ thang 10 sang thang 4
			double averageScore = totalScoreSum / totalCredits;
			return averageScore / 10.0 * 4.0; // Convert to 4.0 scale
		}

		public void setStudentInfo(Student student, User user, String semester) {
			this.studentId = student.getId();
			this.studentCode = student.getStudentCode();
			this.fullName = user.getFullName();
			this.departmentId = user.getDepartmentId();
			this.classId = student.getClassId();
			this.semester = semester;
		}

		// Getters
		public Long getStudentId() {
			return studentId;
		}

		public String getStudentCode() {
			return studentCode;
		}

		public String getFullName() {
			return fullName;
		}

		public Long getDepartmentId() {
			return departmentId;
		}

		public Long getClassId() {
			return classId;
		}

		public String getSemester() {
			return semester;
		}

		public int getTotalCredits() {
			return totalCredits;
		}

		public int getCompletedCredits() {
			return completedCredits;
		}
	}

	/**
	 * Lấy top 20 sinh viên có điểm cao nhất theo khoa và kỳ học để trao học bổng
	 * Tính GPA dựa trên trung bình total_score của các môn trong 1 kỳ học cụ thể
	 * 
	 * Database schema: enrollments -> students -> users -> departments enrollments
	 * -> courses -> semesters students -> classes
	 */
	public List<PrincipalPortalInfo.ScholarshipCandidate> getTopStudentsForScholarship(Long departmentId,
			String semester) {
		logger.info("Getting top students for scholarship - Department: {}, Semester: {}", departmentId, semester);

		try {
			// Map để lưu GPA của từng sinh viên theo semester
			Map<Long, StudentGpaInfo> studentGpaMap = new HashMap<>();

			// Lấy tất cả enrollments có điểm
			List<Enrollment> allEnrollments = enrollmentRepository.findAll();
			logger.info("Found {} total enrollments", allEnrollments.size());

			int processedEnrollments = 0;
			int validEnrollments = 0;

			for (Enrollment enrollment : allEnrollments) {
				processedEnrollments++;

				// Skip nếu không có điểm
				if (enrollment.getTotalScore() == null || enrollment.getTotalScore() <= 0) {
					continue;
				}

				// Lấy thông tin course
				Course course = courseRepository.findById(enrollment.getCourseId()).orElse(null);
				if (course == null) {
					logger.debug("Course not found for enrollment {}", enrollment.getId());
					continue;
				}

				// Lấy thông tin semester từ course
				Semester semesterObj = null;
				if (course.getSemesterId() != null) {
					semesterObj = semesterRepository.findById(course.getSemesterId()).orElse(null);
				}
				if (semesterObj == null) {
					logger.debug("Semester not found for course {}", course.getId());
					continue;
				}

				// Filter theo semester nếu có
				if (semester != null && !semester.trim().isEmpty() && !semester.equals(semesterObj.getSemester())) {
					continue;
				}

				// Lấy thông tin student
				Student student = studentRepository.findById(enrollment.getStudentId()).orElse(null);
				if (student == null) {
					logger.debug("Student not found for enrollment {}", enrollment.getId());
					continue;
				}

				// Lấy thông tin user từ student
				User user = userRepository.findById(student.getUserId()).orElse(null);
				if (user == null) {
					logger.debug("User not found for student {}", student.getId());
					continue;
				}

				// Filter theo department nếu có
				if (departmentId != null && !departmentId.equals(user.getDepartmentId())) {
					continue;
				}

				validEnrollments++;

				// Tính toán GPA cho sinh viên này
				Long studentId = enrollment.getStudentId();
				StudentGpaInfo gpaInfo = studentGpaMap.computeIfAbsent(studentId, k -> new StudentGpaInfo());
				gpaInfo.addScore(enrollment.getTotalScore(), course.getCredit());
				gpaInfo.setStudentInfo(student, user, semesterObj.getSemester());
			}

			logger.info("Processed {}/{} enrollments, {} valid for GPA calculation", validEnrollments,
					processedEnrollments, studentGpaMap.size());

			// Tạo danh sách candidates và sắp xếp theo GPA
			List<PrincipalPortalInfo.ScholarshipCandidate> candidates = studentGpaMap.values().stream()
					.filter(info -> info.getTotalCredits() > 0) // Chỉ lấy sinh viên có môn học
					.map(info -> {
						// Lấy thông tin department
						String departmentName = "Unknown";
						if (info.getDepartmentId() != null) {
							Department dept = departmentRepository.findById(info.getDepartmentId()).orElse(null);
							if (dept != null) {
								departmentName = dept.getName();
							}
						}

						// Lấy thông tin class
						String className = "Unknown";
						if (info.getClassId() != null) {
							ClassEntity classEntity = classRepository.findById(info.getClassId()).orElse(null);
							if (classEntity != null) {
								className = classEntity.getName();
							}
						}

						return new ScholarshipCandidate(info.getStudentId(), info.getStudentCode(), info.getFullName(),
								className, departmentName, info.getGpa(), info.getTotalCredits(),
								info.getCompletedCredits(), info.getSemester(), 0 // rank sẽ được set sau
						);
					}).sorted(Comparator.comparing(ScholarshipCandidate::getGpa).reversed()).limit(20)
					.collect(Collectors.toList());

			// Set rank
			for (int i = 0; i < candidates.size(); i++) {
				candidates.get(i).setRank(i + 1);
			}

			logger.info("Found {} scholarship candidates", candidates.size());

			// Nếu không có data thật, fallback to mock data
			if (candidates.isEmpty()) {
				logger.warn("No real scholarship candidates found, using mock data");
				return null;
			}

			return candidates;

		} catch (Exception e) {
			logger.error("Error getting top students for scholarship", e);
			// Return empty list instead of mock data to encourage fixing database issues
			return new ArrayList<>();
		}
	}

	/**
	 * Xuất danh sách học bổng ra CSV
	 */
	public byte[] exportScholarshipListToCsv(Long departmentId, String semester) {
		try {
			List<PrincipalPortalInfo.ScholarshipCandidate> candidates = getTopStudentsForScholarship(departmentId,
					semester);

			StringBuilder csv = new StringBuilder();
			// Add BOM for UTF-8
			csv.append('\ufeff');

			// Headers
			csv.append("Hạng,Mã SV,Họ tên,Lớp,Khoa,GPA,Tổng TC,TC hoàn thành,Học kỳ\n");

			// Data rows
			for (PrincipalPortalInfo.ScholarshipCandidate candidate : candidates) {
				csv.append(candidate.getRank()).append(",");
				csv.append(escapeCSV(candidate.getStudentCode())).append(",");
				csv.append(escapeCSV(candidate.getFullName())).append(",");
				csv.append(escapeCSV(candidate.getClassName())).append(",");
				csv.append(escapeCSV(candidate.getDepartmentName())).append(",");
				csv.append(String.format("%.2f", candidate.getGpa())).append(",");
				csv.append(candidate.getTotalCredits()).append(",");
				csv.append(candidate.getCompletedCredits()).append(",");
				csv.append(escapeCSV(candidate.getSemester())).append("\n");
			}

			return csv.toString().getBytes(StandardCharsets.UTF_8);
		} catch (Exception e) {
			logger.error("Error exporting scholarship list to CSV", e);
			throw new RuntimeException("Error exporting scholarship list", e);
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
