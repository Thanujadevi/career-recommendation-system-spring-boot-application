package com.example.career_recommendation.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AptitudeResult — stores a student's score for a given AptitudeTest.
 */
@Entity
@Table(name = "aptitude_result")
@Data
@EqualsAndHashCode(callSuper = false)
public class AptitudeResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private AptitudeTest test;

    /** Raw score */
    private double score;

    /** Score as percentage (0–100) */
    private double percentage;

    @Column(name = "logical_score")
    private double logicalScore;

    @Column(name = "verbal_score")
    private double verbalScore;

    @Column(name = "quantitative_score")
    private double quantitativeScore;
}
