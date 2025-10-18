-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.4.32-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for student_db
CREATE DATABASE IF NOT EXISTS `student_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `student_db`;

-- Dumping structure for table student_db.classes
CREATE TABLE IF NOT EXISTS `classes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `year` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table student_db.classes: ~4 rows (approximately)
INSERT INTO `classes` (`id`, `name`, `year`) VALUES
	(1, 'DH12C1', '2022-2026'),
	(2, 'DH12C2', '2022-2026'),
	(3, 'DH12QTKD1', '2022-2026'),
	(4, 'DH12QTKD2', '2022-2026');

-- Dumping structure for table student_db.courses
CREATE TABLE IF NOT EXISTS `courses` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `course_code` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `credit` int(11) NOT NULL,
  `fee` decimal(38,2) NOT NULL,
  `slot` int(11) DEFAULT NULL,
  `semester_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_courses_semesters` (`semester_id`),
  CONSTRAINT `FK_courses_semesters` FOREIGN KEY (`semester_id`) REFERENCES `semesters` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=163 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table student_db.courses: ~24 rows (approximately)
INSERT INTO `courses` (`id`, `course_code`, `name`, `credit`, `fee`, `slot`, `semester_id`) VALUES
	(1, 'IT101', 'Lập trình cơ bản', 3, 1000000.00, 60, 8),
	(2, 'IT102', 'Cấu trúc dữ liệu', 3, 1200000.00, 60, 8),
	(3, 'IT103', 'Cơ sở dữ liệu', 3, 1500000.00, 60, 8),
	(4, 'IT104', 'Mạng máy tính', 3, 1500000.00, 60, 7),
	(5, 'IT105', 'Hệ điều hành', 3, 1500000.00, 60, 7),
	(6, 'BUS101', 'Kinh tế vi mô', 3, 1200000.00, 60, 7),
	(7, 'BUS102', 'Kinh tế vĩ mô', 3, 1200000.00, 59, 6),
	(8, 'BUS103', 'Quản trị học', 3, 1200000.00, 60, 6),
	(9, 'BUS104', 'Marketing căn bản', 3, 1200000.00, 60, 6),
	(10, 'BUS105', 'Tài chính doanh nghiệp', 3, 1200000.00, 59, 5),
	(11, 'ENG101', 'Tiếng Anh 1', 2, 800000.00, 59, 5),
	(12, 'ENG102', 'Tiếng Anh 2', 2, 800000.00, 59, 5),
	(13, 'ENG103', 'Tiếng Anh chuyên ngành', 3, 1000000.00, 59, 4),
	(14, 'ENG104', 'Giao tiếp tiếng Anh', 2, 800000.00, 59, 4),
	(15, 'ENG105', 'Văn hóa Anh - Mỹ', 2, 800000.00, 59, 4),
	(16, 'MATH101', 'Toán cao cấp 1', 4, 1000000.00, 59, 3),
	(17, 'MATH102', 'Toán cao cấp 2', 4, 1000000.00, 59, 3),
	(18, 'MATH103', 'Xác suất thống kê', 3, 900000.00, 59, 3),
	(19, 'MATH104', 'Toán rời rạc', 3, 900000.00, 58, 2),
	(20, 'MATH105', 'Giải tích số', 3, 900000.00, 58, 2),
	(21, 'PHY101', 'Vật lý đại cương 1', 3, 1100000.00, 58, 2),
	(22, 'PHY102', 'Vật lý đại cương 2', 3, 1100000.00, 58, 1),
	(23, 'PHY103', 'Điện từ học', 3, 1100000.00, 58, 1),
	(24, 'PHIL101', 'Triết học Mác - Lênin', 3, 750000.00, 58, 1);

-- Dumping structure for table student_db.departments
CREATE TABLE IF NOT EXISTS `departments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKl7tivi5261wxdnvo6cct9gg6t` (`code`),
  UNIQUE KEY `UKj6cwks7xecs5jov19ro8ge3qk` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table student_db.departments: ~2 rows (approximately)
INSERT INTO `departments` (`id`, `code`, `name`) VALUES
	(1, 'CNTT', 'Công nghệ thông tin'),
	(2, 'QTKD', 'Quản Trị Kinh Doanh');

-- Dumping structure for table student_db.enrollments
CREATE TABLE IF NOT EXISTS `enrollments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `student_id` bigint(20) DEFAULT NULL,
  `course_id` bigint(20) DEFAULT NULL,
  `component_score_1` double DEFAULT NULL,
  `component_score_2` double DEFAULT NULL,
  `final_exam_score` double DEFAULT NULL,
  `total_score` double DEFAULT NULL,
  `score_coefficient_4` double DEFAULT NULL,
  `grade` varchar(255) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `student_id` (`student_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `enrollments_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`),
  CONSTRAINT `enrollments_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table student_db.enrollments: ~6 rows (approximately)
INSERT INTO `enrollments` (`id`, `student_id`, `course_id`, `component_score_1`, `component_score_2`, `final_exam_score`, `total_score`, `score_coefficient_4`, `grade`, `status`) VALUES
	(24, 1, 22, 10, 10, 10, 10, 4, 'A', 'ENROLLED'),
	(25, 1, 23, 9, 9, 9, 9, 4, 'A', 'ENROLLED'),
	(26, 1, 24, 9, 9, 9, 9, 4, 'A', 'ENROLLED'),
	(28, 1, 20, 9, 9, 9, 9, 4, 'A', 'ENROLLED'),
	(29, 1, 21, 9, 9, 9, 9, 4, 'A', 'ENROLLED'),
	(30, 1, 19, 9, 9, 9, 9, 4, 'A', 'ENROLLED');

-- Dumping structure for table student_db.lecturers
CREATE TABLE IF NOT EXISTS `lecturers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `lecturer_code` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `lecturer_code` (`lecturer_code`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `lecturers_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table student_db.lecturers: ~20 rows (approximately)
INSERT INTO `lecturers` (`id`, `user_id`, `lecturer_code`) VALUES
	(1, 2, 'GV001'),
	(2, 3, 'GV002'),
	(3, 4, 'GV003'),
	(4, 5, 'GV004'),
	(5, 6, 'GV005'),
	(6, 7, 'GV006'),
	(7, 8, 'GV007'),
	(8, 9, 'GV008'),
	(9, 10, 'GV009'),
	(10, 11, 'GV010'),
	(11, 12, 'GV011'),
	(12, 13, 'GV012'),
	(13, 14, 'GV013'),
	(14, 15, 'GV014'),
	(15, 16, 'GV015'),
	(16, 17, 'GV016'),
	(17, 18, 'GV017'),
	(18, 19, 'GV018'),
	(19, 20, 'GV019'),
	(20, 21, 'GV020');

-- Dumping structure for table student_db.payments
CREATE TABLE IF NOT EXISTS `payments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `student_id` bigint(20) DEFAULT NULL,
  `semester_id` bigint(20) DEFAULT NULL,
  `payment_date` datetime NOT NULL DEFAULT current_timestamp(),
  `status` enum('PENDING','PAID','FAILED') DEFAULT 'PENDING',
  `amount` double NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_payment_student` (`student_id`),
  KEY `fk_payment_semester` (`semester_id`),
  CONSTRAINT `FK_payments_semesters` FOREIGN KEY (`semester_id`) REFERENCES `semesters` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_payments_students` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table student_db.payments: ~2 rows (approximately)
INSERT INTO `payments` (`id`, `student_id`, `semester_id`, `payment_date`, `status`, `amount`, `description`) VALUES
	(10, 1, 1, '2025-10-18 02:50:24', 'PAID', 0, NULL),
	(11, 1, 2, '2025-10-18 03:30:04', 'PAID', 0, NULL);

-- Dumping structure for table student_db.payment_details
CREATE TABLE IF NOT EXISTS `payment_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `payment_id` bigint(20) NOT NULL,
  `enrollment_id` bigint(20) NOT NULL,
  `semester` varchar(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_payment_enrollment` (`payment_id`,`enrollment_id`),
  KEY `idx_payment_id` (`payment_id`),
  KEY `idx_enrollment_id` (`enrollment_id`),
  KEY `idx_semester` (`semester`),
  CONSTRAINT `payment_details_ibfk_1` FOREIGN KEY (`payment_id`) REFERENCES `payments` (`id`) ON DELETE CASCADE,
  CONSTRAINT `payment_details_ibfk_2` FOREIGN KEY (`enrollment_id`) REFERENCES `enrollments` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table student_db.payment_details: ~6 rows (approximately)
INSERT INTO `payment_details` (`id`, `payment_id`, `enrollment_id`, `semester`, `created_at`) VALUES
	(2, 10, 24, '2022-1', '2025-10-17 16:16:42'),
	(3, 10, 25, '2022-1', '2025-10-17 16:16:42'),
	(4, 10, 26, '2022-1', '2025-10-17 16:16:42'),
	(5, 11, 28, '2022-2', '2025-10-18 02:52:57'),
	(6, 11, 29, '2022-2', '2025-10-18 02:52:57'),
	(7, 11, 30, '2022-2', '2025-10-18 02:52:57');

-- Dumping structure for table student_db.roles
CREATE TABLE IF NOT EXISTS `roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table student_db.roles: ~3 rows (approximately)
INSERT INTO `roles` (`id`, `name`) VALUES
	(2, 'ROLE_GIẢNG_VIÊN'),
	(1, 'ROLE_HIỆU_TRƯỞNG'),
	(3, 'ROLE_SINH_VIÊN');

-- Dumping structure for table student_db.semesters
CREATE TABLE IF NOT EXISTS `semesters` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `semester` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `semester` (`semester`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table student_db.semesters: ~8 rows (approximately)
INSERT INTO `semesters` (`id`, `semester`) VALUES
	(1, '2022-1'),
	(2, '2022-2'),
	(3, '2023-1'),
	(4, '2023-2'),
	(5, '2024-1'),
	(6, '2024-2'),
	(7, '2025-1'),
	(8, '2025-2');

-- Dumping structure for table student_db.students
CREATE TABLE IF NOT EXISTS `students` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `student_code` varchar(255) NOT NULL,
  `class_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `student_code` (`student_code`),
  UNIQUE KEY `user_id` (`user_id`),
  KEY `class_id` (`class_id`),
  CONSTRAINT `students_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `students_ibfk_2` FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table student_db.students: ~28 rows (approximately)
INSERT INTO `students` (`id`, `user_id`, `student_code`, `class_id`) VALUES
	(1, 22, 'SV2024001', 1),
	(2, 23, 'SV2024002', 1),
	(3, 24, 'SV2024003', 1),
	(4, 25, 'SV2024004', 1),
	(5, 26, 'SV2024005', 1),
	(6, 27, 'SV2024006', 1),
	(7, 28, 'SV2024007', 1),
	(8, 29, 'SV2024008', 2),
	(9, 30, 'SV2024009', 2),
	(10, 31, 'SV2024010', 2),
	(11, 32, 'SV2024011', 2),
	(12, 33, 'SV2024012', 2),
	(13, 34, 'SV2023001', 2),
	(14, 35, 'SV2023002', 2),
	(15, 36, 'SV2023003', 3),
	(16, 37, 'SV2023004', 3),
	(17, 38, 'SV2022001', 3),
	(18, 39, 'SV2022002', 3),
	(19, 40, 'SV2022003', 3),
	(20, 41, 'SV2022004', 3),
	(21, 42, 'SV2021001', 3),
	(22, 43, 'SV2021002', 4),
	(23, 44, 'SV2021003', 4),
	(24, 45, 'SV2021004', 4),
	(25, 46, 'SV2020001', 4),
	(26, 47, 'SV2020002', 4),
	(27, 48, 'SV2020003', 4),
	(28, 49, 'SV2020004', 4);

-- Dumping structure for table student_db.student_schedule
CREATE TABLE IF NOT EXISTS `student_schedule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `student_id` bigint(20) NOT NULL,
  `enrollment_id` bigint(20) NOT NULL,
  `teaching_id` bigint(20) NOT NULL,
  `semester` varchar(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_enrollment` (`student_id`,`enrollment_id`),
  KEY `enrollment_id` (`enrollment_id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_semester` (`semester`),
  KEY `idx_student_semester` (`student_id`,`semester`),
  KEY `idx_teaching_id` (`teaching_id`),
  CONSTRAINT `student_schedule_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE,
  CONSTRAINT `student_schedule_ibfk_2` FOREIGN KEY (`enrollment_id`) REFERENCES `enrollments` (`id`) ON DELETE CASCADE,
  CONSTRAINT `student_schedule_ibfk_3` FOREIGN KEY (`teaching_id`) REFERENCES `teachings` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table student_db.student_schedule: ~3 rows (approximately)
INSERT INTO `student_schedule` (`id`, `student_id`, `enrollment_id`, `teaching_id`, `semester`, `created_at`) VALUES
	(4, 1, 24, 1, '2022-1', '2025-10-18 02:51:13'),
	(5, 1, 25, 2, '2022-1', '2025-10-18 02:51:13'),
	(6, 1, 26, 3, '2022-1', '2025-10-18 02:51:13');

-- Dumping structure for table student_db.teachings
CREATE TABLE IF NOT EXISTS `teachings` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lecturer_id` bigint(20) DEFAULT NULL,
  `course_id` bigint(20) DEFAULT NULL,
  `day_of_week` varchar(255) DEFAULT NULL,
  `period` varchar(255) DEFAULT NULL,
  `classroom` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `lecturer_id` (`lecturer_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `teachings_ibfk_1` FOREIGN KEY (`lecturer_id`) REFERENCES `lecturers` (`id`),
  CONSTRAINT `teachings_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table student_db.teachings: ~24 rows (approximately)
INSERT INTO `teachings` (`id`, `lecturer_id`, `course_id`, `day_of_week`, `period`, `classroom`) VALUES
	(1, 3, 22, 'Monday', '2-5', 'Room 13'),
	(2, 4, 23, 'Monday', '6-10', 'Room 13'),
	(3, 1, 24, 'Thursday', '1-3', 'Room 2'),
	(4, 4, 19, 'Saturday', '1-4', 'Room 3'),
	(5, 1, 20, 'Saturday', '7-10', 'Room 15'),
	(6, 2, 21, 'Thursday', '2-5', 'Room 5'),
	(7, 1, 16, 'Thursday', '6-10', 'Room 2'),
	(8, 2, 17, 'Monday', '1-3', 'Room 9'),
	(9, 3, 18, 'Saturday', '7-10', 'Room 20'),
	(10, 2, 13, 'Friday', '7-10', 'Room 15'),
	(11, 3, 14, 'Saturday', '6-10', 'Room 19'),
	(12, 4, 15, 'Tuesday', '1-3', 'Room 7'),
	(13, 3, 10, 'Saturday', '6-10', 'Room 4'),
	(14, 4, 11, 'Tuesday', '6-9', 'Room 12'),
	(15, 1, 12, 'Wednesday', '6-10', 'Room 20'),
	(16, 4, 7, 'Wednesday', '1-3', 'Room 11'),
	(17, 1, 8, 'Thursday', '7-10', 'Room 3'),
	(18, 2, 9, 'Thursday', '7-10', 'Room 13'),
	(19, 1, 4, 'Friday', '1-3', 'Room 12'),
	(20, 2, 5, 'Monday', '6-10', 'Room 16'),
	(21, 3, 6, 'Saturday', '6-10', 'Room 17'),
	(22, 2, 1, 'Tuesday', '2-5', 'Room 11'),
	(23, 3, 2, 'Monday', '6-10', 'Room 19'),
	(24, 4, 3, 'Wednesday', '6-9', 'Room 15');

-- Dumping structure for table student_db.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `department_id` bigint(20) DEFAULT NULL,
  `gender` enum('FEMALE','MALE','OTHER') DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `date_of_birth` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `role_id` (`role_id`),
  KEY `FK_users_departments` (`department_id`),
  CONSTRAINT `FK_users_departments` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=237 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table student_db.users: ~49 rows (approximately)
INSERT INTO `users` (`id`, `username`, `password`, `full_name`, `email`, `role_id`, `department_id`, `gender`, `phone`, `date_of_birth`, `address`) VALUES
	(1, 'hieutruong', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Nguyễn Văn Hiệu Trưởng', 'hieuthruong@university.edu.vn', 1, 1, 'MALE', '0901234567', '1970-01-15', 'Hà Nội'),
	(2, 'gv001', '$2a$10$lcgW3rROpZ0MG4zFpQSNZ.SVp8Y6hFScHBGOVSAVi39pbqeRpRzKi', 'Phạm Văn Đức', 'pvm@university.edu.vn', 2, 1, 'MALE', '0901234569', '1980-05-10', 'Hà Nội'),
	(3, 'gv002', '$2a$10$fNy24Rp2DY7gW7X7ZPSVQuAH7E4IWa3gzqrcqciun/3iyEp4CJ0K6', 'Lê Thị Hoa', 'lth@university.edu.vn', 2, 1, 'FEMALE', '0901234570', '1982-07-25', 'Hà Nội'),
	(4, 'gv003', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Hoàng Văn Đức', 'hvd@university.edu.vn', 2, 1, 'MALE', '0901234571', '1978-12-03', 'Hà Nội'),
	(5, 'gv004', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Nguyễn Thị Lan', 'ntl@university.edu.vn', 2, 1, 'FEMALE', '0901234572', '1983-09-18', 'Hà Nội'),
	(6, 'gv005', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Trần Văn Bình', 'tvb@university.edu.vn', 2, 1, 'MALE', '0901234573', '1979-04-12', 'Hà Nội'),
	(7, 'gv006', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Vũ Thị Mai', 'vtm@university.edu.vn', 2, 1, 'FEMALE', '0901234574', '1981-11-30', 'Hà Nội'),
	(8, 'gv007', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Đặng Văn Tùng', 'dvt@university.edu.vn', 2, 1, 'MALE', '0901234575', '1977-08-22', 'Hà Nội'),
	(9, 'gv008', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Bùi Thị Ngọc', 'btn@university.edu.vn', 2, 1, 'FEMALE', '0901234576', '1984-02-14', 'Hà Nội'),
	(10, 'gv009', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Lý Văn Hùng', 'lvh@university.edu.vn', 2, 1, 'MALE', '0901234577', '1976-06-08', 'Hà Nội'),
	(11, 'gv010', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Phan Thị Thu', 'ptt@university.edu.vn', 2, 1, 'FEMALE', '0901234578', '1985-10-05', 'Hà Nội'),
	(12, 'gv011', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Đinh Văn Long', 'dvl@university.edu.vn', 2, 2, 'MALE', '0901234579', '1980-01-28', 'Hà Nội'),
	(13, 'gv012', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Cao Thị Yến', 'cty@university.edu.vn', 2, 2, 'FEMALE', '0901234580', '1982-12-17', 'Hà Nội'),
	(14, 'gv013', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Dương Văn Khoa', 'dvk@university.edu.vn', 2, 2, 'MALE', '0901234581', '1978-03-11', 'Hà Nội'),
	(15, 'gv014', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Tạ Thị Linh', 'ttl@university.edu.vn', 2, 2, 'FEMALE', '0901234582', '1983-07-04', 'Hà Nội'),
	(16, 'gv015', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Võ Văn Thành', 'vvt@university.edu.vn', 2, 2, 'MALE', '0901234583', '1979-11-19', 'Hà Nội'),
	(17, 'gv016', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Lưu Thị Hương', 'lth2@university.edu.vn', 2, 2, 'FEMALE', '0901234584', '1981-05-26', 'Hà Nội'),
	(18, 'gv017', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Mạc Văn Đạt', 'mvd@university.edu.vn', 2, 2, 'MALE', '0901234585', '1977-09-13', 'Hà Nội'),
	(19, 'gv018', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Kiều Thị Oanh', 'kto@university.edu.vn', 2, 2, 'FEMALE', '0901234586', '1984-04-07', 'Hà Nội'),
	(20, 'gv019', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Hồ Văn Phúc', 'hvp@university.edu.vn', 2, 2, 'MALE', '0901234587', '1976-08-31', 'Hà Nội'),
	(21, 'gv020', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Đỗ Thị Vân', 'dtv@university.edu.vn', 2, 2, 'FEMALE', '0901234588', '1985-12-24', 'Hà Nội'),
	(22, 'sv001', '$2a$10$qnowUGTVpvKLUsBoLsfFt.8irh4MwXc1Wo2xg/eQYMU0Zw4Qz/iJq', 'Nguyễn Văn An', 'nva@student.edu.vn', 3, 1, 'MALE', '0987654321', '2003-01-15', 'Hà Nội'),
	(23, 'sv002', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Trần Thị Bình', 'ttb@student.edu.vn', 3, 1, 'FEMALE', '0987654322', '2003-02-20', 'Hà Nội'),
	(24, 'sv003', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Lê Văn Cường', 'lvc@student.edu.vn', 3, 1, 'MALE', '0987654323', '2003-03-25', 'Hà Nội'),
	(25, 'sv004', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Phạm Thị Dung', 'ptd@student.edu.vn', 3, 1, 'FEMALE', '0987654324', '2003-04-30', 'Hà Nội'),
	(26, 'sv005', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Hoàng Văn Em', 'hve@student.edu.vn', 3, 1, 'MALE', '0987654325', '2003-05-05', 'Hà Nội'),
	(27, 'sv006', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Vũ Thị Phương', 'vtp@student.edu.vn', 3, 1, 'FEMALE', '0987654326', '2003-06-10', 'Hà Nội'),
	(28, 'sv007', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Đặng Văn Giang', 'dvg@student.edu.vn', 3, 1, 'MALE', '0987654327', '2003-07-15', 'Hà Nội'),
	(29, 'sv008', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Bùi Thị Hạnh', 'bth@student.edu.vn', 3, 1, 'FEMALE', '0987654328', '2003-08-20', 'Hà Nội'),
	(30, 'sv009', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Lý Văn Ích', 'lvi@student.edu.vn', 3, 1, 'MALE', '0987654329', '2003-09-25', 'Hà Nội'),
	(31, 'sv010', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Phan Thị Kim', 'ptk@student.edu.vn', 3, 1, 'FEMALE', '0987654330', '2003-10-30', 'Hà Nội'),
	(32, 'sv011', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Đinh Văn Long', 'dvl2@student.edu.vn', 3, 1, 'MALE', '0987654331', '2003-11-05', 'Hà Nội'),
	(33, 'sv012', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Cao Thị Mai', 'ctm@student.edu.vn', 3, 1, 'FEMALE', '0987654332', '2003-12-10', 'Hà Nội'),
	(34, 'sv013', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Dương Văn Nam', 'dvn@student.edu.vn', 3, 1, 'MALE', '0987654333', '2002-01-15', 'Hà Nội'),
	(35, 'sv014', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Tạ Thị Oanh', 'tto@student.edu.vn', 3, 1, 'FEMALE', '0987654334', '2002-02-20', 'Hà Nội'),
	(36, 'sv015', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Võ Văn Phúc', 'vvp@student.edu.vn', 3, 2, 'MALE', '0987654335', '2002-03-25', 'Hà Nội'),
	(37, 'sv016', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Lưu Thị Quỳnh', 'ltq@student.edu.vn', 3, 2, 'FEMALE', '0987654336', '2002-04-30', 'Hà Nội'),
	(38, 'sv017', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Mạc Văn Rồng', 'mvr@student.edu.vn', 3, 2, 'MALE', '0987654337', '2002-05-05', 'Hà Nội'),
	(39, 'sv018', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Kiều Thị Sương', 'kts@student.edu.vn', 3, 2, 'FEMALE', '0987654338', '2002-06-10', 'Hà Nội'),
	(40, 'sv019', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Hồ Văn Tài', 'hvt@student.edu.vn', 3, 2, 'MALE', '0987654339', '2002-07-15', 'Hà Nội'),
	(41, 'sv020', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Đỗ Thị Uyên', 'dtu@student.edu.vn', 3, 2, 'FEMALE', '0987654340', '2002-08-20', 'Hà Nội'),
	(42, 'sv021', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Ngô Văn Việt', 'nvv@student.edu.vn', 3, 2, 'MALE', '0987654341', '2002-09-25', 'Hà Nội'),
	(43, 'sv022', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Trịnh Thị Xuân', 'ttx@student.edu.vn', 3, 2, 'FEMALE', '0987654342', '2002-10-30', 'Hà Nội'),
	(44, 'sv023', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Lâm Văn Yên', 'lvy@student.edu.vn', 3, 2, 'MALE', '0987654343', '2002-11-05', 'Hà Nội'),
	(45, 'sv024', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Chu Thị Zoan', 'ctz@student.edu.vn', 3, 2, 'FEMALE', '0987654344', '2002-12-10', 'Hà Nội'),
	(46, 'sv025', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Hà Văn Bách', 'hvb@student.edu.vn', 3, 2, 'MALE', '0987654345', '2001-01-15', 'Hà Nội'),
	(47, 'sv026', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Lại Thị Cẩm', 'ltc@student.edu.vn', 3, 2, 'FEMALE', '0987654346', '2001-02-20', 'Hà Nội'),
	(48, 'sv027', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Thái Văn Đông', 'tvd@student.edu.vn', 3, 2, 'MALE', '0987654347', '2001-03-25', 'Hà Nội'),
	(49, 'sv028', '$2a$12$EjCIXmEd3U9m6BBfu6O2i.YFLiDRcgBZg4VYfvh.ACWz7vPBR7n1m', 'Ôn Thị Nga', 'otn@student.edu.vn', 3, 2, 'FEMALE', '0987654348', '2001-04-30', 'Hà Nội');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
