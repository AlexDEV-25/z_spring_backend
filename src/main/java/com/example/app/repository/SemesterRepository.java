package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.app.model.Semester;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
    boolean existsBySemester(String semester);
}
