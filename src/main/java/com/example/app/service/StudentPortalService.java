package com.example.app.service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.app.dto.StudentPortalInfo;
import com.example.app.enumvalue.Status;
import com.example.app.model.ClassEntity;
import com.example.app.model.Course;
import com.example.app.model.Department;
import com.example.app.model.Enrollment;
import com.example.app.model.Lecturer;
import com.example.app.model.Payment;
import com.example.app.model.Semester;
import com.example.app.model.Student;
import com.example.app.model.Teaching;
import com.example.app.model.User;
import com.example.app.repository.ClassRepository;
import com.example.app.repository.CourseRepository;
import com.example.app.repository.DepartmentRepository;
import com.example.app.repository.EnrollmentRepository;
import com.example.app.repository.LecturerRepository;
import com.example.app.repository.PaymentRepository;
import com.example.app.repository.SemesterRepository;
import com.example.app.repository.StudentRepository;
import com.example.app.repository.TeachingRepository;
import com.example.app.repository.UserRepository;
import com.example.app.share.Share;
import com.example.app.share.Share.ChangePassword;
import com.example.app.share.Share.SemesterUtils;
import com.example.app.share.Share.getAllSemester;

@Service
public class StudentPortalService {

	private static final Logger logger = LoggerFactory.getLogger(StudentPortalService.class);
	private static final String DEFAULT_SEMESTER = "2024-1";

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
	private final DepartmentRepository departmentRepository;

	public StudentPortalService(StudentRepository studentRepository, UserRepository userRepository,
			EnrollmentRepository enrollmentRepository, CourseRepository courseRepository,
			TeachingRepository teachingRepository, LecturerRepository lecturerRepository,
			ClassRepository classRepository, SemesterRepository semesterRepository, PaymentRepository paymentRepository,
			GradeCalculationService gradeCalculationService, DepartmentRepository departmentRepository) {
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
		this.departmentRepository = departmentRepository;
	}

	public List<Share.SemesterInfo> getAllSemesters() {
		logger.info("Getting all semesters from database");
		getAllSemester semesters = new getAllSemester(semesterRepository);
		return semesters.getAllSemesters();
	}

	private String normalizeSemester(String semester) {
		return (semester != null && !semester.trim().isEmpty()) ? semester : DEFAULT_SEMESTER;
	}

	private Semester requireSemester(String semester) {
		return SemesterUtils.requireByCode(semesterRepository, normalizeSemester(semester));
	}

	private Long resolveTargetSemesterId(String semester) {
		String effectiveSemester = normalizeSemester(semester);
		return SemesterUtils.resolveSemesterId(semesterRepository, effectiveSemester, 1L);
	}

	// Hàm lấy danh sách enrollment theo sinh viên + kỳ học
	private List<Enrollment> getEnrolledCoursesBySemester(Long studentId, Long semesterId) {
		return enrollmentRepository.findByStudentId(studentId).stream().filter(e -> "ENROLLED".equals(e.getStatus()))
				.filter(e -> e.getCourseId() != null).filter(e -> {
					Course course = courseRepository.findById(e.getCourseId()).orElse(null);
					return course != null && Objects.equals(course.getSemesterId(), semesterId);
				}).collect(Collectors.toList());
	}

	// Hàm chuyển đổi Enrollment → ScheduleItem
	private StudentPortalInfo.ScheduleItem convertToScheduleItem(Enrollment enrollment, Long targetSemesterId) {
		Course course = courseRepository.findById(enrollment.getCourseId()).orElse(null);
		if (course == null || !targetSemesterId.equals(course.getSemesterId()))
			return null;

		Teaching teaching = teachingRepository.findAll().stream().filter(t -> course.getId().equals(t.getCourseId()))
				.findFirst().orElse(null);

		String lecturerName = "Chưa phân công";
		if (teaching != null) {
			Lecturer lecturer = lecturerRepository.findById(teaching.getLecturerId()).orElse(null);
			if (lecturer != null) {
				User lecturerUser = userRepository.findById(lecturer.getUserId()).orElse(null);
				if (lecturerUser != null)
					lecturerName = lecturerUser.getFullName();
			}
		}

		return new StudentPortalInfo.ScheduleItem(course.getId(), course.getCourseCode(), course.getName(),
				course.getCredit(), teaching != null ? teaching.getPeriod() : "1-2",
				teaching != null ? teaching.getDayOfWeek() : "Thứ 2", lecturerName,
				teaching != null && teaching.getClassRoom() != null ? teaching.getClassRoom() : "Chưa xác định");
	}

	// Hàm lấy thông tin thời khóa biểu
	public StudentPortalInfo.StudentScheduleInfo getStudentSchedule(Long studentId, String semester) {
		logger.info("Getting schedule for student ID: {} in semester: {}", studentId, semester);

		Student student = getStudentById(studentId);
		User user = getUserById(student.getUserId());

		Long targetSemesterId = resolveTargetSemesterId(semester);
		logger.info("Filtering schedule by semesterId: {}", targetSemesterId);

		List<Enrollment> enrollments = getEnrolledCoursesBySemester(studentId, targetSemesterId);
		logger.info("Found {} ENROLLED enrollments for student ID: {} in semester: {}", enrollments.size(), studentId,
				semester);

		List<StudentPortalInfo.ScheduleItem> scheduleItems = enrollments.stream()
				.map(e -> convertToScheduleItem(e, targetSemesterId)).filter(Objects::nonNull)
				.collect(Collectors.toList());

		int totalCredits = scheduleItems.stream().mapToInt(StudentPortalInfo.ScheduleItem::getCredit).sum();

		return new StudentPortalInfo.StudentScheduleInfo(studentId, student.getStudentCode(), user.getFullName(),
				semester, totalCredits, scheduleItems);
	}

	// Lấy bảng điểm của sinh viên. Nếu không truyền semester sẽ trả về tất cả học phần đã đăng ký
	public StudentPortalInfo.StudentGradesInfo getStudentGrades(Long studentId, String semester) {
		logger.info("Getting grades for student ID: {} in semester: {}", studentId, semester);

		Student student = getStudentById(studentId);
		User user = getUserById(student.getUserId());

		final boolean filterBySemester = semester != null && !semester.trim().isEmpty();
		final String effectiveSemester = filterBySemester ? normalizeSemester(semester) : null;
		final Optional<Semester> semesterOpt = filterBySemester
				? SemesterUtils.findByCode(semesterRepository, effectiveSemester)
				: Optional.empty();
		final Long targetSemesterId = filterBySemester
				? semesterOpt.map(Semester::getId)
						.orElseGet(() -> SemesterUtils.resolveSemesterId(semesterRepository, effectiveSemester, 1L))
				: null;
		final String targetSemesterCode = filterBySemester
				? semesterOpt.map(Semester::getSemester)
						.orElseGet(() -> semesterRepository.findById(targetSemesterId).map(Semester::getSemester)
								.orElse(effectiveSemester))
				: null;

		if (filterBySemester) {
			logger.info("Filtering grades by semesterId: {} ({})", targetSemesterId, targetSemesterCode);
		}

		List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
		Map<Long, String> semesterCodeCache = new HashMap<>();

		List<StudentPortalInfo.GradeItem> gradeItems = enrollments.stream().map(enrollment -> {
			Course course = courseRepository.findById(enrollment.getCourseId()).orElse(null);
			if (course == null) {
				return null;
			}

			Long courseSemesterId = course.getSemesterId();
			if (filterBySemester) {
				if (courseSemesterId == null || !courseSemesterId.equals(targetSemesterId)) {
					return null;
				}
			}

			String semesterCode;
			if (filterBySemester) {
				semesterCode = targetSemesterCode != null ? targetSemesterCode : effectiveSemester;
			} else {
				if (courseSemesterId != null) {
					semesterCode = semesterCodeCache.computeIfAbsent(courseSemesterId, id -> semesterRepository.findById(id)
							.map(Semester::getSemester).orElse(DEFAULT_SEMESTER));
				} else {
					semesterCode = DEFAULT_SEMESTER;
				}
			}

			String status;
			if (enrollment.getGrade() != null) {
				status = "Đã hoàn thành";
			} else if ("PENDING_PAYMENT".equalsIgnoreCase(enrollment.getStatus())) {
				status = "Chờ thanh toán";
			} else {
				status = "Đang học";
			}

			return new StudentPortalInfo.GradeItem(course.getId(), course.getCourseCode(), course.getName(),
					course.getCredit(), enrollment.getComponentScore1(), enrollment.getComponentScore2(),
					enrollment.getFinalExamScore(), enrollment.getTotalScore(), enrollment.getScoreCoefficient4(),
					enrollment.getGrade(), semesterCode, status);
		}).filter(Objects::nonNull).collect(Collectors.toList());

		int totalCredits = gradeItems.stream().mapToInt(StudentPortalInfo.GradeItem::getCredit).sum();
		int completedCredits = gradeItems.stream().filter(item -> item.getGrade() != null)
				.mapToInt(StudentPortalInfo.GradeItem::getCredit).sum();

		double gpa = 0; // TODO: implement GPA calculation

		int totalCourses = gradeItems.size();
		int completedCourses = (int) gradeItems.stream().filter(item -> "Đã hoàn thành".equals(item.getStatus())).count();
		int inProgressCourses = (int) gradeItems.stream()
				.filter(item -> "Đang học".equals(item.getStatus()) || "Chờ thanh toán".equals(item.getStatus()))
				.count();

		return new StudentPortalInfo.StudentGradesInfo(studentId, student.getStudentCode(), user.getFullName(), gpa,
				totalCredits, completedCredits, gradeItems, totalCourses, completedCourses, inProgressCourses);
	}

	// Đăng ký môn học
	public StudentPortalInfo.CourseRegistrationResponse registerCourse(Long studentId, Long courseId, String semester) {
		logger.info("Registering course {} for student ID: {}", courseId, studentId);

		try {
			Semester semesterEntity = requireSemester(normalizeSemester(semester));

			Optional<Payment> existingPayment = paymentRepository.findByStudentIdAndSemesterId(studentId,
					semesterEntity.getId());
			if (existingPayment.isPresent()) {
				return new StudentPortalInfo.CourseRegistrationResponse(false,
						"Bạn đã tạo hóa đơn cho học kỳ này không thể đăng ký thêm môn học.");
			}
			// Kiểm tra môn học tồn tại
			Course course = courseRepository.findById(courseId)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy môn học"));

			// Kiểm tra đã đăng ký chưa
			boolean alreadyEnrolled = enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
			if (alreadyEnrolled) {
				return new StudentPortalInfo.CourseRegistrationResponse(false, "Đã đăng ký môn học này rồi");
			}

			// Tạo enrollment mới với trạng thái PENDING_PAYMENT
			Enrollment enrollment = new Enrollment(null, studentId, courseId, null, null, null, null, null, null,
					"PENDING_PAYMENT");
			enrollmentRepository.save(enrollment);

			return new StudentPortalInfo.CourseRegistrationResponse(true,
					"Đăng ký môn học " + course.getName() + " thành công");

		} catch (Exception e) {
			logger.error("Error registering course: ", e);
			return new StudentPortalInfo.CourseRegistrationResponse(false, "Lỗi đăng ký: " + e.getMessage());
		}
	}

	// Hủy đăng ký môn học
	public StudentPortalInfo.CourseRegistrationResponse unregisterCourse(Long studentId, Long courseId) {
		logger.info("Unregistering course {} for student ID: {}", courseId, studentId);

		try {
			// Kiểm tra enrollment tồn tại
			Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy đăng ký môn học"));

			// Kiểm tra trạng thái enrollment
			if ("ENROLLED".equals(enrollment.getStatus())) {
				return new StudentPortalInfo.CourseRegistrationResponse(false, "Không thể hủy môn học đã thanh toán");
			}

			// Xóa enrollment (chỉ cho phép với trạng thái PENDING_PAYMENT)
			enrollmentRepository.delete(enrollment);

			Course course = courseRepository.findById(courseId).orElse(null);
			String courseName = course != null ? course.getName() : "môn học";

			return new StudentPortalInfo.CourseRegistrationResponse(true, "Hủy đăng ký " + courseName + " thành công");

		} catch (Exception e) {
			logger.error("Error unregistering course: ", e);
			return new StudentPortalInfo.CourseRegistrationResponse(false, "Lỗi hủy đăng ký: " + e.getMessage());
		}
	}

	// lấy thông tin student và user
	private Student getStudentById(Long studentId) {
		return studentRepository.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với ID: " + studentId));
	}

	private User getUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin user"));
	}

	// Lấy thông tin thanh toán học phí của sinh viên theo semester
	public StudentPortalInfo.PaymentInfo getPaymentInfo(Long studentId, String semester) {
		logger.info("Getting payment info for student ID: {} in semester: {}", studentId, semester);

		String effectiveSemester = normalizeSemester(semester);

		try {
			Semester semesterEntity = requireSemester(effectiveSemester);

			// Lấy danh sách enrollments (bao gồm cả PENDING_PAYMENT và ENROLLED) của sinh
			// viên trong semester này
			List<Enrollment> enrolledCourses = enrollmentRepository.findByStudentIdAndSemester(studentId, semester)
					.stream().filter(e -> "PENDING_PAYMENT".equals(e.getStatus()) || "ENROLLED".equals(e.getStatus()))
					.collect(Collectors.toList());

			// Tính tổng số tiền phải đóng từ các môn học đã đăng ký
			BigDecimal totalAmount = enrolledCourses.stream()
					.map(e -> courseRepository.findById(e.getCourseId()).map(Course::getFee).orElse(BigDecimal.ZERO))
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			List<StudentPortalInfo.CoursePaymentDetail> courseDetails = new ArrayList<>();

			for (Enrollment enrollment : enrolledCourses) {
				Course course = courseRepository.findById(enrollment.getCourseId()).orElse(null);
				if (course != null) {
					// Tạo course payment detail
					StudentPortalInfo.CoursePaymentDetail detail = new StudentPortalInfo.CoursePaymentDetail(
							course.getId(), course.getCourseCode(), course.getName(), course.getCredit(),
							course.getFee(), enrollment.getStatus() // Sử dụng trạng thái thực tế của enrollment
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
			String displayName = Share.getAllSemester.generateDisplayName(semester);

			return new StudentPortalInfo.PaymentInfo(semesterEntity.getId(), semester, displayName, totalAmount,
					paidAmount, paymentStatus, paymentDate, courseDetails);

		} catch (Exception e) {
			logger.error("Error getting payment info for student ID: {} in semester: {}", studentId, semester, e);
			throw new RuntimeException("Lỗi khi lấy thông tin thanh toán: " + e.getMessage());
		}
	}

	// Lấy danh sách thông tin thanh toán của tất cả các semester mà sinh viên đã
	// đăng ký
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

	// Tạo payment record cho sinh viên trong semester
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

	// Cập nhật trạng thái enrollment từ PENDING_PAYMENT thành ENROLLED khi tạo yêu
	// cầu thanh toán và trừ slot trong course
	public void updateEnrollmentStatusToEnrolled(Long studentId, String semester) {
		try {
			logger.info("Updating enrollment status to ENROLLED for student ID: {} in semester: {}", studentId,
					semester);

			// Lấy kỳ học từ database
			Semester semesterObj = getSemesterBySemesterString(semester);
			if (semesterObj == null) {
				logger.warn("Semester not found: {}", semester);
				return;
			}

			// Lấy tất cả enrollment của sinh viên trong kỳ học này có trạng thái
			// PENDING_PAYMENT
			List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId).stream()
					.filter(e -> e.getCourseId() != null).filter(e -> "PENDING_PAYMENT".equals(e.getStatus()))
					.filter(e -> {
						Course course = courseRepository.findById(e.getCourseId()).orElse(null);
						return course != null && semester
								.equals(course.getSemesterId() != null
										? semesterRepository.findById(course.getSemesterId()).map(Semester::getSemester)
												.orElse("")
										: "");
					}).collect(Collectors.toList());

			logger.info("Found {} PENDING_PAYMENT enrollments for student ID: {} in semester: {}", enrollments.size(),
					studentId, semester);

			if (enrollments.isEmpty()) {
				logger.warn("No PENDING_PAYMENT enrollments found for student ID: {} in semester: {}", studentId,
						semester);
				return;
			}

			// Cập nhật trạng thái thành ENROLLED và trừ slot
			for (Enrollment enrollment : enrollments) {
				logger.info("Updating enrollment ID: {} from PENDING_PAYMENT to ENROLLED", enrollment.getId());
				enrollment.setStatus("ENROLLED");
				enrollmentRepository.save(enrollment);

				// Trừ slot trong course
				Course course = courseRepository.findById(enrollment.getCourseId()).orElse(null);
				if (course != null && course.getSlot() != null && course.getSlot() > 0) {
					int oldSlot = course.getSlot();
					course.setSlot(course.getSlot() - 1);
					courseRepository.save(course);
					logger.info("Reduced slot for course ID: {} from {} to {}", course.getId(), oldSlot,
							course.getSlot());
				}
			}

			logger.info(
					"Successfully updated {} enrollments to ENROLLED status and reduced course slots for student ID: {} in semester: {}",
					enrollments.size(), studentId, semester);

		} catch (Exception e) {
			logger.error("Error updating enrollment status to ENROLLED for student ID: {} in semester: {}", studentId,
					semester, e);
		}
	}

	// Helper method để lấy Semester entity từ semester string
	private Semester getSemesterBySemesterString(String semester) {
		return semesterRepository.findAll().stream().filter(s -> s.getSemester().equals(semester)).findFirst()
				.orElse(null);
	}

	// Tạo yêu cầu thanh toán học phí cho sinh viên
	public String createPaymentRequest(Long studentId, String semester) {
		try {
			logger.info("Creating payment request for student ID: {} in semester: {}", studentId, semester);

			// Kiểm tra xem kỳ học có tồn tại không
			Semester semesterObj = getSemesterBySemesterString(semester);
			if (semesterObj == null) {
				throw new RuntimeException("Không tìm thấy kỳ học: " + semester);
			}

			// Lấy danh sách enrollment của sinh viên trong kỳ học này
			List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId).stream()
					.filter(e -> e.getCourseId() != null).filter(e -> {
						Course course = courseRepository.findById(e.getCourseId()).orElse(null);
						return course != null && semester
								.equals(course.getSemesterId() != null
										? semesterRepository.findById(course.getSemesterId()).map(Semester::getSemester)
												.orElse("")
										: "");
					}).collect(Collectors.toList());

			if (enrollments.isEmpty()) {
				throw new RuntimeException("Sinh viên chưa đăng ký môn học nào trong kỳ này");
			}

			// Kiểm tra xem đã có payment cho kỳ này chưa
			Payment existingPayment = paymentRepository.findByStudentIdAndSemesterId(studentId, semesterObj.getId())
					.orElse(null);

			if (existingPayment != null) {
				if (existingPayment.getStatus() == Status.PAID) {
					// Payment đã tồn tại và đã thanh toán rồi
					return "Đã thanh toán học phí cho kỳ này";
				} else if (existingPayment.getStatus() == Status.PENDING) {
					// Payment đã tồn tại và đang chờ thanh toán - kiểm tra enrollment đã được cập
					// nhật chưa Cập nhật trạng thái enrollment và trừ slot nếu chưa được cập nhật
					updateEnrollmentStatusToEnrolled(studentId, semester);
					return "Yêu cầu thanh toán đã được tạo, đã cập nhật trạng thái đăng ký cho kỳ: " + semester;
				} else {
					// Payment đã tồn tại nhưng không ở trạng thái hợp lệ
					return "Có lỗi với trạng thái thanh toán hiện tại cho kỳ: " + semester;
				}
			} else {
				// Tính tổng học phí dựa trên trường fee của course
				double totalAmount = enrollments.stream().mapToDouble(e -> {
					try {
						Course course = courseRepository.findById(e.getCourseId()).orElse(null);
						if (course != null && course.getFee() != null) {
							return course.getFee().doubleValue(); // Sử dụng học phí đã định nghĩa trong course
						}
						return 0.0;
					} catch (Exception ex) {
						logger.warn("Error calculating fee for enrollment ID: {}, course ID: {}", e.getId(),
								e.getCourseId(), ex);
						return 0.0;
					}
				}).sum();

				if (totalAmount <= 0) {
					throw new RuntimeException("Không thể tính học phí cho kỳ này");
				}

				// Tạo payment mới với trạng thái PENDING (chờ thanh toán)
				Payment newPayment = new Payment();
				newPayment.setStudentId(studentId);
				newPayment.setSemesterId(semesterObj.getId());
				newPayment.setAmount(totalAmount);
				newPayment.setStatus(Status.PENDING); // Đặt trạng thái là PENDING
				newPayment.setPaymentDate(LocalDateTime.now());
				newPayment.setDescription("Học phí kỳ " + semester + " - " + enrollments.size() + " môn học");

				paymentRepository.save(newPayment);

				logger.info(
						"Created payment request for student ID: {} in semester: {} with amount: {} (status: PENDING)",
						studentId, semester, totalAmount);

				// Cập nhật trạng thái enrollment từ PENDING_PAYMENT thành ENROLLED ngay khi tạo
				// payment Và trừ slot trong course
				updateEnrollmentStatusToEnrolled(studentId, semester);

				return "Đã tạo yêu cầu thanh toán và đăng ký chính thức cho kỳ: " + semester + " - Tổng tiền: "
						+ String.format("%,.0f", totalAmount) + " VND (trạng thái: chờ thanh toán)";
			}

		} catch (Exception e) {
			logger.error("Error creating payment request for student ID: {} in semester: {}", studentId, semester, e);
			throw new RuntimeException("Lỗi khi tạo yêu cầu thanh toán: " + e.getMessage());
		}
	}

	// Lấy danh sách khóa học có thể đăng ký theo kỳ học (để tương thích với
	// frontend)
	public List<StudentPortalInfo.AvailableCourseInfo> getAvailableCourses(Long studentId, String semester) {
		try {
			logger.info("Getting available courses for student ID: {} in semester: {}", studentId, semester);

			// Lấy kỳ học từ database
			Semester semesterObj = getSemesterBySemesterString(semester);
			if (semesterObj == null) {
				logger.warn("Semester not found: {}", semester);
				return new ArrayList<>();
			}

			// Lấy tất cả khóa học của kỳ học
			List<Course> courses = courseRepository.findBySemesterId(semesterObj.getId());

			// Convert sang AvailableCourseInfo DTO
			return courses.stream().map(course -> {
				StudentPortalInfo.AvailableCourseInfo courseInfo = new StudentPortalInfo.AvailableCourseInfo();
				courseInfo.setCourseId(course.getId());
				courseInfo.setCourseCode(course.getCourseCode());
				courseInfo.setCourseName(course.getName());
				courseInfo.setCredit(course.getCredit());
				courseInfo.setCanRegister(true); // Mặc định có thể đăng ký
				courseInfo.setSemester(semester);
				courseInfo.setCanUnregister(false); // Mặc định không thể hủy đăng ký
				courseInfo.setAvailableSlots(course.getSlot()); // Sử dụng slot làm available slots
				courseInfo.setMaxSlots(course.getSlot()); // Tương tự

				// Lấy thông tin giảng viên nếu có
				List<Teaching> teachings = teachingRepository.findByCourseId(course.getId());
				if (!teachings.isEmpty()) {
					Teaching teaching = teachings.get(0);
					if (teaching.getLecturerId() != null) {
						Lecturer lecturer = lecturerRepository.findById(teaching.getLecturerId()).orElse(null);
						if (lecturer != null) {
							User lecturerUser = userRepository.findById(lecturer.getUserId()).orElse(null);
							if (lecturerUser != null) {
								courseInfo.setLecturerName(lecturerUser.getFullName());
							}
						}
					}
					courseInfo.setPeriod(teaching.getPeriod());
					courseInfo.setDayOfWeek(teaching.getDayOfWeek());
					courseInfo.setClassroom(teaching.getClassRoom());
				}

				return courseInfo;
			}).collect(Collectors.toList());

		} catch (Exception e) {
			logger.error("Error getting available courses for student ID: {} in semester: {}", studentId, semester, e);
			return new ArrayList<>();
		}
	}

	// Lấy thông tin cá nhân
	public StudentPortalInfo.StudentProfileInfo getStudentProfile(Long studentId) {
		logger.info("Getting profile for student ID: {}", studentId);

		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với ID: " + studentId));

		User user = userRepository.findById(student.getUserId()).orElseThrow(
				() -> new RuntimeException("Không tìm thấy thông tin user cho sinh viên ID: " + studentId));

		Department department = departmentRepository.findById(user.getDepartmentId())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin department"));

		// Lấy tên lớp và niên khóa
		String className = "Chưa phân lớp";
		String classYear = "Chưa phân niên khóa";
		if (student.getClassId() != null) {
			ClassEntity classEntity = classRepository.findById(student.getClassId()).orElse(null);
			className = (classEntity != null) ? classEntity.getName() : "Lớp chưa có ";
			classYear = (classEntity != null) ? classEntity.getYear() : "năm chưa có ";
		}
		System.out.println(department.getName() + classYear);
		return new StudentPortalInfo.StudentProfileInfo(student.getId(), student.getStudentCode(), user.getFullName(),
				user.getEmail(), user.getPhone(), className, department.getName(), classYear);
	}

	// Thay đổi mật khẩu
	public Share.ChangePasswordResponse changePassword(Long userId, Share.ChangePasswordRequest request) {
		logger.info("Changing password for user ID: {}", userId);
		Share.ChangePassword change = new ChangePassword(passwordEncoder, userRepository);
		return change.changePassword(userId, request);
	}

	// Xuất bảng điểm ra file CSV
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
				csv.append(Share.escapeCSV(item.getCourseCode())).append(",");
				csv.append(Share.escapeCSV(item.getCourseName())).append(",");
				csv.append(item.getCredit()).append(",");
				csv.append(item.getComponentScore1() != null ? item.getComponentScore1() : "").append(",");
				csv.append(item.getComponentScore2() != null ? item.getComponentScore2() : "").append(",");
				csv.append(item.getFinalExamScore() != null ? item.getFinalExamScore() : "").append(",");
				csv.append(item.getTotalScore() != null ? item.getTotalScore() : "").append(",");
				csv.append(item.getScoreCoefficient4() != null ? item.getScoreCoefficient4() : "").append(",");
				csv.append(Share.escapeCSV(item.getGrade())).append(",");
				csv.append(Share.escapeCSV(gradeCalculationService.getClassification(item.getTotalScore())))
						.append(",");
				csv.append(Share.escapeCSV(item.getStatus())).append(",");
				csv.append(Share.escapeCSV(item.getSemester())).append("\n");
			}

			return csv.toString().getBytes(StandardCharsets.UTF_8);
		} catch (Exception e) {
			logger.error("Error exporting grades to CSV", e);
			throw new RuntimeException("Error exporting grades", e);
		}
	}
}