package com.example.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.app.model.Payment;

import jakarta.transaction.Transactional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	/**
	 * Tìm payment theo student ID và semester ID
	 */
	Optional<Payment> findByStudentIdAndSemesterId(Long studentId, Long semesterId);

	/**
	 * Lấy tất cả payments của một sinh viên
	 */
	List<Payment> findByStudentIdOrderByPaymentDateDesc(Long studentId);

	/**
	 * Lấy payments của sinh viên theo semester
	 */
	List<Payment> findByStudentIdAndSemesterIdOrderByPaymentDateDesc(Long studentId, Long semesterId);

	/**
	 * Kiểm tra xem sinh viên đã thanh toán cho semester chưa
	 */
	@Query("SELECT COUNT(p) > 0 FROM Payment p WHERE p.studentId = :studentId AND p.semesterId = :semesterId AND p.status = 'PAID'")
	boolean existsPaidPaymentByStudentAndSemester(@Param("studentId") Long studentId,
			@Param("semesterId") Long semesterId);

	/**
	 * Lấy danh sách payments theo status
	 */
	List<Payment> findByStudentIdAndStatus(Long studentId, Payment.PaymentStatus status);

	/**
	 * Lấy tất cả payments theo status (for admin)
	 */
	List<Payment> findByStatus(Payment.PaymentStatus status);

	/**
	 * Lấy payments theo semester ID
	 */
	List<Payment> findBySemesterId(Long semesterId);

	/**
	 * Lấy payments theo status và semester ID
	 */
	List<Payment> findByStatusAndSemesterId(Payment.PaymentStatus status, Long semesterId);

	@Transactional
	@Modifying
	@Query("UPDATE Payment p SET p.studentId = null WHERE p.studentId = :studentId")
	void setStudentIdNullByStudentId(@Param("studentId") Long studentId);

	@Transactional
	@Modifying
	@Query("UPDATE Payment p SET p.semesterId = null WHERE p.semesterId = :semesterId")
	void setSemesterIdNullBySemesterId(@Param("semesterId") Long semesterId);

}
