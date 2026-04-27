package com.example.career_recommendation.controller;

import com.example.career_recommendation.model.StudentSkill;
import com.example.career_recommendation.repository.StudentSkillRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * StudentSkillController — REST API for managing student-skill mappings.
 */
@RestController
@RequestMapping("/api/student-skills")
public class StudentSkillController {

    private final StudentSkillRepository repo;

    public StudentSkillController(StudentSkillRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public StudentSkill create(@RequestBody StudentSkill s) {
        return repo.save(s);
    }

    @GetMapping
    public List<StudentSkill> getAll() {
        return repo.findAll();
    }

    @GetMapping("/student/{studentId}")
    public List<StudentSkill> getByStudent(@PathVariable Long studentId) {
        return repo.findByStudent_StudentId(studentId);
    }
}
