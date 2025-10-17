package com.example.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity lưu thời khóa biểu sinh viên
 * Chỉ lưu các ID liên kết, thông tin chi tiết lấy từ teaching table
 */
@Entity
@Table(name = "student_schedule")
public class StudentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "enrollment_id", nullable = false)
    private Long enrollmentId;

    @Column(name = "teaching_id", nullable = false)
    private Long teachingId;

    @Column(name = "semester", nullable = false, length = 20)
    private String semester;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public StudentSchedule() {
    }

    public StudentSchedule(Long studentId, Long enrollmentId, Long teachingId, String semester) {
        this.studentId = studentId;
        this.enrollmentId = enrollmentId;
        this.teachingId = teachingId;
        this.semester = semester;
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

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Long getTeachingId() {
        return teachingId;
    }

    public void setTeachingId(Long teachingId) {
        this.teachingId = teachingId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
