package com.example.career_recommendation.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

/**
 * BaseEntity — superclass for all entities.
 * Provides audit fields: createdDate and updatedDate.
 * Uses @MappedSuperclass so fields are included in each child table,
 * but BaseEntity itself has no table.
 */
@MappedSuperclass
@Data
public abstract class BaseEntity {

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate = LocalDateTime.now();
}
