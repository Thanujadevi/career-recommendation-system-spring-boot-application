package com.example.career_recommendation.repository;

import com.example.career_recommendation.model.AptitudeResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AptitudeResultRepository extends JpaRepository<AptitudeResult, Long> {
    List<AptitudeResult> findByStudent_StudentId(Long studentId);
}
