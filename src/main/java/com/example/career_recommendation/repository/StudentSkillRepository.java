package com.example.career_recommendation.repository;

import com.example.career_recommendation.model.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentSkillRepository extends JpaRepository<StudentSkill, Long> {
    List<StudentSkill> findByStudent_StudentId(Long studentId);
}
