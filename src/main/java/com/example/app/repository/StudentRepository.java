package com.example.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.app.model.Student;

@RepositoryRestResource(path = "students")
public interface StudentRepository extends JpaRepository<Student, Long> {
	Optional<Student> findByStudentCode(String studentCode);

//	List<Student> findByClassId(Long classId);

	boolean existsByStudentCode(String studentCode);

	Student findByUserId(Long userId);

	void deleteByClassId(Long classId);

}