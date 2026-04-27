package com.example.career_recommendation.repository;

import com.example.career_recommendation.model.AptitudeTest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AptitudeTestRepository extends JpaRepository<AptitudeTest, Long> {
    List<AptitudeTest> findByStatus(String status);
}
