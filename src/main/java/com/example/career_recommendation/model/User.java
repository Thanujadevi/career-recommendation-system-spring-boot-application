package com.example.career_recommendation.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User — stores login credentials and role.
 * Roles: STUDENT, ADMIN
 */
@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    /** STUDENT or ADMIN */
    private String role;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    /** ACTIVE or INACTIVE */
    private String status;

    @Column(name = "security_question")
    private String securityQuestion;

    @Column(name = "security_answer")
    private String securityAnswer;
}
