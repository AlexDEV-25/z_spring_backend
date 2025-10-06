package com.example.app.share;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
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

	// DTO cho response thay đổi mật khẩu
	public static class ChangePasswordResponse {
		private boolean success;
		private String message;

		// Constructors
		public ChangePasswordResponse() {
		}

		public ChangePasswordResponse(boolean success, String message) {
			this.success = success;
			this.message = message;
		}

		// Getters and Setters
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

	public static class ChangePassword {
		private final BCryptPasswordEncoder passwordEncoder;
		private final UserRepository userRepository;

		public ChangePassword(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
			this.passwordEncoder = passwordEncoder;
			this.userRepository = userRepository;
		}

		public ChangePasswordResponse changePassword(Long userId, ChangePasswordRequest request) {
			try {
				// Validate input - chỉ cần mật khẩu mới
				if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
					return new Share.ChangePasswordResponse(false, "Mật khẩu mới không được để trống");
				}

				if (request.getNewPassword().length() < 6) {
					return new Share.ChangePasswordResponse(false, "Mật khẩu mới phải có ít nhất 6 ký tự");
				}

				if (!request.getNewPassword().equals(request.getConfirmPassword())) {
					return new Share.ChangePasswordResponse(false, "Xác nhận mật khẩu không khớp");
				}

				User user = userRepository.findById(userId).orElseThrow(
						() -> new ResourceNotFoundException("Không tìm thấy thông tin user ID: " + userId));

				// Mã hóa mật khẩu mới bằng BCrypt
				String encodedPassword = passwordEncoder.encode(request.getNewPassword());

				// Update password với mật khẩu đã mã hóa
				user.setPassword(encodedPassword);
				userRepository.save(user);

				logger.info("Password changed successfully for user ID: {}", userId);
				return new Share.ChangePasswordResponse(true, "Đổi mật khẩu thành công");

			} catch (Exception e) {
				logger.error("Error changing password for user ID: {}", userId, e);
				return new Share.ChangePasswordResponse(false, "Lỗi hệ thống: " + e.getMessage());
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

		public List<Share.SemesterInfo> getAllSemesters() {
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
