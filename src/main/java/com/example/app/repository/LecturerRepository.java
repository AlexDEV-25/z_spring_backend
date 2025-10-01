package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.app.model.Lecturer;

@RepositoryRestResource(path = "lecturers")
public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
	Lecturer findByUserId(Long userId);
}