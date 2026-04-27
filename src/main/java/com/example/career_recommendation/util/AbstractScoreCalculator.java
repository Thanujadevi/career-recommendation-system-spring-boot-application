package com.example.career_recommendation.util;

import com.example.career_recommendation.model.Student;

/**
 * AbstractScoreCalculator — abstract base class demonstrating the
 * Template Method design pattern.
 *
 * Subclasses implement calculate() to provide domain-specific scoring logic.
 * This makes it easy to plug in new scoring strategies without touching
 * existing code (Open/Closed Principle).
 */
public abstract class AbstractScoreCalculator {

    /**
     * Calculate a score (0–100) for the given student.
     * Implemented by concrete subclasses.
     */
    public abstract double calculate(Student student);

    /**
     * Normalize a raw score to a 0–100 range.
     * Used by subclasses to standardize scores before returning.
     */
    protected double normalize(double value, double min, double max) {
        if (max == min) return 0;
        return ((value - min) / (max - min)) * 100;
    }
}
