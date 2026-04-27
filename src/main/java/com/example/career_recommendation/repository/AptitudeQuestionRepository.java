package com.example.career_recommendation.repository;

import com.example.career_recommendation.model.AptitudeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AptitudeQuestionRepository extends JpaRepository<AptitudeQuestion, Long> {
    List<AptitudeQuestion> findByTest_TestId(Long testId);
    long countByTest_TestId(Long testId);
}
