package com.example.app.exception;

// xử lý lỗi trùng lặp
public class DuplicateResourceException extends RuntimeException {
	public DuplicateResourceException(String message) {
		super(message);
	}

	public DuplicateResourceException(String message, Throwable cause) {
		super(message, cause);
	}
}
