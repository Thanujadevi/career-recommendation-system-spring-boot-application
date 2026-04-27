package com.example.career_recommendation.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Skill — master list of all skills available in the system.
 *
 * FIX: Renamed isActive → active to avoid Lombok/JPA boolean getter conflict.
 * Lombok generates isActive() for a boolean field named "active", which is
 * correct. But for "isActive", Lombok generates isIsActive() — confusing JPA.
 */
@Entity
@Table(name = "skill")
@Data
@EqualsAndHashCode(callSuper = false)
public class Skill extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Long skillId;

    @Column(name = "skill_name", nullable = false)
    private String skillName;

    @Column(name = "skill_category")
    private String skillCategory;

    @Column(length = 300)
    private String description;

    /** LOW, MEDIUM, HIGH */
    @Column(name = "difficulty_level")
    private String difficultyLevel;

    @Column(name = "industry_relevance")
    private String industryRelevance;

    /** FIX: renamed from isActive to active — Lombok generates isActive() correctly */
    @Column(name = "is_active")
    private boolean active;
}
