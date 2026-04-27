package com.example.career_recommendation.service.impl;

import com.example.career_recommendation.model.*;
import com.example.career_recommendation.repository.*;
import com.example.career_recommendation.service.RecommendationService;
import com.example.career_recommendation.util.RecommendationEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RecommendationServiceImpl — generates ranked career recommendations.
 *
 * FIX: deleteByStudentId() removes stale results before saving fresh ones,
 *      preventing duplicate rows on every "Generate" click.
 *
 * Algorithm (all scores normalised 0–100):
 *   academic  = student.cgpa × 10
 *   skill     = average proficiency score across StudentSkills
 *   interest  = HIGH→100, MEDIUM→70, LOW→50 — averaged across interests
 *   aptitude  = average test percentage across AptitudeResults
 *
 *   For each Career:
 *     total = academic*aw + skill*sw + interest*iw + aptitude*aptw
 *     If total >= rule.minimumScore → include in results
 *
 *   Results sorted descending by score, rank assigned (1 = best).
 */
@Service
@Transactional
public class RecommendationServiceImpl implements RecommendationService {

    private static final Logger log = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private final StudentRepository       studentRepo;
    private final CareerRepository        careerRepo;
    private final RecommendationRuleRepository ruleRepo;
    private final RecommendationResultRepository resultRepo;
    private final StudentSkillRepository  studentSkillRepo;
    private final InterestRepository      interestRepo;
    private final AptitudeResultRepository aptitudeRepo;
    private final RecommendationEngine    engine;

    public RecommendationServiceImpl(
            StudentRepository studentRepo,
            CareerRepository careerRepo,
            RecommendationRuleRepository ruleRepo,
            RecommendationResultRepository resultRepo,
            StudentSkillRepository studentSkillRepo,
            InterestRepository interestRepo,
            AptitudeResultRepository aptitudeRepo,
            RecommendationEngine engine) {
        this.studentRepo      = studentRepo;
        this.careerRepo       = careerRepo;
        this.ruleRepo         = ruleRepo;
        this.resultRepo       = resultRepo;
        this.studentSkillRepo = studentSkillRepo;
        this.interestRepo     = interestRepo;
        this.aptitudeRepo     = aptitudeRepo;
        this.engine           = engine;
    }

    @Override
    public List<RecommendationResult> generate(Long studentId) {

        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

        // FIX: Delete any existing recommendations first to prevent duplicates
        resultRepo.deleteByStudentId(studentId);

        double academicScore = student.getCgpa() * 10;                    // 0–100
        double skillScore    = calculateSkillScore(studentId);            // 0–100
        double interestScore = calculateInterestScore(studentId);         // 0–100
        double aptitudeScore = calculateAptitudeScore(studentId);         // 0–100

        log.info("Scores for student {}: academic={}, skill={}, interest={}, aptitude={}",
                studentId, academicScore, skillScore, interestScore, aptitudeScore);

        Map<Career, Double> scoreMap = engine.calculateScores(
                student,
                careerRepo.findAll(),
                ruleRepo.findAll(),
                academicScore, skillScore, interestScore, aptitudeScore);

        if (scoreMap.isEmpty()) {
            log.warn("No careers matched threshold for student {}. " +
                     "Ensure the student has skills, interests, and aptitude results.", studentId);
        }

        // Sort descending, assign ranks, persist
        List<RecommendationResult> results = new ArrayList<>();
        scoreMap.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .forEach(entry -> {
                    RecommendationResult r = new RecommendationResult();
                    r.setStudent(student);
                    r.setCareer(entry.getKey());
                    r.setTotalScore(Math.round(entry.getValue() * 100.0) / 100.0);
                    results.add(r);
                });

        for (int i = 0; i < results.size(); i++) {
            results.get(i).setRank(i + 1);
        }

        return resultRepo.saveAll(results);
    }

    // ── score helpers ────────────────────────────────────────────────────────

    /** Average proficiency score (0–100) across all student skills. */
    private double calculateSkillScore(Long studentId) {
        List<StudentSkill> skills = studentSkillRepo.findByStudent_StudentId(studentId);
        if (skills.isEmpty()) return 0;
        return skills.stream()
                .mapToDouble(StudentSkill::getProficiencyScore)
                .average()
                .orElse(0);
    }

    /**
     * Average interest score mapped from level strings.
     * HIGH → 100, MEDIUM → 70, LOW → 50
     */
    private double calculateInterestScore(Long studentId) {
        List<Interest> interests = interestRepo.findByStudent_StudentId(studentId);
        if (interests.isEmpty()) return 0;
        return interests.stream()
                .mapToDouble(i -> switch (i.getInterestLevel().toUpperCase()) {
                    case "HIGH"   -> 100.0;
                    case "MEDIUM" -> 70.0;
                    default       -> 50.0;
                })
                .average()
                .orElse(0);
    }

    /** Average aptitude percentage across all test results. */
    private double calculateAptitudeScore(Long studentId) {
        List<AptitudeResult> results = aptitudeRepo.findByStudent_StudentId(studentId);
        if (results.isEmpty()) return 0;
        return results.stream()
                .mapToDouble(AptitudeResult::getPercentage)
                .average()
                .orElse(0);
    }
}
