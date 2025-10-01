package com.example.app.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.model.Payment;
import com.example.app.service.PaymentService;

@RestController
@RequestMapping("/api/admin/payments")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    
    private final PaymentService adminPaymentService;

    public PaymentController(PaymentService adminPaymentService) {
        this.adminPaymentService = adminPaymentService;
    }

    /**
     * Lấy tất cả payments với filtering
     */
    @GetMapping
    public ResponseEntity<List<PaymentWithDetails>> getAllPayments(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String semester) {
        try {
            logger.info("Getting all payments with status: {} and semester: {}", status, semester);
            List<PaymentWithDetails> payments = adminPaymentService.getAllPayments(status, semester);
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
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        try {
            logger.info("Getting payment by ID: {}", id);
            Payment payment = adminPaymentService.getPaymentById(id);
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
    public ResponseEntity<PaymentDetailResponse> getPaymentDetail(@PathVariable Long id) {
        try {
            logger.info("Getting payment detail for ID: {}", id);
            PaymentDetailResponse paymentDetail = adminPaymentService.getPaymentDetail(id);
            return ResponseEntity.ok(paymentDetail);
        } catch (Exception e) {
            logger.error("Error getting payment detail for ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cập nhật trạng thái thanh toán
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentStatusUpdateResponse> updatePaymentStatus(
            @PathVariable Long id, 
            @RequestBody PaymentStatusUpdateRequest request) {
        try {
            logger.info("Updating payment status for ID: {} to status: {}", id, request.getStatus());
            
            Payment updatedPayment = adminPaymentService.updatePaymentStatus(id, request.getStatus(), request.getReason());
            
            return ResponseEntity.ok(new PaymentStatusUpdateResponse(
                true, 
                "Cập nhật trạng thái thanh toán thành công", 
                updatedPayment
            ));
        } catch (Exception e) {
            logger.error("Error updating payment status for ID: {}", id, e);
            return ResponseEntity.ok(new PaymentStatusUpdateResponse(
                false, 
                "Lỗi khi cập nhật trạng thái: " + e.getMessage(), 
                null
            ));
        }
    }

    /**
     * Lấy payments theo student ID
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Payment>> getPaymentsByStudentId(@PathVariable Long studentId) {
        try {
            logger.info("Getting payments for student ID: {}", studentId);
            List<Payment> payments = adminPaymentService.getPaymentsByStudentId(studentId);
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
    public ResponseEntity<PaymentStatistics> getPaymentStatistics(
            @RequestParam(required = false) String semester) {
        try {
            logger.info("Getting payment statistics for semester: {}", semester);
            PaymentStatistics stats = adminPaymentService.getPaymentStatistics(semester);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error getting payment statistics", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // DTO classes
    public static class PaymentStatusUpdateRequest {
        private String status;
        private String reason;

        public PaymentStatusUpdateRequest() {}

        public PaymentStatusUpdateRequest(String status, String reason) {
            this.status = status;
            this.reason = reason;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    public static class PaymentStatusUpdateResponse {
        private boolean success;
        private String message;
        private Payment payment;

        public PaymentStatusUpdateResponse() {}

        public PaymentStatusUpdateResponse(boolean success, String message, Payment payment) {
            this.success = success;
            this.message = message;
            this.payment = payment;
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

        public Payment getPayment() {
            return payment;
        }

        public void setPayment(Payment payment) {
            this.payment = payment;
        }
    }

    public static class PaymentStatistics {
        private long totalPayments;
        private long paidPayments;
        private long pendingPayments;
        private long failedPayments;
        private double totalAmount;
        private double paidAmount;
        private double pendingAmount;

        public PaymentStatistics() {}

        public PaymentStatistics(long totalPayments, long paidPayments, long pendingPayments, 
                long failedPayments, double totalAmount, double paidAmount, double pendingAmount) {
            this.totalPayments = totalPayments;
            this.paidPayments = paidPayments;
            this.pendingPayments = pendingPayments;
            this.failedPayments = failedPayments;
            this.totalAmount = totalAmount;
            this.paidAmount = paidAmount;
            this.pendingAmount = pendingAmount;
        }

        // Getters and setters
        public long getTotalPayments() { return totalPayments; }
        public void setTotalPayments(long totalPayments) { this.totalPayments = totalPayments; }

        public long getPaidPayments() { return paidPayments; }
        public void setPaidPayments(long paidPayments) { this.paidPayments = paidPayments; }

        public long getPendingPayments() { return pendingPayments; }
        public void setPendingPayments(long pendingPayments) { this.pendingPayments = pendingPayments; }

        public long getFailedPayments() { return failedPayments; }
        public void setFailedPayments(long failedPayments) { this.failedPayments = failedPayments; }

        public double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

        public double getPaidAmount() { return paidAmount; }
        public void setPaidAmount(double paidAmount) { this.paidAmount = paidAmount; }

        public double getPendingAmount() { return pendingAmount; }
        public void setPendingAmount(double pendingAmount) { this.pendingAmount = pendingAmount; }
    }

    public static class PaymentDetailResponse {
        private Long id;
        private Long studentId;
        private String studentName;
        private String studentClass;
        private Long semesterId;
        private String semesterName;
        private String paymentDate;
        private String status;
        private List<CoursePaymentDetail> courses;
        private double totalAmount;

        public PaymentDetailResponse() {}

        public PaymentDetailResponse(Long id, Long studentId, String studentName, String studentClass,
                Long semesterId, String semesterName, String paymentDate, String status,
                List<CoursePaymentDetail> courses, double totalAmount) {
            this.id = id;
            this.studentId = studentId;
            this.studentName = studentName;
            this.studentClass = studentClass;
            this.semesterId = semesterId;
            this.semesterName = semesterName;
            this.paymentDate = paymentDate;
            this.status = status;
            this.courses = courses;
            this.totalAmount = totalAmount;
        }

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }

        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }

        public String getStudentClass() { return studentClass; }
        public void setStudentClass(String studentClass) { this.studentClass = studentClass; }

        public Long getSemesterId() { return semesterId; }
        public void setSemesterId(Long semesterId) { this.semesterId = semesterId; }

        public String getSemesterName() { return semesterName; }
        public void setSemesterName(String semesterName) { this.semesterName = semesterName; }

        public String getPaymentDate() { return paymentDate; }
        public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public List<CoursePaymentDetail> getCourses() { return courses; }
        public void setCourses(List<CoursePaymentDetail> courses) { this.courses = courses; }

        public double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    }

    public static class CoursePaymentDetail {
        private Long courseId;
        private String courseCode;
        private String courseName;
        private Integer credits;
        private double fee;

        public CoursePaymentDetail() {}

        public CoursePaymentDetail(Long courseId, String courseCode, String courseName, Integer credits, double fee) {
            this.courseId = courseId;
            this.courseCode = courseCode;
            this.courseName = courseName;
            this.credits = credits;
            this.fee = fee;
        }

        // Getters and setters
        public Long getCourseId() { return courseId; }
        public void setCourseId(Long courseId) { this.courseId = courseId; }

        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

        public String getCourseName() { return courseName; }
        public void setCourseName(String courseName) { this.courseName = courseName; }

        public Integer getCredits() { return credits; }
        public void setCredits(Integer credits) { this.credits = credits; }

        public double getFee() { return fee; }
        public void setFee(double fee) { this.fee = fee; }
    }

    public static class PaymentWithDetails {
        private Long id;
        private Long studentId;
        private String studentCode;
        private Long semesterId;
        private String semesterName;
        private LocalDateTime paymentDate;
        private Payment.PaymentStatus status;

        public PaymentWithDetails() {}

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }

        public String getStudentCode() { return studentCode; }
        public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

        public Long getSemesterId() { return semesterId; }
        public void setSemesterId(Long semesterId) { this.semesterId = semesterId; }

        public String getSemesterName() { return semesterName; }
        public void setSemesterName(String semesterName) { this.semesterName = semesterName; }

        public LocalDateTime getPaymentDate() { return paymentDate; }
        public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

        public Payment.PaymentStatus getStatus() { return status; }
        public void setStatus(Payment.PaymentStatus status) { this.status = status; }
    }
}
