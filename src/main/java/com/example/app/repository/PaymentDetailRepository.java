package com.example.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.model.PaymentDetail;

/**
 * Repository cho PaymentDetail - ĐơN GIẢN như StudentScheduleRepository
 * CHỈ LƯU ID LIÊN KẾT, dữ liệu thực tế lấy qua JOIN
 */
@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {

    /**
     * Lấy tất cả payment details theo payment_id
     */
    List<PaymentDetail> findByPaymentId(Long paymentId);

    /**
     * Lấy payment details theo payment_id và semester
     */
    List<PaymentDetail> findByPaymentIdAndSemester(Long paymentId, String semester);

    /**
     * Lấy payment detail theo enrollment_id
     */
    PaymentDetail findByEnrollmentId(Long enrollmentId);

    /**
     * Kiểm tra enrollment đã có payment detail chưa
     */
    boolean existsByEnrollmentId(Long enrollmentId);

    /**
     * Xóa payment details theo payment_id
     */
    @Modifying
    @Transactional
    void deleteByPaymentId(Long paymentId);

    /**
     * Xóa payment details theo payment_id và semester
     */
    @Modifying
    @Transactional
    void deleteByPaymentIdAndSemester(Long paymentId, String semester);
}
