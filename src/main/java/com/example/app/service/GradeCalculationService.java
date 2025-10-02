package com.example.app.service;

import org.springframework.stereotype.Service;

@Service
public class GradeCalculationService {

    /**
     * Tính điểm tổng kết từ các điểm thành phần
     * Công thức: (componentScore1 * 0.2) + (componentScore2 * 0.3) + (finalExamScore * 0.5)
     */
    public Double calculateTotalScore(Double componentScore1, Double componentScore2, Double finalExamScore) {
        if (componentScore1 == null || componentScore2 == null || finalExamScore == null) {
            return null;
        }
        
        return (componentScore1 * 0.2) + (componentScore2 * 0.3) + (finalExamScore * 0.5);
    }

    /**
     * Chuyển đổi điểm hệ 10 sang hệ 4
     */
    public Double convertToCoefficient4(Double totalScore) {
        if (totalScore == null) {
            return null;
        }

        if (totalScore >= 9.5) return 4.0;
        if (totalScore >= 8.5) return 4.0;
        if (totalScore >= 8.0) return 3.5;
        if (totalScore >= 7.0) return 3.0;
        if (totalScore >= 6.5) return 2.5;
        if (totalScore >= 5.5) return 2.0;
        if (totalScore >= 5.0) return 1.5;
        if (totalScore >= 4.0) return 1.0;
        return 0.0;
    }

    /**
     * Chuyển đổi điểm số sang điểm chữ
     */
    public String convertToLetterGrade(Double totalScore) {
        if (totalScore == null) {
            return null;
        }

        if (totalScore >= 9.5) return "A+";
        if (totalScore >= 8.5) return "A";
        if (totalScore >= 8.0) return "B+";
        if (totalScore >= 7.0) return "B";
        if (totalScore >= 6.5) return "C+";
        if (totalScore >= 5.5) return "C";
        if (totalScore >= 5.0) return "D+";
        if (totalScore >= 4.0) return "D";
        return "F";
    }

    /**
     * Lấy xếp loại từ điểm số
     */
    public String getClassification(Double totalScore) {
        if (totalScore == null) {
            return null;
        }

        if (totalScore >= 9.5) return "Xuất sắc";
        if (totalScore >= 8.5) return "Giỏi";
        if (totalScore >= 8.0) return "Khá giỏi";
        if (totalScore >= 7.0) return "Khá";
        if (totalScore >= 6.5) return "TB khá";
        if (totalScore >= 5.5) return "Trung bình";
        if (totalScore >= 5.0) return "TB yếu";
        if (totalScore >= 4.0) return "Yếu (vẫn qua môn)";
        return "Trượt";
    }

    /**
     * Lấy thang điểm tương đương
     */
    public String getScoreRange(Double totalScore) {
        if (totalScore == null) {
            return null;
        }

        if (totalScore >= 9.5) return "9.5 – 10.0";
        if (totalScore >= 8.5) return "8.5 – 9.4";
        if (totalScore >= 8.0) return "8.0 – 8.4";
        if (totalScore >= 7.0) return "7.0 – 7.9";
        if (totalScore >= 6.5) return "6.5 – 6.9";
        if (totalScore >= 5.5) return "5.5 – 6.4";
        if (totalScore >= 5.0) return "5.0 – 5.4";
        if (totalScore >= 4.0) return "4.0 – 4.9";
        return "< 4.0";
    }
}
