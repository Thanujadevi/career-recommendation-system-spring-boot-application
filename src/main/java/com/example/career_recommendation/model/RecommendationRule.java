package com.example.career_recommendation.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * RecommendationRule — defines scoring weights for each Career.
 * The engine uses these weights to compute a weighted total score.
 *
 * Formula:
 *   totalScore = (academicScore * academicWeight)
 *              + (skillScore   * skillWeight)
 *              + (interestScore* interestWeight)
 *              + (aptitudeScore* aptitudeWeight)
 */
@Entity
@Table(name = "recommendation_rule")
@Data
@EqualsAndHashCode(callSuper = false)
public class RecommendationRule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Long ruleId;

    @ManyToOne
    @JoinColumn(name = "career_id", nullable = false)
    private Career career;

    @Column(name = "academic_weight")
    private double academicWeight;

    @Column(name = "skill_weight")
    private double skillWeight;

    @Column(name = "interest_weight")
    private double interestWeight;

    @Column(name = "aptitude_weight")
    private double aptitudeWeight;

    /** Careers with totalScore below this threshold are excluded */
    @Column(name = "minimum_score")
    private double minimumScore;
}
