package com.example.career_recommendation.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Interest — stores a student's interests and preferred career domains.
 */
@Entity
@Table(name = "interest")
@Data
@EqualsAndHashCode(callSuper = false)
public class Interest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id")
    private Long interestId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "interest_area")
    private String interestArea;

    /** LOW, MEDIUM, HIGH */
    @Column(name = "interest_level")
    private String interestLevel;

    @Column(name = "preferred_domain")
    private String preferredDomain;
}
