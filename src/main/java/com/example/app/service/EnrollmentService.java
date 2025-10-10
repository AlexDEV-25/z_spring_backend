package com.example.app.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.app.dto.DepartmentDTO;
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

	private final EnrollmentRepository enrollmentRepository;
	private final DepartmentRepository departmentRepository;
	private final StudentRepository studentRepository;
	private final UserRepository userRepository;
	private final ClassRepository classRepository;
	private final CourseRepository courseRepository;
	private final SemesterRepository semesterRepository;

	public EnrollmentService(EnrollmentRepository enrollmentRepository, DepartmentRepository departmentRepository,
			StudentRepository studentRepository, UserRepository userRepository, ClassRepository classRepository,
			CourseRepository courseRepository, SemesterRepository semesterRepository) {
		this.enrollmentRepository = enrollmentRepository;

		this.departmentRepository = departmentRepository;
		this.studentRepository = studentRepository;
		this.userRepository = userRepository;
		this.classRepository = classRepository;
		this.courseRepository = courseRepository;
		this.semesterRepository = semesterRepository;
	}

	// Convert Entity -> DTO
	private DepartmentDTO convertToDTO(Department entity) {
		return new DepartmentDTO(entity.getId(), entity.getName(), entity.getCode());
	}

	// Lấy danh sách tất cả khoa từ database
	public List<DepartmentDTO> getAllDepartments() {
		try {
			return departmentRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	// Convert Entity -> DTO
	private SemesterDTO convertToDTO(Semester entity) {
		return new SemesterDTO(entity.getId(), entity.getSemester());
	}

	// Lấy danh sách tất cả học kỳ từ database
	public List<SemesterDTO> getAllSemesters() {
		try {
			return semesterRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public List<PrincipalPortalInfo.ScholarshipCandidate> getStudentsEligibleForScholarship(Long departmentId,
			String semester) {
		try {
			// B1: lấy GPA cho sinh viên (theo department + semester)
			Map<Long, PrincipalPortalInfo.StudentGpaInfo> studentGpaMap = calculateStudentGpaForEachStudent(
					departmentId, semester);

			// B2: convert sang danh sách ScholarshipCandidate và lọc theo điều kiện học bổng
			List<PrincipalPortalInfo.ScholarshipCandidate> candidates = getScholarshipCandidatesFromGpaMap(
					studentGpaMap);
			printAllStudentScores(departmentId, semester);
			return candidates.isEmpty() ? new ArrayList<>() : candidates;

		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	private Map<Long, PrincipalPortalInfo.StudentGpaInfo> calculateStudentGpaForEachStudent(Long departmentId,
			String semester) {
		Map<Long, PrincipalPortalInfo.StudentGpaInfo> studentGpaMap = new HashMap<>();
		List<Enrollment> allEnrollments = enrollmentRepository.findAll();

		for (Enrollment enrollment : allEnrollments) {
			if (!isValidEnrollment(enrollment))
				continue;

			Course course = courseRepository.findById(enrollment.getCourseId()).orElse(null);
			if (course == null)
				continue;

			Semester semesterObj = getSemester(course);
			if (semesterObj == null || !matchesSemester(semester, semesterObj))
				continue;

			Student student = studentRepository.findById(enrollment.getStudentId()).orElse(null);
			if (student == null)
				continue;

			User user = userRepository.findById(student.getUserId()).orElse(null);
			if (user == null || !matchesDepartment(departmentId, user))
				continue;

			// Tính toán GPA
			Long studentId = enrollment.getStudentId();
			PrincipalPortalInfo.StudentGpaInfo gpaInfo = studentGpaMap.computeIfAbsent(studentId,
					k -> new PrincipalPortalInfo.StudentGpaInfo());
			gpaInfo.addScore(enrollment.getScoreCoefficient4(), course.getCredit(), enrollment.getTotalScore());
			gpaInfo.setStudentInfo(student, user, semesterObj.getSemester());
		}
		return studentGpaMap;
	}

	private List<PrincipalPortalInfo.ScholarshipCandidate> getScholarshipCandidatesFromGpaMap(
			Map<Long, PrincipalPortalInfo.StudentGpaInfo> studentGpaMap) {

		return studentGpaMap.values().stream()
				.filter(info -> info.getGpa() >= 3.6 && info.getTotalCredits() >= 18)
				.map(this::convertToScholarshipCandidate)
				.sorted(Comparator.comparing(ScholarshipCandidate::getGpa).reversed()).collect(Collectors.toList());
	}

	private ScholarshipCandidate convertToScholarshipCandidate(PrincipalPortalInfo.StudentGpaInfo info) {
		String departmentName = getDepartmentName(info.getDepartmentId());
		String className = getClassName(info.getClassId());

		// Kiểm tra điều kiện học bổng: GPA >= 3.6 và tổng tín chỉ >= 18
		boolean eligibleForScholarship = info.getGpa() >= 3.6 && info.getTotalCredits() >= 18;

		return new ScholarshipCandidate(info.getStudentId(), info.getStudentCode(), info.getFullName(), className,
				departmentName, info.getGpa(), info.getTotalCredits(), info.getCompletedCredits(), info.getSemester(),
				eligibleForScholarship);
	}

	private boolean isValidEnrollment(Enrollment enrollment) {
		return enrollment.getTotalScore() != null && enrollment.getTotalScore() > 0 
			&& enrollment.getScoreCoefficient4() != null && enrollment.getScoreCoefficient4() > 0;
	}

	private Semester getSemester(Course course) {
		return (course.getSemesterId() != null) ? semesterRepository.findById(course.getSemesterId()).orElse(null)
				: null;
	}

	private boolean matchesSemester(String semester, Semester semesterObj) {
		return semester == null || semester.trim().isEmpty() || semester.equals(semesterObj.getSemester());
	}

	private boolean matchesDepartment(Long departmentId, User user) {
		return departmentId == null || departmentId.equals(user.getDepartmentId());
	}

	private String getDepartmentName(Long departmentId) {
		if (departmentId == null)
			return "Unknown";
		return departmentRepository.findById(departmentId).map(Department::getName).orElse("Unknown");
	}

	private String getClassName(Long classId) {
		if (classId == null)
			return "Unknown";
		return classRepository.findById(classId).map(ClassEntity::getName).orElse("Unknown");
	}

	// Xuất danh sách học bổng ra CSV
	public byte[] exportScholarshipListToCsv(Long departmentId, String semester) {
		try {
			List<PrincipalPortalInfo.ScholarshipCandidate> candidates = getStudentsEligibleForScholarship(departmentId,
					semester);

			StringBuilder csv = new StringBuilder();
			// Add BOM for UTF-8
			csv.append('\ufeff');

			// Headers
			csv.append("Hạng,Mã SV,Họ tên,Lớp,Khoa,GPA,Tổng TC,TC hoàn thành,Học kỳ,Đủ điều kiện học bổng (GPA >= 3.6 & TC >= 18)\n");

			// Data rows
			for (PrincipalPortalInfo.ScholarshipCandidate candidate : candidates) {
				csv.append(escapeCSV(candidate.getStudentCode())).append(",");
				csv.append(escapeCSV(candidate.getFullName())).append(",");
				csv.append(escapeCSV(candidate.getClassName())).append(",");
				csv.append(escapeCSV(candidate.getDepartmentName())).append(",");
				csv.append(String.format("%.2f", candidate.getGpa())).append(",");
				csv.append(candidate.getTotalCredits()).append(",");
				csv.append(candidate.getCompletedCredits()).append(",");
				csv.append(escapeCSV(candidate.getSemester())).append(",");
				csv.append(candidate.getEligibleForScholarship() ? "Có (GPA >= 3.6 & TC >= 18)" : "Không").append("\n");
			}

			return csv.toString().getBytes(StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException("Error exporting scholarship list", e);
		}
	}

	// Helper method để escape CSV values
	private String escapeCSV(String value) {
		if (value == null)
			return "";
		if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
			return "\"" + value.replace("\"", "\"\"") + "\"";
		}
		return value;
	}

	public void printAllStudentScores(Long departmentId, String semester) {
		Map<Long, PrincipalPortalInfo.StudentGpaInfo> studentGpaMap = calculateStudentGpaForEachStudent(departmentId,
				semester);

		for (PrincipalPortalInfo.StudentGpaInfo info : studentGpaMap.values()) {
			System.out.println("MSSV: " + info.getStudentCode() + ", Tên: " + info.getFullName() + ", GPA: "
					+ String.format("%.2f", info.getGpa()) + ", Tổng TC: " + info.getTotalCredits());
		}
	}

	// Tính toán statistics cho scholarship candidates - Backend xử lý thay vì frontend
	public PrincipalPortalInfo.ScholarshipStatistics getScholarshipStatistics(Long departmentId, String semester) {
		List<PrincipalPortalInfo.ScholarshipCandidate> candidates = getStudentsEligibleForScholarship(departmentId, semester);
		
		if (candidates.isEmpty()) {
			return new PrincipalPortalInfo.ScholarshipStatistics(0, 0.0, 0.0, 0.0, 0);
		}

		// Tính toán statistics
		int totalCandidates = candidates.size();
		
		// GPA trung bình
		double averageGPA = candidates.stream()
			.mapToDouble(PrincipalPortalInfo.ScholarshipCandidate::getGpa)
			.average()
			.orElse(0.0);
		
		// GPA cao nhất
		double topGPA = candidates.stream()
			.mapToDouble(PrincipalPortalInfo.ScholarshipCandidate::getGpa)
			.max()
			.orElse(0.0);
		
		// Tỷ lệ hoàn thành trung bình
		double averageCompletionRate = candidates.stream()
			.mapToDouble(candidate -> {
				if (candidate.getTotalCredits() == 0) return 0.0;
				return (double) candidate.getCompletedCredits() / candidate.getTotalCredits() * 100;
			})
			.average()
			.orElse(0.0);
		
		// Số sinh viên đủ điều kiện học bổng (GPA >= 3.6)
		int totalEligibleForScholarship = (int) candidates.stream()
			.filter(candidate -> candidate.getEligibleForScholarship() != null && candidate.getEligibleForScholarship())
			.count();

		return new PrincipalPortalInfo.ScholarshipStatistics(
			totalCandidates, 
			Math.round(averageGPA * 100.0) / 100.0, // Round to 2 decimal places
			Math.round(topGPA * 100.0) / 100.0,
			Math.round(averageCompletionRate * 100.0) / 100.0,
			totalEligibleForScholarship
		);
	}

}
