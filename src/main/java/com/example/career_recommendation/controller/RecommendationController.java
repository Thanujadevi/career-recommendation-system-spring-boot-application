package com.example.career_recommendation.controller;

import com.example.career_recommendation.model.RecommendationResult;
import com.example.career_recommendation.repository.RecommendationResultRepository;
import com.example.career_recommendation.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * RecommendationController — REST API for generating and retrieving recommendations.
 *
 * POST /api/recommendations/generate/{studentId}  — generate fresh recommendations
 * GET  /api/recommendations/student/{studentId}   — fetch saved recommendations
 */
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService service;
    private final RecommendationResultRepository resultRepo;

    public RecommendationController(RecommendationService service,
                                    RecommendationResultRepository resultRepo) {
        this.service = service;
        this.resultRepo = resultRepo;
    }

    @PostMapping("/generate/{studentId}")
    public ResponseEntity<List<RecommendationResult>> generate(@PathVariable Long studentId) {
        try {
            return ResponseEntity.ok(service.generate(studentId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/student/{studentId}")
    public List<RecommendationResult> getByStudent(@PathVariable Long studentId) {
        return resultRepo.findByStudent_StudentIdOrderByRankAsc(studentId);
    }
}
