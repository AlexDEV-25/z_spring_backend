package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.app.model.Course;

import jakarta.transaction.Transactional;

@RepositoryRestResource(path = "courses")
public interface CourseRepository extends JpaRepository<Course, Long> {
	@Modifying
	@Transactional
	@Query("UPDATE Course c SET c.semesterId = null WHERE c.semesterId = :semesterId")
	void setSemesterIdNullBySemesterId(@Param("semesterId") Long semesterId);
}