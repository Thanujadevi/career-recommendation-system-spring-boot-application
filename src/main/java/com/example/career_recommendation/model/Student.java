package com.example.career_recommendation.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Student — core entity representing a student profile.
 * Linked 1:1 with User for login purposes.
 */
@Entity
@Table(name = "student")
@Data
@EqualsAndHashCode(callSuper = false)
public class Student extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String gender;

    @Column(nullable = false)
    private String department;

    @Column(name = "year_of_study")
    private int yearOfStudy;

    @Column(name = "college_name")
    private String collegeName;

    /** Current CGPA (0.0 – 10.0) */
    private double cgpa;

    @Column(name = "enrollment_number", unique = true)
    private String enrollmentNumber;

    private String address;
}
