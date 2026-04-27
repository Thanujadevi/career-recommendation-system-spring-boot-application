package com.example.career_recommendation.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AcademicProfile — detailed academic history of a student.
 */
@Entity
@Table(name = "academic_profile")
@Data
@EqualsAndHashCode(callSuper = false)
public class AcademicProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "academic_id")
    private Long academicId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "tenth_percentage")
    private double tenthPercentage;

    @Column(name = "twelfth_percentage")
    private double twelfthPercentage;

    /** Only if diploma track; 0 otherwise */
    @Column(name = "diploma_percentage")
    private double diplomaPercentage;

    @Column(name = "current_cgpa")
    private double currentCGPA;

    private int backlogs;

    private String specialization;

    @Column(name = "university_name")
    private String universityName;

    /** Computed composite score used in recommendation engine */
    @Column(name = "academic_score")
    private double academicScore;
}
