package com.example.app.dto;

/**
 * DTO trả về thông tin đầy đủ của thời khóa biểu
 * Lấy từ view v_student_schedule_detail hoặc join các bảng
 */
public class StudentScheduleDetailDTO {
    
    private Long id;
    private Long studentId;
    private Long enrollmentId;
    private Long teachingId;
    private String semester;
    
    // Thông tin từ teaching và course
    private Long courseId;
    private String courseCode;
    private String courseName;
    private Integer credit;
    private String dayOfWeek;
    private String period;
    private String classroom;
    
    // Thông tin giảng viên
    private Long lecturerId;
    private String lecturerName;

    // Constructors
    public StudentScheduleDetailDTO() {
    }

    public StudentScheduleDetailDTO(Long id, Long studentId, Long enrollmentId, Long teachingId, 
                                   String semester, Long courseId, String courseCode, String courseName, 
                                   Integer credit, String dayOfWeek, String period, String classroom, 
                                   Long lecturerId, String lecturerName) {
        this.id = id;
        this.studentId = studentId;
        this.enrollmentId = enrollmentId;
        this.teachingId = teachingId;
        this.semester = semester;
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credit = credit;
        this.dayOfWeek = dayOfWeek;
        this.period = period;
        this.classroom = classroom;
        this.lecturerId = lecturerId;
        this.lecturerName = lecturerName;
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

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public Long getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(Long lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }
}
