package com.example.career_recommendation.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * StudentSkill — maps a Student to a Skill.
 *
 * FIX: proficiencyScore is now AUTO-DERIVED from skillLevel.
 * Students should not manually enter a "proficiency score out of 100"
 * — that is meaningless from a UX perspective.
 * Instead they pick a level (BEGINNER/INTERMEDIATE/ADVANCED/EXPERT)
 * and we map it to a score internally.
 *
 * Mapping:
 *   BEGINNER     → 30
 *   INTERMEDIATE → 60
 *   ADVANCED     → 80
 *   EXPERT       → 95
 */
@Entity
@Table(name = "student_skill")
@Data
@EqualsAndHashCode(callSuper = false)
public class StudentSkill extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_skill_id")
    private Long studentSkillId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    /** BEGINNER, INTERMEDIATE, ADVANCED, EXPERT */
    @Column(name = "skill_level")
    private String skillLevel;

    @Column(name = "experience_years")
    private int experienceYears;

    /** Optional certification name */
    private String certification;

    /**
     * Auto-derived from skillLevel — not entered manually by user.
     * Set by service layer before saving.
     */
    @Column(name = "proficiency_score")
    private double proficiencyScore;

    /** Derive proficiency score from skill level */
    public static double deriveScore(String skillLevel) {
        if (skillLevel == null) return 30;
        return switch (skillLevel.toUpperCase()) {
            case "EXPERT"        -> 95;
            case "ADVANCED"      -> 80;
            case "INTERMEDIATE"  -> 60;
            default              -> 30;  // BEGINNER
        };
    }
}
