package com.example.career_recommendation.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Career — represents a career path that can be recommended.
 * FIX: Now extends BaseEntity (was missing, inconsistent with other entities).
 * FIX: Removed fragile lowercase @Column name overrides; let Hibernate snake_case naming handle it.
 */
@Entity
@Table(name = "career")
@Data
@EqualsAndHashCode(callSuper = false)
public class Career extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_id")
    private Long careerId;

    @Column(name = "career_name", nullable = false)
    private String careerName;

    private String domain;

    @Column(length = 500)
    private String description;

    @Column(name = "required_skills", length = 500)
    private String requiredSkills;

    /** Minimum CGPA required for this career */
    @Column(name = "minimum_cgpa")
    private double minimumCGPA;

    /** Average annual salary in INR */
    @Column(name = "average_salary")
    private double averageSalary;
}
