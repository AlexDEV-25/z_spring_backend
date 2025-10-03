package com.example.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.app.model.Teaching;

@RepositoryRestResource(path = "teachings")
public interface TeachingRepository extends JpaRepository<Teaching, Long> {
	@Modifying
	@Query("DELETE FROM Teaching t WHERE t.courseId = :courseId")
	void deleteByCourseId(@Param("courseId") Long courseId);

	List<Teaching> findByLecturerId(Long lecturerId);

}