package com.example.career_recommendation.controller;

import com.example.career_recommendation.model.Skill;
import com.example.career_recommendation.repository.SkillRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SkillController — RESTful API for Skill master data.
 */
@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillRepository repo;

    public SkillController(SkillRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Skill create(@RequestBody Skill s) {
        return repo.save(s);
    }

    @GetMapping
    public List<Skill> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Skill> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.ok("Skill deleted");
    }
}
