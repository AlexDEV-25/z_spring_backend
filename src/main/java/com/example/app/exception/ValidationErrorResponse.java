package com.example.app.exception;

import java.time.LocalDateTime;
import java.util.Map;

// phản hồi đặc biệt khi có lỗi validate dữ liệu
public class ValidationErrorResponse extends ErrorResponse {
	private Map<String, String> validationErrors;

	public ValidationErrorResponse(int status, String message, LocalDateTime timestamp, String path,
			Map<String, String> validationErrors) {
		super(status, message, timestamp, path);
		this.validationErrors = validationErrors;
	}

	public Map<String, String> getValidationErrors() {
		return validationErrors;
	}

	public void setValidationErrors(Map<String, String> validationErrors) {
		this.validationErrors = validationErrors;
	}
}
