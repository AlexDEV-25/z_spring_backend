package com.example.app.service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.app.dto.PaymentDTO;
import com.example.app.dto.PrincipalPortalInfo;
import com.example.app.dto.PrincipalPortalInfo.PaymentDetailResponse;
import com.example.app.dto.PrincipalPortalInfo.PaymentStatistics;
import com.example.app.dto.PrincipalPortalInfo.PaymentWithDetails;
import com.example.app.enumvalue.Status;
import com.example.app.model.Course;
import com.example.app.model.Enrollment;
import com.example.app.model.Payment;
import com.example.app.model.Semester;
import com.example.app.model.Student;
import com.example.app.model.User;
import com.example.app.repository.CourseRepository;
import com.example.app.repository.EnrollmentRepository;
import com.example.app.repository.PaymentRepository;
import com.example.app.repository.SemesterRepository;
import com.example.app.repository.StudentRepository;
import com.example.app.repository.UserRepository;

@Service
public class PaymentService {

	private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

	private final PaymentRepository paymentRepository;
	private final EnrollmentRepository enrollmentRepository;
	private final CourseRepository courseRepository;
	private final SemesterRepository semesterRepository;
	private final StudentRepository studentRepository;
	private final UserRepository userRepository;

	public PaymentService(PaymentRepository paymentRepository, EnrollmentRepository enrollmentRepository,
			CourseRepository courseRepository, SemesterRepository semesterRepository,
			StudentRepository studentRepository, UserRepository userRepository) {
		this.paymentRepository = paymentRepository;
		this.enrollmentRepository = enrollmentRepository;
		this.courseRepository = courseRepository;
		this.semesterRepository = semesterRepository;
		this.studentRepository = studentRepository;
		this.userRepository = userRepository;
	}

	// Lấy tất cả payments của tất cả sinh viên
	public List<PaymentWithDetails> getAllPayments(String status, String semester) {
		logger.info("Getting all payments with status: {} and semester: {}", status, semester);

		try {
			List<Payment> payments = paymentRepository.findAll();

			// Sort by payment date descending
			payments.sort((p1, p2) -> p2.getPaymentDate().compareTo(p1.getPaymentDate()));

			// Convert to PaymentWithDetails with additional info
			return payments.stream().map(this::convertToPaymentWithDetails).collect(Collectors.toList());

		} catch (Exception e) {
			logger.error("Error getting payments with filters", e);
			throw new RuntimeException("Lỗi khi lấy danh sách payments: " + e.getMessage());
		}
	}

	//
	private PaymentWithDetails convertToPaymentWithDetails(Payment payment) {
		PaymentWithDetails result = new PaymentWithDetails();
		result.setId(payment.getId());
		result.setStudentId(payment.getStudentId());
		result.setSemesterId(payment.getSemesterId());
		result.setPaymentDate(payment.getPaymentDate());
		result.setStatus(payment.getStatus());

		// Get student code
		try {
			Student student = studentRepository.findById(payment.getStudentId()).orElse(null);
			if (student != null) {
				result.setStudentCode(student.getStudentCode());
			}
		} catch (Exception e) {
			logger.warn("Could not find student for payment {}", payment.getId());
		}

		// Get semester name
		try {
			Semester semester = semesterRepository.findById(payment.getSemesterId()).orElse(null);
			if (semester != null) {
				result.setSemesterName(semester.getSemester());
			}
		} catch (Exception e) {
			logger.warn("Could not find semester for payment {}", payment.getId());
		}

		return result;
	}

	// Convert Entity -> DTO
	private PaymentDTO convertToDTO(Payment entity) {
		return new PaymentDTO(entity.getId(), entity.getStudentId(), entity.getSemesterId(), entity.getPaymentDate(),
				entity.getStatus());
	}

	// Convert DTO -> Entity
	private Payment convertToEntity(PaymentDTO dto) {
		return new Payment(dto.getId(), dto.getStudentId(), dto.getSemesterId(), dto.getPaymentDate(), dto.getStatus());
	}

	// Lấy payment theo ID
	public PaymentDTO getPaymentById(Long id) {
		logger.info("Getting payment by ID: {}", id);

		return paymentRepository.findById(id).map(this::convertToDTO)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy payment với ID: " + id));
	}

	// Lấy chi tiết payment
	public PaymentDetailResponse getPaymentDetail(Long paymentId) {
		logger.info("Getting payment detail for ID: {}", paymentId);

		try {
			PaymentDTO dto = getPaymentById(paymentId);

			// Lấy thông tin sinh viên
			Student student = studentRepository.findById(dto.getStudentId()).orElse(null);

			// Lấy thông tin user để lấy fullname
			User user = null;
			if (student != null) {
				user = userRepository.findById(student.getUserId()).orElse(null);
			}

			// Lấy thông tin semester
			Semester semester = semesterRepository.findById(dto.getSemesterId()).orElse(null);

			// Lấy danh sách enrollments và courses
			String semesterString = semester != null ? semester.getSemester() : "";
			List<Enrollment> enrollments = enrollmentRepository.findByStudentIdAndSemester(dto.getStudentId(),
					semesterString);

			List<PrincipalPortalInfo.CoursePaymentDetail> courseDetails = new ArrayList<>();
			double totalAmount = 0;

			for (Enrollment enrollment : enrollments) {
				Course course = courseRepository.findById(enrollment.getCourseId()).orElse(null);
				if (course != null) {
					double fee = course.getFee() != null ? course.getFee().doubleValue() : 0;
					courseDetails.add(new PrincipalPortalInfo.CoursePaymentDetail(course.getId(),
							course.getCourseCode(), course.getName(), course.getCredit(), fee));
					totalAmount += fee;
				}
			}

			return new PaymentDetailResponse(dto.getId(), dto.getStudentId(),
					user != null ? user.getFullName()
							: (student != null ? "Sinh viên " + student.getStudentCode() : "N/A"),
					student != null ? "CNTT" + (student.getId() % 10 + 1) : "N/A", dto.getSemesterId(),
					semester != null ? semester.getSemester() : "N/A", dto.getPaymentDate().toString(),
					dto.getStatus().toString(), courseDetails, totalAmount);

		} catch (Exception e) {
			logger.error("Error getting payment detail for ID: {}", paymentId, e);
			throw new RuntimeException("Lỗi khi lấy chi tiết payment: " + e.getMessage());
		}
	}

	// Cập nhật trạng thái thanh toán vào DB
	public PaymentDTO updatePaymentStatus(Long paymentId, String newStatus, String reason) {
		logger.info("Updating payment status for ID: {} to status: {} with reason: {}", paymentId, newStatus, reason);

		try {
			PaymentDTO dto = getPaymentById(paymentId);

			// Validate new status
			Status status;
			try {
				status = Status.valueOf(newStatus.toUpperCase());
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("Trạng thái không hợp lệ: " + newStatus);
			}

			// Update payment
			Status oldStatus = dto.getStatus();
			dto.setStatus(status);

			// Update payment date if status changes to PAID
			if (status == Status.PAID && oldStatus != Status.PAID) {
				dto.setPaymentDate(LocalDateTime.now());
			}

			Payment updatedPayment = paymentRepository.save(convertToEntity(dto));

			logger.info("Payment status updated successfully. ID: {}, Old status: {}, New status: {}", paymentId,
					oldStatus, status);

			return convertToDTO(updatedPayment);

		} catch (Exception e) {
			logger.error("Error updating payment status for ID: {}", paymentId, e);
			throw new RuntimeException("Lỗi khi cập nhật trạng thái thanh toán: " + e.getMessage());
		}
	}

	// Lấy tất cả payment của 1 sinh viên
	public List<Payment> getPaymentsByStudentId(Long studentId) {
		logger.info("Getting payments for student ID: {}", studentId);

		try {
			return paymentRepository.findByStudentIdOrderByPaymentDateDesc(studentId);
		} catch (Exception e) {
			logger.error("Error getting payments for student ID: {}", studentId, e);
			throw new RuntimeException("Lỗi khi lấy payments của sinh viên: " + e.getMessage());
		}
	}

	// Lấy thống kê payments
	public PrincipalPortalInfo.PaymentStatistics getPaymentStatistics(String semester) {
		logger.info("Getting payment statistics for semester: {}", semester);

		try {
			List<Payment> payments;

			if (semester != null) {
				Semester semesterEntity = getSemesterByString(semester);
				if (semesterEntity != null) {
					payments = paymentRepository.findAll().stream()
							.filter(p -> p.getSemesterId().equals(semesterEntity.getId())).collect(Collectors.toList());
				} else {
					payments = List.of();
				}
			} else {
				payments = paymentRepository.findAll();
			}

			long totalPayments = payments.size();
			long paidPayments = payments.stream().mapToLong(p -> p.getStatus() == Status.PAID ? 1 : 0).sum();
			long pendingPayments = payments.stream().mapToLong(p -> p.getStatus() == Status.PENDING ? 1 : 0).sum();
			long failedPayments = payments.stream().mapToLong(p -> p.getStatus() == Status.FAILED ? 1 : 0).sum();

			// Calculate amounts (need to get from enrollments and courses)
			double totalAmount = 0;
			double paidAmount = 0;
			double pendingAmount = 0;

			for (Payment payment : payments) {
				double paymentAmount = calculatePaymentAmount(payment);
				totalAmount += paymentAmount;

				if (payment.getStatus() == Status.PAID) {
					paidAmount += paymentAmount;
				} else if (payment.getStatus() == Status.PENDING) {
					pendingAmount += paymentAmount;
				}
			}

			return new PaymentStatistics(totalPayments, paidPayments, pendingPayments, failedPayments, totalAmount,
					paidAmount, pendingAmount);

		} catch (Exception e) {
			logger.error("Error getting payment statistics", e);
			throw new RuntimeException("Lỗi khi lấy thống kê payments: " + e.getMessage());
		}
	}

	// Tính toán số tiền của một payment dựa trên enrollments
	private double calculatePaymentAmount(Payment payment) {
		try {
			// Get semester string from payment's semesterId
			Semester semester = semesterRepository.findById(payment.getSemesterId()).orElse(null);
			if (semester == null) {
				return 0;
			}

			// Get enrollments for this student in this semester
			List<Enrollment> enrollments = enrollmentRepository.findByStudentIdAndSemester(payment.getStudentId(),
					semester.getSemester());

			BigDecimal totalAmount = BigDecimal.ZERO;
			for (Enrollment enrollment : enrollments) {
				Course course = courseRepository.findById(enrollment.getCourseId()).orElse(null);
				if (course != null) {
					totalAmount = totalAmount.add(course.getFee());
				}
			}

			return totalAmount.doubleValue();

		} catch (Exception e) {
			logger.warn("Error calculating payment amount for payment ID: {}", payment.getId(), e);
			return 0;
		}
	}

	// Lấy Semester entity từ semester string
	private Semester getSemesterByString(String semester) {
		return semesterRepository.findAll().stream().filter(s -> s.getSemester().equals(semester)).findFirst()
				.orElse(null);
	}

	// Xuất danh sách payments ra file CSV
	public byte[] exportPaymentsToCsv(String semester) {
		try {
			List<PaymentWithDetails> payments = getAllPayments("", "");

			StringBuilder csv = new StringBuilder();
			// Add BOM for UTF-8
			csv.append('\ufeff');

			// Headers
			csv.append("ID,Mã SV,Học kỳ,Trạng thái,Ngày thanh toán\n");

			// Data rows
			for (PaymentWithDetails payment : payments) {
				csv.append(payment.getId()).append(",");
				csv.append(escapeCSV(payment.getStudentCode())).append(",");
				csv.append(escapeCSV(payment.getSemesterName())).append(",");
				csv.append(escapeCSV(payment.getStatus().toString())).append(",");
				csv.append(payment.getPaymentDate() != null ? payment.getPaymentDate().toString() : "").append("\n");
			}

			return csv.toString().getBytes(StandardCharsets.UTF_8);
		} catch (Exception e) {
			logger.error("Error exporting payments to CSV", e);
			throw new RuntimeException("Error exporting payments", e);
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
