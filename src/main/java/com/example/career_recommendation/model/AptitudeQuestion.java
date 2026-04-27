package com.example.career_recommendation.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AptitudeQuestion — a single MCQ question belonging to an AptitudeTest.
 *
 * Each question has 4 options (A-D) and one correct answer.
 * The section determines which sub-score it contributes to:
 *   LOGICAL, VERBAL, or QUANTITATIVE
 */
@Entity
@Table(name = "aptitude_question")
@Data
@EqualsAndHashCode(callSuper = false)
public class AptitudeQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private AptitudeTest test;

    @Column(name = "question_text", nullable = false, length = 1000)
    private String questionText;

    @Column(name = "option_a", nullable = false)
    private String optionA;

    @Column(name = "option_b", nullable = false)
    private String optionB;

    @Column(name = "option_c", nullable = false)
    private String optionC;

    @Column(name = "option_d", nullable = false)
    private String optionD;

    /** Correct answer: A, B, C, or D */
    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer;

    /** Marks awarded for correct answer */
    @Column(name = "marks")
    private int marks = 1;

    /** LOGICAL, VERBAL, QUANTITATIVE */
    @Column(name = "section")
    private String section;
}
