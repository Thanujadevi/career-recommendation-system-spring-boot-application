package com.example.career_recommendation.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AptitudeTest — defines a test (metadata only, no questions stored here).
 */
@Entity
@Table(name = "aptitude_test")
@Data
@EqualsAndHashCode(callSuper = false)
public class AptitudeTest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private Long testId;

    @Column(name = "test_name")
    private String testName;

    /** LOGICAL, VERBAL, QUANTITATIVE, MIXED */
    @Column(name = "test_type")
    private String testType;

    @Column(name = "total_questions")
    private int totalQuestions;

    @Column(name = "total_marks")
    private int totalMarks;

    /** Duration in minutes */
    private int duration;

    /** LOW, MEDIUM, HIGH */
    @Column(name = "difficulty_level")
    private String difficultyLevel;

    /** ACTIVE or INACTIVE */
    private String status;
}
