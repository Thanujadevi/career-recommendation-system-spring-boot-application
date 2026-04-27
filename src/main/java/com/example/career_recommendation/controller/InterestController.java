package com.example.career_recommendation.controller;

import com.example.career_recommendation.model.Interest;
import com.example.career_recommendation.repository.InterestRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * InterestController — REST API for managing student interests.
 */
@RestController
@RequestMapping("/api/interests")
public class InterestController {

    private final InterestRepository repo;

    public InterestController(InterestRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Interest create(@RequestBody Interest i) {
        return repo.save(i);
    }

    @GetMapping
    public List<Interest> getAll() {
        return repo.findAll();
    }

    @GetMapping("/student/{studentId}")
    public List<Interest> getByStudent(@PathVariable Long studentId) {
        return repo.findByStudent_StudentId(studentId);
    }
}
