package com.example.career_recommendation.repository;

import com.example.career_recommendation.model.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CareerRepository extends JpaRepository<Career, Long> {

    List<Career> findByDomain(String domain);

    List<Career> findByMinimumCGPALessThanEqual(double cgpa);
}
