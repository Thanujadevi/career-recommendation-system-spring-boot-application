package com.example.career_recommendation.service;

import com.example.career_recommendation.model.RecommendationResult;
import java.util.List;

/**
 * RecommendationService — interface (contract) for the recommendation feature.
 *
 * Using an interface + implementation class (RecommendationServiceImpl) is
 * the recommended Spring pattern:
 *   - Decouples controller from implementation
 *   - Allows easy mocking in unit tests
 *   - Demonstrates the Dependency Inversion Principle (DIP)
 */
public interface RecommendationService {

    /**
     * Generate and persist ranked career recommendations for the given student.
     *
     * @param studentId The ID of the student
     * @return List of RecommendationResult, sorted by rank (1 = best match)
     */
    List<RecommendationResult> generate(Long studentId);
}
