package com.example.career_recommendation.util;

import com.example.career_recommendation.model.Career;
import com.example.career_recommendation.model.RecommendationRule;
import com.example.career_recommendation.model.Student;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RecommendationEngine — core scoring component.
 *
 * Algorithm:
 *   For each Career, look up its RecommendationRule.
 *   Compute: totalScore = (academic * academicWeight)
 *                       + (skill    * skillWeight)
 *                       + (interest * interestWeight)
 *                       + (aptitude * aptitudeWeight)
 *
 *   Careers whose totalScore < minimumScore are excluded from results.
 *
 * Design Pattern used: Strategy Pattern — the weights in RecommendationRule
 * define the scoring strategy per career, allowing different careers to
 * prioritise different dimensions.
 */
@Component
public class RecommendationEngine {

    /**
     * @param student    The student being evaluated
     * @param careers    All available careers
     * @param rules      All recommendation rules (one per career)
     * @param academic   Academic score (0–100)
     * @param skill      Skill score (0–100)
     * @param interest   Interest score (0–100)
     * @param aptitude   Aptitude score (0–100)
     * @return Map of Career → computed totalScore (only those above minimumScore)
     */
    public Map<Career, Double> calculateScores(
            Student student,
            List<Career> careers,
            List<RecommendationRule> rules,
            double academic,
            double skill,
            double interest,
            double aptitude) {

        Map<Career, Double> scoreMap = new HashMap<>();

        for (Career career : careers) {

            // Skip career if student's CGPA is below the minimum required
            if (student.getCgpa() < career.getMinimumCGPA()) {
                continue;
            }

            // Find the rule for this career
            RecommendationRule rule = rules.stream()
                    .filter(r -> r.getCareer().getCareerId().equals(career.getCareerId()))
                    .findFirst()
                    .orElse(null);

            if (rule == null) continue;

            // Weighted score calculation
            double total = (academic  * rule.getAcademicWeight())
                         + (skill     * rule.getSkillWeight())
                         + (interest  * rule.getInterestWeight())
                         + (aptitude  * rule.getAptitudeWeight());

            // Apply minimum score threshold
            if (total >= rule.getMinimumScore()) {
                scoreMap.put(career, total);
            }
        }

        return scoreMap;
    }
}
