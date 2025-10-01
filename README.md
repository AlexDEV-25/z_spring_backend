# Student Management System - Backend API

## Tổng quan
Hệ thống quản lý sinh viên được xây dựng bằng Spring Boot với các tính năng:
- Authentication & Authorization với JWT
- Quản lý sinh viên, giảng viên, lớp học, môn học
- Đăng ký môn học và chấm điểm
- Phân quyền theo vai trò (Hiệu trưởng, Giảng viên, Sinh viên)

## Công nghệ sử dụng
- **Spring Boot 3.5.6** với Java 21
- **Spring Security** với JWT Authentication
- **Spring Data JPA** với Hibernate
- **MySQL** Database
- **Spring Boot Actuator** cho monitoring
- **Validation** với Jakarta Validation

## Cấu trúc dự án
```
src/main/java/com/example/app/
├── config/          # Cấu hình Security, CORS
├── controller/      # REST Controllers
├── dto/            # Data Transfer Objects
├── exception/       # Custom Exceptions & Global Handler
├── model/          # JPA Entities
├── repository/     # JPA Repositories
├── security/       # JWT & Security Components
└── service/        # Business Logic Services
```

## API Endpoints

### Authentication
- `POST /api/auth/login` - Đăng nhập
- `POST /api/auth/register` - Đăng ký tài khoản

### Students
- `GET /api/students` - Lấy danh sách sinh viên
- `GET /api/students/{id}` - Lấy sinh viên theo ID
- `GET /api/students/code/{studentCode}` - Lấy sinh viên theo mã
- `GET /api/students/class/{classId}` - Lấy sinh viên theo lớp
- `POST /api/students` - Tạo sinh viên mới
- `PUT /api/students/{id}` - Cập nhật sinh viên
- `DELETE /api/students/{id}` - Xóa sinh viên

### Courses
- `GET /api/courses` - Lấy danh sách môn học
- `POST /api/courses` - Tạo môn học mới
- `PUT /api/courses/{id}` - Cập nhật môn học
- `DELETE /api/courses/{id}` - Xóa môn học

### Classes
- `GET /api/classes` - Lấy danh sách lớp học
- `POST /api/classes` - Tạo lớp học mới
- `PUT /api/classes/{id}` - Cập nhật lớp học
- `DELETE /api/classes/{id}` - Xóa lớp học

### Lecturers
- `GET /api/lecturers` - Lấy danh sách giảng viên
- `POST /api/lecturers` - Tạo giảng viên mới
- `PUT /api/lecturers/{id}` - Cập nhật giảng viên
- `DELETE /api/lecturers/{id}` - Xóa giảng viên

### Enrollments
- `GET /api/enrollments` - Lấy danh sách đăng ký
- `POST /api/enrollments` - Đăng ký môn học
- `PUT /api/enrollments/{id}` - Cập nhật điểm
- `DELETE /api/enrollments/{id}` - Hủy đăng ký

### Teachings
- `GET /api/teachings` - Lấy danh sách phân công giảng dạy
- `POST /api/teachings` - Phân công giảng dạy
- `PUT /api/teachings/{id}` - Cập nhật phân công
- `DELETE /api/teachings/{id}` - Xóa phân công

## Phân quyền

### Sinh viên (SINH_VIÊN)
- Xem thông tin cá nhân
- Xem danh sách lớp học
- Đăng ký/hủy môn học
- Xem điểm số

### Giảng viên (GIẢNG_VIÊN)
- Tất cả quyền của sinh viên
- Quản lý môn học
- Phân công giảng dạy
- Chấm điểm sinh viên

### Hiệu trưởng (HIỆU_TRƯỞNG)
- Tất cả quyền của giảng viên
- Quản lý người dùng
- Quản lý lớp học
- Xem báo cáo tổng hợp

## Cấu hình Database

### Tạo database
```sql
CREATE DATABASE student_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Khởi tạo dữ liệu mẫu
```sql
-- Roles
INSERT INTO roles (name) VALUES ('HIỆU_TRƯỞNG'), ('GIẢNG_VIÊN'), ('SINH_VIÊN');

-- Admin user (password: admin123)
INSERT INTO users (username, password, full_name, email, role_id) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Administrator', 'admin@school.edu', 1);
```

## Cấu hình Environment Variables

Tạo file `.env` hoặc set environment variables:
```bash
JWT_SECRET=your-super-secret-jwt-key-here
JWT_EXPIRATION=604800000
SERVER_PORT=8080
```

## Chạy ứng dụng

### Yêu cầu
- Java 21+
- MySQL 8.0+
- Maven 3.6+

### Cài đặt và chạy
```bash
# Clone repository
git clone <repository-url>
cd z_btl

# Cấu hình database trong application.properties

# Chạy ứng dụng
./mvnw spring-boot:run

# Hoặc build và chạy JAR
./mvnw clean package
java -jar target/z_btl-0.0.1-SNAPSHOT.jar
```

## Monitoring

Ứng dụng có tích hợp Spring Boot Actuator:
- Health check: `GET /actuator/health`
- Metrics: `GET /actuator/metrics`
- Info: `GET /actuator/info`

## Validation Rules

### User
- Username: 3-20 ký tự, không được trống
- Password: ít nhất 6 ký tự
- Email: định dạng email hợp lệ
- Full name: tối đa 100 ký tự

### Student
- Student code: 6-10 ký tự chữ hoa và số
- Date of birth: định dạng YYYY-MM-DD
- Address: tối đa 255 ký tự

### Course
- Course code: 4-8 ký tự chữ hoa và số
- Name: tối đa 100 ký tự
- Credit: 1-6 tín chỉ

### Class
- Name: không được trống
- Year: 2020-2030

### Enrollment/Teaching
- Semester: định dạng YYYY-X (ví dụ: 2024-1)
- Grade: A+, A, B+, B, C+, C, D+, D, F hoặc số thập phân

## Error Handling

Hệ thống có Global Exception Handler xử lý:
- `ResourceNotFoundException` (404)
- `DuplicateResourceException` (409)
- `UnauthorizedException` (403)
- `ValidationException` (400)
- `BadCredentialsException` (401)

## Logging

Logging được cấu hình với các level:
- Application: DEBUG
- Security: DEBUG
- Hibernate SQL: DEBUG
- Hibernate Parameters: TRACE

## Testing

```bash
# Chạy tests
./mvnw test

# Chạy tests với coverage
./mvnw test jacoco:report
```

## API Documentation

Sau khi chạy ứng dụng, có thể truy cập:
- Swagger UI: `http://localhost:8080/swagger-ui.html` (nếu có tích hợp)
- Actuator endpoints: `http://localhost:8080/actuator`

## Troubleshooting

### Lỗi thường gặp
1. **Database connection failed**: Kiểm tra MySQL service và connection string
2. **JWT token invalid**: Kiểm tra JWT secret và expiration time
3. **CORS error**: Kiểm tra CORS configuration trong SecurityConfig
4. **Validation error**: Kiểm tra dữ liệu input theo validation rules

### Debug
- Bật debug logging trong application.properties
- Sử dụng Spring Boot Actuator để monitor
- Kiểm tra logs trong console hoặc log files

