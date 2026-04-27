package com.example.career_recommendation.repository;

import com.example.career_recommendation.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {
    List<Interest> findByStudent_StudentId(Long studentId);
}
