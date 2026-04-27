package com.example.career_recommendation.repository;

import com.example.career_recommendation.model.AcademicProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AcademicProfileRepository extends JpaRepository<AcademicProfile, Long> {
    List<AcademicProfile> findByStudent_StudentId(Long studentId);
    Optional<AcademicProfile> findFirstByStudent_StudentIdOrderByCreatedDateDesc(Long studentId);
}
