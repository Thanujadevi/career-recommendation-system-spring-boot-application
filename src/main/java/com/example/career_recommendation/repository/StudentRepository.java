package com.example.career_recommendation.repository;

import com.example.career_recommendation.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByDepartment(String department);
    List<Student> findByCgpaGreaterThanEqual(double minCgpa);
    /** FIX: needed by UserPortalController to resolve Student from logged-in User */
    Optional<Student> findByUser_UserId(Long userId);
}
