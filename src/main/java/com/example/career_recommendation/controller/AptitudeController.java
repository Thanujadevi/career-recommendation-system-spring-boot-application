package com.example.career_recommendation.controller;

import com.example.career_recommendation.model.AptitudeResult;
import com.example.career_recommendation.repository.AptitudeResultRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * AptitudeController — REST API for managing aptitude test results.
 */
@RestController
@RequestMapping("/api/aptitude")
public class AptitudeController {

    private final AptitudeResultRepository repo;

    public AptitudeController(AptitudeResultRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public AptitudeResult create(@RequestBody AptitudeResult a) {
        return repo.save(a);
    }

    @GetMapping
    public List<AptitudeResult> getAll() {
        return repo.findAll();
    }

    @GetMapping("/student/{studentId}")
    public List<AptitudeResult> getByStudent(@PathVariable Long studentId) {
        return repo.findByStudent_StudentId(studentId);
    }
}
