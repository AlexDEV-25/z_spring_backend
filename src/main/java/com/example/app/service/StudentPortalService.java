package com.example.app.service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.app.dto.StudentPortalInfo;
import com.example.app.enumvalue.Status;
import com.example.app.model.ClassEntity;
import com.example.app.model.Course;
import com.example.app.model.Enrollment;
import com.example.app.model.Lecturer;
import com.example.app.model.Payment;
import com.example.app.model.Semester;
import com.example.app.model.Student;
import com.example.app.model.Teaching;
import com.example.app.model.User;
import com.example.app.repository.ClassRepository;
import com.example.app.repository.CourseRepository;
import com.example.app.repository.EnrollmentRepository;
import com.example.app.repository.LecturerRepository;
import com.example.app.repository.PaymentRepository;
import com.example.app.repository.SemesterRepository;
import com.example.app.repository.StudentRepository;
import com.example.app.repository.TeachingRepository;
import com.example.app.repository.UserRepository;

/**
 * Student Portal Service
 */
@Service
public class StudentPortalService {

	private static final Logger logger = LoggerFactory.getLogger(StudentPortalService.class);

	private final StudentRepository studentRepository;
	private final UserRepository userRepository;
	private final EnrollmentRepository enrollmentRepository;
	private final CourseRepository courseRepository;
	private final TeachingRepository teachingRepository;
	private final LecturerRepository lecturerRepository;
	private final ClassRepository classRepository;
	private final SemesterRepository semesterRepository;
	private final PaymentRepository paymentRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final GradeCalculationService gradeCalculationService;

	public StudentPortalService(StudentRepository studentRepository, UserRepository userRepository,
			EnrollmentRepository enrollmentRepository, CourseRepository courseRepository,
			TeachingRepository teachingRepository, LecturerRepository lecturerRepository,
			ClassRepository classRepository, SemesterRepository semesterRepository, PaymentRepository paymentRepository,
			GradeCalculationService gradeCalculationService) {
		this.studentRepository = studentRepository;
		this.userRepository = userRepository;
		this.enrollmentRepository = enrollmentRepository;
		this.courseRepository = courseRepository;
		this.teachingRepository = teachingRepository;
		this.lecturerRepository = lecturerRepository;
		this.classRepository = classRepository;
		this.semesterRepository = semesterRepository;
		this.paymentRepository = paymentRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
		this.gradeCalculationService = gradeCalculationService;
	}

	/**
	 * Lấy thời khóa biểu của sinh viên(course và teaching đã tồn tại)
	 */
	public StudentPortalInfo.StudentScheduleInfo getStudentSchedule(Long studentId, String semester) {
		logger.info("Getting schedule for student ID: {} in semester: {}", studentId, semester);

		Student student = getStudentById(studentId);
		User user = getUserById(student.getUserId());

		// Lấy class name từ ClassEntity
		final String className;
		if (student.getClassId() != null) {
			ClassEntity classEntity = classRepository.findById(student.getClassId()).orElse(null);
			className = (classEntity != null) ? classEntity.getName() : "Chưa phân lớp";
		} else {
			className = "Chưa phân lớp";
		}

		// Xác định semesterId để lọc (sử dụng semester được truyền vào hoặc lấy mới
		// nhất)
		Long targetSemesterId;
		if (semester != null && !semester.trim().isEmpty()) {
			// Tìm semesterId từ semester string
			targetSemesterId = semesterRepository.findAll().stream().filter(s -> s.getSemester().equals(semester))
					.map(s -> s.getId()).findFirst().orElse(getLatestSemesterId());
		} else {
			targetSemesterId = getLatestSemesterId();
		}

		logger.info("Filtering schedule by semesterId: {}", targetSemesterId);

		// Lấy danh sách enrollment của sinh viên
		List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

		// Chuyển đổi thành schedule items, chỉ lấy courses thuộc semester được chọn
		List<StudentPortalInfo.ScheduleItem> scheduleItems = enrollments.stream().map(enrollment -> {
			Course course = courseRepository.findById(enrollment.getCourseId()).orElse(null);
			if (course == null)
				return null;

			// Lọc chỉ lấy courses thuộc semester được chọn
			if (course.getSemesterId() == null || !course.getSemesterId().equals(targetSemesterId)) {
				return null;
			}

			// Tìm teaching info
			Teaching teaching = teachingRepository.findAll().stream()
					.filter(t -> t.getCourseId() != null && t.getCourseId().equals(course.getId())).findFirst()
					.orElse(null);
			String lecturerName = "Chưa phân công";
			if (teaching != null) {
				Lecturer lecturer = lecturerRepository.findById(teaching.getLecturerId()).orElse(null);
				if (lecturer != null) {
					User lecturerUser = userRepository.findById(lecturer.getUserId()).orElse(null);
					if (lecturerUser != null) {
						lecturerName = lecturerUser.getFullName();
					}
				}
			}

			return new StudentPortalInfo.ScheduleItem(course.getId(), course.getCourseCode(), course.getName(),
					course.getCredit(), teaching != null ? teaching.getPeriod() : "1-2",
					teaching != null ? teaching.getDayOfWeek() : "Thứ 2", lecturerName, className,
					teaching != null ? (teaching.getClassRoom() != null ? teaching.getClassRoom() : "Chưa xác định")
							: "Chưa xác định");
		}).filter(item -> item != null).collect(Collectors.toList());

		int totalCredits = scheduleItems.stream().mapToInt(StudentPortalInfo.ScheduleItem::getCredit).sum();

		return new StudentPortalInfo.StudentScheduleInfo(studentId, student.getStudentCode(), user.getFullName(),
				semester, totalCredits, scheduleItems);
	}

	/**
	 * Lấy bảng điểm của sinh viên (theo học phần đã đăng ký của semester được chọn)
	 */
	public StudentPortalInfo.StudentGradesInfo getStudentGrades(Long studentId, String semester) {
		logger.info("Getting grades for student ID: {} in semester: {}", studentId, semester);

		Student student = getStudentById(studentId);
		User user = getUserById(student.getUserId());

		// Xác định semesterId để lọc (sử dụng semester được truyền vào hoặc lấy mới
		// nhất)
		Long targetSemesterId;
		String targetSemesterString;
		if (semester != null && !semester.trim().isEmpty()) {
			// Tìm semesterId từ semester string
			targetSemesterId = semesterRepository.findAll().stream().filter(s -> s.getSemester().equals(semester))
					.map(s -> s.getId()).findFirst().orElse(getLatestSemesterId());
			targetSemesterString = semester;
		} else {
			targetSemesterId = getLatestSemesterId();
			targetSemesterString = getSemesterStringById(targetSemesterId);
		}

		logger.info("Filtering grades by semesterId: {} ({})", targetSemesterId, targetSemesterString);

		// Lấy danh sách enrollment của sinh viên
		List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

		// Chuyển đổi thành grade items, chỉ lấy courses thuộc semester được chọn
		List<StudentPortalInfo.GradeItem> gradeItems = enrollments.stream().map(enrollment -> {
			Course course = courseRepository.findById(enrollment.getCourseId()).orElse(null);
			if (course == null)
				return null;

			// Lọc chỉ lấy courses thuộc semester được chọn
			if (course.getSemesterId() == null || !course.getSemesterId().equals(targetSemesterId)) {
				return null;
			}

			return new StudentPortalInfo.GradeItem(course.getId(), course.getCourseCode(), course.getName(),
					course.getCredit(), enrollment.getComponentScore1(), // Lấy điểm thành phần 1
					enrollment.getComponentScore2(), // Lấy điểm thành phần 2
					enrollment.getFinalExamScore(), // Lấy điểm thi cuối kỳ
					enrollment.getTotalScore(), // Lấy điểm tổng kết
					enrollment.getScoreCoefficient4(), // Lấy điểm hệ số 4
					enrollment.getGrade(), targetSemesterString, // semester thực tế
					enrollment.getGrade() != null ? "Đã hoàn thành" : "Đang học");
		}).filter(item -> item != null).collect(Collectors.toList());

		// Tính toán GPA và credits
		int totalCredits = gradeItems.stream().mapToInt(StudentPortalInfo.GradeItem::getCredit).sum();
		int completedCredits = gradeItems.stream().filter(item -> item.getGrade() != null)
				.mapToInt(StudentPortalInfo.GradeItem::getCredit).sum();

		double gpa = 3.0; // Mock GPA - sẽ implement sau

		// Tính statistics
		int totalCourses = gradeItems.size();
		int completedCourses = (int) gradeItems.stream().filter(item -> "Đã hoàn thành".equals(item.getStatus()))
				.count();
		int inProgressCourses = (int) gradeItems.stream().filter(item -> "Đang học".equals(item.getStatus())).count();

		return new StudentPortalInfo.StudentGradesInfo(studentId, student.getStudentCode(), user.getFullName(), gpa,
				totalCredits, completedCredits, gradeItems, totalCourses, completedCourses, inProgressCourses);
	}

	/**
	 * Lấy danh sách môn học có thể đăng ký (chỉ của kỳ mới nhất) Logic: Lọc courses
	 * theo semesterId mới nhất từ bảng courses và có teaching SemesterId mới nhất
	 * được xác định bằng semesterId cao nhất trong bảng courses
	 */
	public List<StudentPortalInfo.AvailableCourseInfo> getAvailableCourses(Long studentId, String semester) {
		logger.info("Getting available courses for student ID: {} in semester: {}", studentId, semester);
		logger.info("Note: Only courses from latest semester will be shown");

		// Student student = getStudentById(studentId);

		// Xác định semesterId mới nhất để lọc
		Long latestSemesterId = getLatestSemesterId();
		String latestSemester = getSemesterStringById(latestSemesterId);
		logger.info("Using latest semesterId: {} ({})", latestSemesterId, latestSemester);

		// Lấy tất cả teachings để xác định courses nào có teaching
		List<Teaching> allTeachings = teachingRepository.findAll();
		Set<Long> coursesWithTeaching = allTeachings.stream().map(Teaching::getCourseId).collect(Collectors.toSet());

		// Lọc courses theo semesterId mới nhất và có teaching
		List<Course> availableCourses = courseRepository.findAll().stream()
				.filter(course -> course.getSemesterId() != null && course.getSemesterId().equals(latestSemesterId)) // Lọc
																														// theo
																														// semesterId
																														// mới
																														// nhất
				.filter(course -> coursesWithTeaching.contains(course.getId())) // Chỉ lấy courses có teaching
				.collect(Collectors.toList());

		// Lấy danh sách đã đăng ký của student
		List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
		Set<Long> enrolledCourseIds = enrollments.stream().map(Enrollment::getCourseId).collect(Collectors.toSet());

		return availableCourses.stream().map(course -> {
			// Đếm số lượng đã đăng ký cho course này
			Long enrolledCount = enrollmentRepository.findAll().stream()
					.filter(e -> e.getCourseId().equals(course.getId())).count();

			// Sử dụng slot từ course hoặc fallback về 50
			int maxSlots = course.getSlot() != null ? course.getSlot() : 50;
			int availableSlots = maxSlots - enrolledCount.intValue();

			// Kiểm tra trạng thái đăng ký
			boolean isEnrolled = enrolledCourseIds.contains(course.getId());
			boolean canRegister = !isEnrolled && availableSlots > 0;
			String reason = "";

			if (isEnrolled) {
				reason = "Đã đăng ký";
			} else if (availableSlots <= 0) {
				canRegister = false;
				reason = "Hết slot";
			}

			// Lấy thông tin giảng viên từ teaching
			String lecturerName = "Chưa phân công";
			String period = "Chưa xác định";
			String dayOfWeek = "Chưa xác định";
			String classroom = "Chưa xác định";

			Teaching teaching = teachingRepository.findAll().stream()
					.filter(t -> t.getCourseId().equals(course.getId())).findFirst().orElse(null);

			if (teaching != null) {
				period = teaching.getPeriod() != null ? teaching.getPeriod() : "Chưa xác định";
				dayOfWeek = teaching.getDayOfWeek() != null ? teaching.getDayOfWeek() : "Chưa xác định";
				classroom = teaching.getClassRoom() != null ? teaching.getClassRoom() : "Chưa xác định";

				if (teaching.getLecturerId() != null) {
					Lecturer lecturer = lecturerRepository.findById(teaching.getLecturerId()).orElse(null);
					if (lecturer != null) {
						User lecturerUser = userRepository.findById(lecturer.getUserId()).orElse(null);
						if (lecturerUser != null) {
							lecturerName = lecturerUser.getFullName();
						}
					}
				}
			}

			// Kiểm tra có thể hủy đăng ký không (chỉ courses đã đăng ký và chưa có điểm)
			boolean canUnregister = false;
			if (isEnrolled) {
				// Kiểm tra xem enrollment có điểm chưa
				Enrollment enrollment = enrollments.stream().filter(e -> e.getCourseId().equals(course.getId()))
						.findFirst().orElse(null);
				canUnregister = enrollment != null && enrollment.getGrade() == null;
			}

			return new StudentPortalInfo.AvailableCourseInfo(course.getId(), course.getCourseCode(), course.getName(),
					course.getCredit(), canRegister, reason, availableSlots, maxSlots, lecturerName, period, dayOfWeek,
					classroom, latestSemester, canUnregister);
		}).collect(Collectors.toList());
	}

	/**
	 * Đăng ký môn học (trả về thông báo có đăng ký được không, tạo enrollment mới)
	 */
	public StudentPortalInfo.CourseRegistrationResponse registerCourse(Long studentId, Long courseId, String semester) {
		logger.info("Registering course {} for student ID: {}", courseId, studentId);

		try {
			// Kiểm tra sinh viên tồn tại
			// Student student = getStudentById(studentId);

			// Kiểm tra môn học tồn tại
			Course course = courseRepository.findById(courseId)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy môn học"));

			// Kiểm tra đã đăng ký chưa
			boolean alreadyEnrolled = enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
			if (alreadyEnrolled) {
				return new StudentPortalInfo.CourseRegistrationResponse(false, "Đã đăng ký môn học này rồi");
			}

			// Tạo enrollment mới
			Enrollment enrollment = new Enrollment();
			enrollment.setStudentId(studentId);
			enrollment.setCourseId(courseId);
			enrollment.setGrade(null);
			enrollmentRepository.save(enrollment);

			return new StudentPortalInfo.CourseRegistrationResponse(true,
					"Đăng ký môn học " + course.getName() + " thành công");

		} catch (Exception e) {
			logger.error("Error registering course: ", e);
			return new StudentPortalInfo.CourseRegistrationResponse(false, "Lỗi đăng ký: " + e.getMessage());
		}
	}

	/**
	 * Hủy đăng ký môn học (chưa viết gia diện, tính sau)
	 */
	public StudentPortalInfo.CourseRegistrationResponse unregisterCourse(Long studentId, Long courseId) {
		logger.info("Unregistering course {} for student ID: {}", courseId, studentId);

		try {
			// Kiểm tra enrollment tồn tại
			Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy đăng ký môn học"));

			// Kiểm tra đã có điểm chưa
			if (enrollment.getGrade() != null) {
				return new StudentPortalInfo.CourseRegistrationResponse(false, "Không thể hủy môn học đã có điểm");
			}

			// Xóa enrollment
			enrollmentRepository.delete(enrollment);

			Course course = courseRepository.findById(courseId).orElse(null);
			String courseName = course != null ? course.getName() : "môn học";

			return new StudentPortalInfo.CourseRegistrationResponse(true, "Hủy đăng ký " + courseName + " thành công");

		} catch (Exception e) {
			logger.error("Error unregistering course: ", e);
			return new StudentPortalInfo.CourseRegistrationResponse(false, "Lỗi hủy đăng ký: " + e.getMessage());
		}
	}

	// Helper methods
	private Student getStudentById(Long studentId) {
		return studentRepository.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với ID: " + studentId));
	}

	private User getUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin user"));
	}

	/**
	 * Lấy semesterId mới nhất từ các courses có sẵn
	 */
	private Long getLatestSemesterId() {
		// Lấy semesterId cao nhất từ bảng courses
		return courseRepository.findAll().stream().map(Course::getSemesterId).filter(semesterId -> semesterId != null)
				.max(Long::compareTo).orElse(1L); // fallback nếu không có
	}

	/**
	 * Lấy semester string từ semesterId
	 */
	private String getSemesterStringById(Long semesterId) {
		if (semesterId == null)
			return "2024-1";

		return semesterRepository.findById(semesterId).map(Semester::getSemester).orElse("2024-1");
	}

	/**
	 * Public method để Controller có thể gọi lấy semester mới nhất
	 */
	public String getLatestSemesterInfo() {
		Long latestSemesterId = getLatestSemesterId();
		return getSemesterStringById(latestSemesterId);
	}

	/**
	 * Lấy danh sách tất cả semesters từ database
	 */
	public List<StudentPortalInfo.SemesterInfo> getAllSemesters() {
		logger.info("Getting all semesters from database");

		return semesterRepository.findAll().stream().map(semester -> {
			String displayName = generateDisplayName(semester.getSemester());
			return new StudentPortalInfo.SemesterInfo(semester.getId(), semester.getSemester(), displayName);
		}).sorted((s1, s2) -> s2.getSemester().compareTo(s1.getSemester())) // Sort descending (newest first)
				.collect(Collectors.toList());
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
	 * Lấy thông tin cá nhân của sinh viên
	 */
	public StudentPortalInfo.StudentProfile getStudentProfile(Long studentId) {
		logger.info("Getting profile for student ID: {}", studentId);

		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với ID: " + studentId));

		User user = userRepository.findById(student.getUserId()).orElseThrow(
				() -> new RuntimeException("Không tìm thấy thông tin user cho sinh viên ID: " + studentId));

		// Lấy tên lớp
		String className = "Chưa phân lớp";
		if (student.getClassId() != null) {
			ClassEntity classEntity = classRepository.findById(student.getClassId()).orElse(null);
			className = (classEntity != null) ? classEntity.getName() : "Lớp " + student.getClassId();
		}

		return new StudentPortalInfo.StudentProfile(student.getId(), student.getStudentCode(), user.getFullName(),
				user.getEmail(), user.getPhone(), className, "Công nghệ thông tin", // Default major
				"2024-2028" // Default academic year
		);
	}

	/**
	 * Thay đổi mật khẩu cho sinh viên (không cần mật khẩu hiện tại)
	 */
	public StudentPortalInfo.ChangePasswordResponse changePassword(Long studentId,
			StudentPortalInfo.ChangePasswordRequest request) {
		logger.info("Changing password for student ID: {}", studentId);

		try {
			// Validate input - chỉ cần mật khẩu mới
			if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
				return new StudentPortalInfo.ChangePasswordResponse(false, "Mật khẩu mới không được để trống");
			}

			if (request.getNewPassword().length() < 6) {
				return new StudentPortalInfo.ChangePasswordResponse(false, "Mật khẩu mới phải có ít nhất 6 ký tự");
			}

			if (!request.getNewPassword().equals(request.getConfirmPassword())) {
				return new StudentPortalInfo.ChangePasswordResponse(false, "Xác nhận mật khẩu không khớp");
			}

			// Get student and user
			Student student = studentRepository.findById(studentId)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với ID: " + studentId));

			User user = userRepository.findById(student.getUserId()).orElseThrow(
					() -> new RuntimeException("Không tìm thấy thông tin user cho sinh viên ID: " + studentId));

			// Mã hóa mật khẩu mới bằng BCrypt
			String encodedPassword = passwordEncoder.encode(request.getNewPassword());

			// Update password với mật khẩu đã mã hóa
			user.setPassword(encodedPassword);
			userRepository.save(user);

			logger.info("Password changed successfully for student ID: {}", studentId);
			return new StudentPortalInfo.ChangePasswordResponse(true, "Đổi mật khẩu thành công");

		} catch (Exception e) {
			logger.error("Error changing password for student ID: {}", studentId, e);
			return new StudentPortalInfo.ChangePasswordResponse(false, "Lỗi hệ thống: " + e.getMessage());
		}
	}

	/**
	 * Lấy thông tin thanh toán học phí của sinh viên theo semester
	 */
	public StudentPortalInfo.PaymentInfo getPaymentInfo(Long studentId, String semester) {
		logger.info("Getting payment info for student ID: {} in semester: {}", studentId, semester);

		try {
			// Lấy thông tin semester
			Semester semesterEntity = getSemesterBySemesterString(semester);

			if (semesterEntity == null) {
				throw new RuntimeException("Không tìm thấy semester: " + semester);
			}

			// Lấy danh sách enrollments của sinh viên trong semester này
			List<Enrollment> enrollments = enrollmentRepository.findByStudentIdAndSemester(studentId, semester);

			// Tính tổng số tiền phải đóng từ các môn học đã đăng ký
			BigDecimal totalAmount = BigDecimal.ZERO;
			List<StudentPortalInfo.CoursePaymentDetail> courseDetails = new ArrayList<>();

			for (Enrollment enrollment : enrollments) {
				Course course = courseRepository.findById(enrollment.getCourseId()).orElse(null);
				if (course != null) {
					totalAmount = totalAmount.add(course.getFee());

					// Tạo course payment detail
					StudentPortalInfo.CoursePaymentDetail detail = new StudentPortalInfo.CoursePaymentDetail(
							course.getId(), course.getCourseCode(), course.getName(), course.getCredit(),
							course.getFee(), "ENROLLED" // Status của enrollment
					);
					courseDetails.add(detail);
				}
			}

			// Kiểm tra trạng thái thanh toán
			Optional<Payment> paymentOpt = paymentRepository.findByStudentIdAndSemesterId(studentId,
					semesterEntity.getId());

			String paymentStatus;
			BigDecimal paidAmount;
			LocalDateTime paymentDate = null;

			if (paymentOpt.isPresent()) {
				Payment payment = paymentOpt.get();
				paymentStatus = payment.getStatus().toString();
				paidAmount = payment.getStatus() == Status.PAID ? totalAmount : BigDecimal.ZERO;
				paymentDate = payment.getPaymentDate();
			} else {
				paymentStatus = "PENDING";
				paidAmount = BigDecimal.ZERO;
			}

			// Tạo display name cho semester
			String displayName = generateDisplayName(semester);

			return new StudentPortalInfo.PaymentInfo(semesterEntity.getId(), semester, displayName, totalAmount,
					paidAmount, paymentStatus, paymentDate, courseDetails);

		} catch (Exception e) {
			logger.error("Error getting payment info for student ID: {} in semester: {}", studentId, semester, e);
			throw new RuntimeException("Lỗi khi lấy thông tin thanh toán: " + e.getMessage());
		}
	}

	/**
	 * Lấy danh sách thông tin thanh toán của tất cả các semester mà sinh viên đã
	 * đăng ký
	 */
	public List<StudentPortalInfo.PaymentInfo> getAllPaymentInfo(Long studentId) {
		logger.info("Getting all payment info for student ID: {}", studentId);

		try {
			// Lấy danh sách tất cả semester mà sinh viên có enrollment
			List<String> semesters = enrollmentRepository.findDistinctSemestersByStudentId(studentId);

			List<StudentPortalInfo.PaymentInfo> paymentInfos = new ArrayList<>();

			for (String semester : semesters) {
				try {
					StudentPortalInfo.PaymentInfo paymentInfo = getPaymentInfo(studentId, semester);
					paymentInfos.add(paymentInfo);
				} catch (Exception e) {
					logger.warn("Error getting payment info for semester: {}", semester, e);
					// Continue with other semesters
				}
			}

			// Sort by semester descending (newest first)
			paymentInfos.sort((p1, p2) -> p2.getSemester().compareTo(p1.getSemester()));

			return paymentInfos;

		} catch (Exception e) {
			logger.error("Error getting all payment info for student ID: {}", studentId, e);
			throw new RuntimeException("Lỗi khi lấy thông tin thanh toán: " + e.getMessage());
		}
	}

	/**
	 * Tạo payment record cho sinh viên trong semester
	 */
	public Payment createPayment(Long studentId, String semester) {
		logger.info("Creating payment for student ID: {} in semester: {}", studentId, semester);

		try {
			Semester semesterEntity = getSemesterBySemesterString(semester);
			if (semesterEntity == null) {
				throw new RuntimeException("Không tìm thấy semester: " + semester);
			}

			// Kiểm tra xem đã có payment chưa
			Optional<Payment> existingPayment = paymentRepository.findByStudentIdAndSemesterId(studentId,
					semesterEntity.getId());
			if (existingPayment.isPresent()) {
				return existingPayment.get();
			}

			// Tạo payment mới
			Payment payment = new Payment(studentId, semesterEntity.getId());
			return paymentRepository.save(payment);

		} catch (Exception e) {
			logger.error("Error creating payment for student ID: {} in semester: {}", studentId, semester, e);
			throw new RuntimeException("Lỗi khi tạo payment: " + e.getMessage());
		}
	}

	/**
	 * Helper method để lấy Semester entity từ semester string
	 */
	private Semester getSemesterBySemesterString(String semester) {
		return semesterRepository.findAll().stream().filter(s -> s.getSemester().equals(semester)).findFirst()
				.orElse(null);
	}

	/**
	 * Xuất bảng điểm ra file CSV
	 */
	public byte[] exportGradesToCsv(Long studentId, String semester) {
		try {
			StudentPortalInfo.StudentGradesInfo grades = getStudentGrades(studentId, semester);

			StringBuilder csv = new StringBuilder();
			// Add BOM for UTF-8
			csv.append('\ufeff');

			// Headers
			csv.append(
					"Mã môn,Tên môn học,Tín chỉ,Điểm TP1,Điểm TP2,Điểm CK,Điểm TK,Hệ số 4,Điểm chữ,Xếp loại,Trạng thái,Học kỳ\n");

			// Data rows
			for (StudentPortalInfo.GradeItem item : grades.getGradeItems()) {
				csv.append(escapeCSV(item.getCourseCode())).append(",");
				csv.append(escapeCSV(item.getCourseName())).append(",");
				csv.append(item.getCredit()).append(",");
				csv.append(item.getComponentScore1() != null ? item.getComponentScore1() : "").append(",");
				csv.append(item.getComponentScore2() != null ? item.getComponentScore2() : "").append(",");
				csv.append(item.getFinalExamScore() != null ? item.getFinalExamScore() : "").append(",");
				csv.append(item.getTotalScore() != null ? item.getTotalScore() : "").append(",");
				csv.append(item.getScoreCoefficient4() != null ? item.getScoreCoefficient4() : "").append(",");
				csv.append(escapeCSV(item.getGrade())).append(",");
				csv.append(escapeCSV(gradeCalculationService.getClassification(item.getTotalScore()))).append(",");
				csv.append(escapeCSV(item.getStatus())).append(",");
				csv.append(escapeCSV(item.getSemester())).append("\n");
			}

			return csv.toString().getBytes(StandardCharsets.UTF_8);
		} catch (Exception e) {
			logger.error("Error exporting grades to CSV", e);
			throw new RuntimeException("Error exporting grades", e);
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
