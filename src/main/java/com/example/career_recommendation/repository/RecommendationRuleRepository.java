package com.example.career_recommendation.repository;

import com.example.career_recommendation.model.RecommendationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RecommendationRuleRepository extends JpaRepository<RecommendationRule, Long> {
    Optional<RecommendationRule> findByCareer_CareerId(Long careerId);
}
