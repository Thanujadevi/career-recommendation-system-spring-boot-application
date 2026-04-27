package com.example.career_recommendation.repository;

import com.example.career_recommendation.model.RecommendationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RecommendationResultRepository extends JpaRepository<RecommendationResult, Long> {
    List<RecommendationResult> findByStudent_StudentIdOrderByRankAsc(Long studentId);

    /** FIX: Used to clear old results before regenerating */
    @Modifying
    @Query("DELETE FROM RecommendationResult r WHERE r.student.studentId = :studentId")
    void deleteByStudentId(Long studentId);
}
