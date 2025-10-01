package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.app.model.ClassEntity;

@RepositoryRestResource(path = "classes")
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
}