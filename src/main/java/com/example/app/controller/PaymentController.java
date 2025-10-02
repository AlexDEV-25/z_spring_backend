package com.example.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.PaymentDTO;
import com.example.app.dto.PrincipalPortalInfo;
import com.example.app.model.Payment;
import com.example.app.service.PaymentService;

@RestController
@RequestMapping("/api/admin/payments")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	/**
	 * Lấy tất cả payments với filtering
	 */
	@GetMapping
	public ResponseEntity<List<PrincipalPortalInfo.PaymentWithDetails>> getAllPayments(
			@RequestParam(required = false) String status, @RequestParam(required = false) String semester) {
		try {
			logger.info("Getting all payments with status: {} and semester: {}", status, semester);
			List<PrincipalPortalInfo.PaymentWithDetails> payments = paymentService.getAllPayments(status, semester);
			return ResponseEntity.ok(payments);
		} catch (Exception e) {
			logger.error("Error getting all payments", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Lấy payment theo ID
	 */
	@GetMapping("/{id}")
	public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
		try {
			logger.info("Getting payment by ID: {}", id);
			PaymentDTO payment = paymentService.getPaymentById(id);
			return ResponseEntity.ok(payment);
		} catch (Exception e) {
			logger.error("Error getting payment by ID: {}", id, e);
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Lấy payment detail với thông tin đầy đủ
	 */
	@GetMapping("/{id}/detail")
	public ResponseEntity<PrincipalPortalInfo.PaymentDetailResponse> getPaymentDetail(@PathVariable Long id) {
		try {
			logger.info("Getting payment detail for ID: {}", id);
			PrincipalPortalInfo.PaymentDetailResponse paymentDetail = paymentService.getPaymentDetail(id);
			return ResponseEntity.ok(paymentDetail);
		} catch (Exception e) {
			logger.error("Error getting payment detail for ID: {}", id, e);
			return ResponseEntity.notFound().build();
		}
	}

	// Cập nhật trạng thái thanh toán
	@PutMapping("/{id}/status")
	public ResponseEntity<PrincipalPortalInfo.PaymentStatusUpdateResponse> updatePaymentStatus(@PathVariable Long id,
			@RequestBody PrincipalPortalInfo.PaymentStatusUpdateRequest request) {
		try {
			logger.info("Updating payment status for ID: {} to status: {}", id, request.getStatus());

			PaymentDTO updatedPayment = paymentService.updatePaymentStatus(id, request.getStatus(), request.getReason());

			return ResponseEntity.ok(new PrincipalPortalInfo.PaymentStatusUpdateResponse(true,
					"Cập nhật trạng thái thanh toán thành công", updatedPayment));
		} catch (Exception e) {
			logger.error("Error updating payment status for ID: {}", id, e);
			return ResponseEntity.ok(new PrincipalPortalInfo.PaymentStatusUpdateResponse(false,
					"Lỗi khi cập nhật trạng thái: " + e.getMessage(), null));
		}
	}

	/**
	 * Lấy payments theo student ID
	 */
	@GetMapping("/student/{studentId}")
	public ResponseEntity<List<Payment>> getPaymentsByStudentId(@PathVariable Long studentId) {
		try {
			logger.info("Getting payments for student ID: {}", studentId);
			List<Payment> payments = paymentService.getPaymentsByStudentId(studentId);
			return ResponseEntity.ok(payments);
		} catch (Exception e) {
			logger.error("Error getting payments for student ID: {}", studentId, e);
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Lấy thống kê payments
	 */
	@GetMapping("/statistics")
	public ResponseEntity<PrincipalPortalInfo.PaymentStatistics> getPaymentStatistics(
			@RequestParam(required = false) String semester) {
		try {
			logger.info("Getting payment statistics for semester: {}", semester);
			PrincipalPortalInfo.PaymentStatistics stats = paymentService.getPaymentStatistics(semester);
			return ResponseEntity.ok(stats);
		} catch (Exception e) {
			logger.error("Error getting payment statistics", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Xuất danh sách payments ra file CSV
	 */
	@GetMapping("/export")
	public ResponseEntity<byte[]> exportPayments(@RequestParam(required = false) String semester) {
		try {
			logger.info("Exporting payments for semester: {}", semester);

			byte[] csvData = paymentService.exportPaymentsToCsv(semester);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
			headers.setContentDispositionFormData("attachment", "danh_sach_thanh_toan.csv");

			return ResponseEntity.ok().headers(headers).body(csvData);
		} catch (Exception e) {
			logger.error("Error exporting payments", e);
			return ResponseEntity.internalServerError().build();
		}
	}
}
