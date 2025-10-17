package com.example.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.model.StudentSchedule;

@Repository
public interface StudentScheduleRepository extends JpaRepository<StudentSchedule, Long> {
    
    /**
     * Lấy toàn bộ thời khóa biểu của sinh viên
     */
    List<StudentSchedule> findByStudentId(Long studentId);
    
    /**
     * Lấy thời khóa biểu của sinh viên theo semester
     */
    List<StudentSchedule> findByStudentIdAndSemester(Long studentId, String semester);
    
    /**
     * Xóa toàn bộ thời khóa biểu của sinh viên trong semester
     */
    @Modifying
    @Transactional
    void deleteByStudentIdAndSemester(Long studentId, String semester);
    
    /**
     * Kiểm tra sinh viên đã có thời khóa biểu trong semester chưa
     */
    boolean existsByStudentIdAndSemester(Long studentId, String semester);
}
