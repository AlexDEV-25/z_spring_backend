package com.example.app.dto;

import java.math.BigDecimal;

/**
 * DTO cho payment detail với thông tin đầy đủ
 * GIỐNG StudentScheduleDetailDTO - JOIN để lấy dữ liệu từ enrollment và course
 */
public class PaymentDetailDTO {

    // ID từ payment_details table
    private Long id;
    private Long paymentId;
    private Long enrollmentId;
    private String semester;

    // Thông tin từ course (qua enrollment.course_id)
    private Long courseId;
    private String courseCode;
    private String courseName;
    private Integer credit;
    private BigDecimal fee;

    // Constructors
    public PaymentDetailDTO() {
    }

    public PaymentDetailDTO(Long id, Long paymentId, Long enrollmentId, String semester,
            Long courseId, String courseCode, String courseName, Integer credit, BigDecimal fee) {
        this.id = id;
        this.paymentId = paymentId;
        this.enrollmentId = enrollmentId;
        this.semester = semester;
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credit = credit;
        this.fee = fee;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}
