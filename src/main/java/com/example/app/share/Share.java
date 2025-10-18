package com.example.app.share;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.app.dto.PaymentDetailDTO;
import com.example.app.exception.ResourceNotFoundException;
import com.example.app.model.Semester;
import com.example.app.model.User;
import com.example.app.repository.SemesterRepository;
import com.example.app.repository.UserRepository;

public class Share {
	private static final Logger logger = LoggerFactory.getLogger(Share.class);

	public static class SemesterInfo {
		private Long id;
		private String semester;
		private String displayName;

		// Constructors
		public SemesterInfo() {
		}

		public SemesterInfo(Long id, String semester, String displayName) {
			this.id = id;
			this.semester = semester;
			this.displayName = displayName;
		}

		// Getters and Setters
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getSemester() {
			return semester;
		}

		public void setSemester(String semester) {
			this.semester = semester;
		}

		public String getDisplayName() {
			return displayName != null ? displayName : semester;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
	}

	/**
	 * Base API Response DTO - sử dụng chung cho tất cả API responses
	 */
	public static class ApiResponse {
		private boolean success;
		private String message;

		public ApiResponse() {
		}

		public ApiResponse(boolean success, String message) {
			this.success = success;
			this.message = message;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}

	/**
	 * API Response với dữ liệu - sử dụng cho các API trả về data
	 */
	public static class DataResponse<T> extends ApiResponse {
		private T data;

		public DataResponse() {
			super();
		}

		public DataResponse(boolean success, String message, T data) {
			super(success, message);
			this.data = data;
		}

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}
	}

	/**
	 * Utility methods để tạo các loại response khác nhau
	 */
	public static class ResponseUtils {
		public static ApiResponse success(String message) {
			return new ApiResponse(true, message);
		}

		public static ApiResponse error(String message) {
			return new ApiResponse(false, message);
		}

		public static <T> DataResponse<T> success(String message, T data) {
			return new DataResponse<>(true, message, data);
		}

		public static <T> DataResponse<T> error(String message, T data) {
			return new DataResponse<>(false, message, data);
		}

		public static <T> DataResponse<T> dataOnly(T data) {
			return new DataResponse<>(true, "Success", data);
		}
	}

	/**
	 * DTO chung cho thông tin thanh toán - sử dụng cho cả Student và Principal Thay
	 * thế cho PaymentInfo (Student) và PaymentDetailResponse (Principal)
	 */
	public static class PaymentSummaryDTO {
		// Thông tin cơ bản về payment
		private Long id;
		private Long studentId;
		private String studentName;
		private String studentCode;
		private String studentClass;
		private Long semesterId;
		private String semester;
		private String semesterDisplayName;
		private String paymentDate;
		private LocalDateTime paymentDateTime;
		private String status;

		// Thông tin tài chính
		private BigDecimal totalAmount;
		private BigDecimal paidAmount;
		private BigDecimal remainingAmount;

		// Chi tiết thanh toán
		private List<PaymentDetailDTO> paymentDetails;

		// Thông tin dành riêng cho sinh viên
		private boolean canCreatePayment;

		// Constructors
		public PaymentSummaryDTO() {
			this.canCreatePayment = false;
			this.paidAmount = BigDecimal.ZERO;
		}

		// Constructor đầy đủ cho Principal/Admin
		public PaymentSummaryDTO(Long id, Long studentId, String studentName, String studentCode, String studentClass,
				Long semesterId, String semester, String semesterDisplayName, String paymentDate,
				LocalDateTime paymentDateTime, String status, BigDecimal totalAmount, BigDecimal paidAmount,
				List<PaymentDetailDTO> paymentDetails) {
			this.id = id;
			this.studentId = studentId;
			this.studentName = studentName;
			this.studentCode = studentCode;
			this.studentClass = studentClass;
			this.semesterId = semesterId;
			this.semester = semester;
			this.semesterDisplayName = semesterDisplayName;
			this.paymentDate = paymentDate;
			this.paymentDateTime = paymentDateTime;
			this.status = status;
			this.totalAmount = totalAmount;
			this.paidAmount = paidAmount != null ? paidAmount : BigDecimal.ZERO;
			this.paymentDetails = paymentDetails;
			this.canCreatePayment = false;
			recalculateDerivedFields();
		}

		// Constructor đơn giản cho Student
		public PaymentSummaryDTO(Long semesterId, String semester, String semesterDisplayName, BigDecimal totalAmount,
				BigDecimal paidAmount, String status, LocalDateTime paymentDateTime,
				List<PaymentDetailDTO> paymentDetails, boolean canCreatePayment) {
			this.semesterId = semesterId;
			this.semester = semester;
			this.semesterDisplayName = semesterDisplayName;
			this.totalAmount = totalAmount;
			this.paidAmount = paidAmount != null ? paidAmount : BigDecimal.ZERO;
			this.status = status;
			this.paymentDateTime = paymentDateTime;
			this.paymentDetails = paymentDetails;
			this.canCreatePayment = canCreatePayment;
			this.paymentDate = paymentDateTime != null ? paymentDateTime.toString() : null;
			recalculateDerivedFields();
		}

		// Getters and Setters
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getStudentId() {
			return studentId;
		}

		public void setStudentId(Long studentId) {
			this.studentId = studentId;
		}

		public String getStudentName() {
			return studentName;
		}

		public void setStudentName(String studentName) {
			this.studentName = studentName;
		}

		public String getStudentCode() {
			return studentCode;
		}

		public void setStudentCode(String studentCode) {
			this.studentCode = studentCode;
		}

		public String getStudentClass() {
			return studentClass;
		}

		public void setStudentClass(String studentClass) {
			this.studentClass = studentClass;
		}

		public Long getSemesterId() {
			return semesterId;
		}

		public void setSemesterId(Long semesterId) {
			this.semesterId = semesterId;
		}

		public String getSemester() {
			return semester;
		}

		public void setSemester(String semester) {
			this.semester = semester;
		}

		public String getSemesterDisplayName() {
			return semesterDisplayName;
		}

		public void setSemesterDisplayName(String semesterDisplayName) {
			this.semesterDisplayName = semesterDisplayName;
		}

		public String getPaymentDate() {
			return paymentDate;
		}

		public void setPaymentDate(String paymentDate) {
			this.paymentDate = paymentDate;
		}

		public LocalDateTime getPaymentDateTime() {
			return paymentDateTime;
		}

		public void setPaymentDateTime(LocalDateTime paymentDateTime) {
			this.paymentDateTime = paymentDateTime;
			this.paymentDate = paymentDateTime != null ? paymentDateTime.toString() : null;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public BigDecimal getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(BigDecimal totalAmount) {
			this.totalAmount = totalAmount;
			recalculateDerivedFields();
		}

		public BigDecimal getPaidAmount() {
			return paidAmount;
		}

		public void setPaidAmount(BigDecimal paidAmount) {
			this.paidAmount = paidAmount;
			recalculateDerivedFields();
		}

		public BigDecimal getRemainingAmount() {
			return remainingAmount;
		}

		public void setRemainingAmount(BigDecimal remainingAmount) {
			this.remainingAmount = remainingAmount;
		}

		public List<PaymentDetailDTO> getPaymentDetails() {
			return paymentDetails;
		}

		public void setPaymentDetails(List<PaymentDetailDTO> paymentDetails) {
			this.paymentDetails = paymentDetails;
		}

		public boolean isCanCreatePayment() {
			return canCreatePayment;
		}

		public void setCanCreatePayment(boolean canCreatePayment) {
			this.canCreatePayment = canCreatePayment;
		}

		private void recalculateDerivedFields() {
			if (totalAmount != null && paidAmount != null) {
				this.remainingAmount = totalAmount.subtract(paidAmount);
			} else if (totalAmount != null) {
				this.remainingAmount = totalAmount;
			} else {
				this.remainingAmount = BigDecimal.ZERO;
			}

			this.canCreatePayment = (totalAmount != null && totalAmount.compareTo(BigDecimal.ZERO) > 0)
					&& paymentDateTime == null;
		}
	}

	// DTO cho yêu cầu thay đổi mật khẩu (không cần mật khẩu hiện tại)
	public static class ChangePasswordRequest {
		private String newPassword;
		private String confirmPassword;

		// Constructors
		public ChangePasswordRequest() {
		}

		public ChangePasswordRequest(String newPassword, String confirmPassword) {
			this.newPassword = newPassword;
			this.confirmPassword = confirmPassword;
		}

		public String getNewPassword() {
			return newPassword;
		}

		public void setNewPassword(String newPassword) {
			this.newPassword = newPassword;
		}

		public String getConfirmPassword() {
			return confirmPassword;
		}

		public void setConfirmPassword(String confirmPassword) {
			this.confirmPassword = confirmPassword;
		}
	}

	public static class ChangePassword {
		private final BCryptPasswordEncoder passwordEncoder;
		private final UserRepository userRepository;

		public ChangePassword(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
			this.passwordEncoder = passwordEncoder;
			this.userRepository = userRepository;
		}

		public ApiResponse changePassword(Long userId, ChangePasswordRequest request) {
			try {
				// Validate input - chỉ cần mật khẩu mới
				if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
					return new ApiResponse(false, "Mật khẩu mới không được để trống");
				}

				if (request.getNewPassword().length() < 6) {
					return new ApiResponse(false, "Mật khẩu mới phải có ít nhất 6 ký tự");
				}

				if (!request.getNewPassword().equals(request.getConfirmPassword())) {
					return new ApiResponse(false, "Xác nhận mật khẩu không khớp");
				}

				User user = userRepository.findById(userId).orElseThrow(
						() -> new ResourceNotFoundException("Không tìm thấy thông tin user ID: " + userId));

				// Mã hóa mật khẩu mới bằng BCrypt
				String encodedPassword = passwordEncoder.encode(request.getNewPassword());

				// Update password với mật khẩu đã mã hóa
				user.setPassword(encodedPassword);
				userRepository.save(user);

				logger.info("Password changed successfully for user ID: {}", userId);
				return new ApiResponse(true, "Đổi mật khẩu thành công");

			} catch (Exception e) {
				logger.error("Error changing password for user ID: {}", userId, e);
				return new ApiResponse(false, "Lỗi hệ thống: " + e.getMessage());
			}
		}
	}

	public static class getAllSemester {
		private final SemesterRepository semesterRepository;

		public getAllSemester(SemesterRepository semesterRepository) {
			this.semesterRepository = semesterRepository;
		}

		// Tạo display name cho semester (ví dụ: 2024-1 -> Học kỳ 1 (2024-2025))
		public static String generateDisplayName(String semester) {
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

		public List<SemesterInfo> getAllSemesters() {
			return semesterRepository.findAll().stream().map(semester -> {
				String displayName = generateDisplayName(semester.getSemester());
				return new SemesterInfo(semester.getId(), semester.getSemester(), displayName);
			}).sorted((s1, s2) -> s2.getSemester().compareTo(s1.getSemester())) // Sort descending (newest first)
					.collect(Collectors.toList());
		}

	}

	// Helper method để escape CSV values
	public static String escapeCSV(String value) {
		if (value == null)
			return "";
		if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
			return "\"" + value.replace("\"", "\"\"") + "\"";
		}
		return value;
	}

	public static final class SemesterUtils {
		private SemesterUtils() {
		}

		public static Optional<Semester> findByCode(SemesterRepository semesterRepository, String semester) {
			if (semesterRepository == null || semester == null || semester.trim().isEmpty()) {
				return Optional.empty();
			}
			return semesterRepository.findAll().stream().filter(s -> semester.equals(s.getSemester())).findFirst();
		}

		public static Semester requireByCode(SemesterRepository semesterRepository, String semester) {
			return findByCode(semesterRepository, semester)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy kỳ học: " + semester));
		}

		public static Long resolveSemesterId(SemesterRepository semesterRepository, String semester,
				Long fallbackSemesterId) {
			return findByCode(semesterRepository, semester).map(Semester::getId).orElse(fallbackSemesterId);
		}

		public static Long resolveSemesterId(SemesterRepository semesterRepository, String semester) {
			return resolveSemesterId(semesterRepository, semester, 1L);
		}
	}

}
