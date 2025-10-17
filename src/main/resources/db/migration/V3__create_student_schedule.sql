-- Tạo bảng student_schedule để lưu thời khóa biểu sinh viên
-- Liên kết trực tiếp với bảng teaching để đảm bảo tính toàn vẹn dữ liệu

CREATE TABLE IF NOT EXISTS student_schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    enrollment_id BIGINT NOT NULL,
    teaching_id BIGINT NOT NULL,
    semester VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign keys
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (enrollment_id) REFERENCES enrollments(id) ON DELETE CASCADE,
    FOREIGN KEY (teaching_id) REFERENCES teaching(id) ON DELETE CASCADE,
    
    -- Indexes
    INDEX idx_student_id (student_id),
    INDEX idx_semester (semester),
    INDEX idx_student_semester (student_id, semester),
    INDEX idx_teaching_id (teaching_id),
    
    -- Unique constraint: một sinh viên chỉ có một bản ghi cho mỗi enrollment
    UNIQUE KEY uk_student_enrollment (student_id, enrollment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- View để lấy thông tin đầy đủ từ các bảng liên quan
CREATE OR REPLACE VIEW v_student_schedule_detail AS
SELECT 
    ss.id,
    ss.student_id,
    ss.enrollment_id,
    ss.teaching_id,
    ss.semester,
    ss.created_at,
    -- Thông tin từ teaching
    t.course_id,
    t.day_of_week,
    t.period,
    t.class_room as classroom,
    t.lecturer_id,
    -- Thông tin từ course
    c.course_code,
    c.name as course_name,
    c.credit,
    -- Thông tin giảng viên
    u.full_name as lecturer_name
FROM student_schedule ss
INNER JOIN teaching t ON ss.teaching_id = t.id
INNER JOIN courses c ON t.course_id = c.id
LEFT JOIN lecturers l ON t.lecturer_id = l.id
LEFT JOIN users u ON l.user_id = u.id;
