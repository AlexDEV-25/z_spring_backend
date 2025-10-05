package com.example.app.share;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.app.service.StudentPortalService;

public class Share {
	private static final Logger logger = LoggerFactory.getLogger(StudentPortalService.class);

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

	/**
	 * Tạo display name cho semester (ví dụ: 2024-1 -> Học kỳ 1 (2024-2025))
	 */
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

	/**
	 * Helper method để escape CSV values
	 */
	public static String escapeCSV(String value) {
		if (value == null)
			return "";
		if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
			return "\"" + value.replace("\"", "\"\"") + "\"";
		}
		return value;
	}
}
