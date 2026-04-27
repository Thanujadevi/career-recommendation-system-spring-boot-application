package com.example.career_recommendation.repository;

import com.example.career_recommendation.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findBySkillCategory(String category);
    // FIX: field renamed to "active" — method is now findByActiveTrue()
    List<Skill> findByActiveTrue();
}
