package com.example.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.app.model.Enrollment;

import jakarta.transaction.Transactional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
	@Modifying
	@Transactional
	@Query("UPDATE Enrollment e SET e.courseId = null WHERE e.courseId = :courseId")
	void setCourseIdNullByCourseId(@Param("courseId") Long courseId);

	List<Enrollment> findByStudentId(Long studentId);

	void deleteByStudentId(Long studentId);

	// Thêm các method hỗ trợ cho student portal
	Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

	boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

	@Query("SELECT e FROM Enrollment e WHERE e.studentId = :studentId AND e.courseId = :courseId")
	Optional<Enrollment> findEnrollmentByStudentAndCourse(@Param("studentId") Long studentId,
			@Param("courseId") Long courseId);

	@Query("SELECT COUNT(e) FROM Enrollment e WHERE e.courseId = :courseId")
	Long countByCourseId(@Param("courseId") Long courseId);

	// Methods for payment functionality
	@Query("SELECT e FROM Enrollment e JOIN Course c ON e.courseId = c.id JOIN Semester s ON c.semesterId = s.id WHERE e.studentId = :studentId AND s.semester = :semester")
	List<Enrollment> findByStudentIdAndSemester(@Param("studentId") Long studentId, @Param("semester") String semester);

	@Query("SELECT DISTINCT s.semester FROM Enrollment e JOIN Course c ON e.courseId = c.id JOIN Semester s ON c.semesterId = s.id WHERE e.studentId = :studentId ORDER BY s.semester DESC")
	List<String> findDistinctSemestersByStudentId(@Param("studentId") Long studentId);
}