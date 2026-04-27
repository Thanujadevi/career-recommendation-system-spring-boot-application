package com.example.career_recommendation.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Admin — represents a system administrator account.
 * Linked 1:1 with User for login.
 */
@Entity
@Table(name = "admin")
@Data
@EqualsAndHashCode(callSuper = false)
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "admin_name")
    private String adminName;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String designation;

    /** FULL, READ_ONLY, etc. */
    @Column(name = "access_level")
    private String accessLevel;

    /** ACTIVE or INACTIVE */
    private String status;
}
