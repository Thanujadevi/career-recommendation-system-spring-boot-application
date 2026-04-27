package com.example.career_recommendation.controller;

import com.example.career_recommendation.model.Career;
import com.example.career_recommendation.repository.CareerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CareerController — RESTful API for Career CRUD.
 */
@RestController
@RequestMapping("/api/careers")
public class CareerController {

    private final CareerRepository repo;

    public CareerController(CareerRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Career create(@RequestBody Career c) {
        return repo.save(c);
    }

    @GetMapping
    public List<Career> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Career> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Career> update(@PathVariable Long id,
                                          @RequestBody Career updated) {
        return repo.findById(id).map(c -> {
            c.setCareerName(updated.getCareerName());
            c.setDomain(updated.getDomain());
            c.setDescription(updated.getDescription());
            c.setRequiredSkills(updated.getRequiredSkills());
            c.setMinimumCGPA(updated.getMinimumCGPA());
            c.setAverageSalary(updated.getAverageSalary());
            return ResponseEntity.ok(repo.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.ok("Career deleted successfully");
    }
}
