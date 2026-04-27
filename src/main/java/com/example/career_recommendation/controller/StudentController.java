package com.example.career_recommendation.controller;

import com.example.career_recommendation.model.Student;
import com.example.career_recommendation.repository.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * StudentController — RESTful API for Student CRUD.
 * Endpoints:  POST /api/students       — create
 *             GET  /api/students       — list all
 *             GET  /api/students/{id}  — get by id
 *             PUT  /api/students/{id}  — update
 *             DELETE /api/students/{id}— delete
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository repo;

    public StudentController(StudentRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Student create(@RequestBody Student s) {
        return repo.save(s);
    }

    @GetMapping
    public List<Student> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable Long id,
                                          @RequestBody Student updated) {
        return repo.findById(id).map(s -> {
            s.setFullName(updated.getFullName());
            s.setDepartment(updated.getDepartment());
            s.setCgpa(updated.getCgpa());
            s.setGender(updated.getGender());
            s.setYearOfStudy(updated.getYearOfStudy());
            s.setCollegeName(updated.getCollegeName());
            return ResponseEntity.ok(repo.save(s));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.ok("Student deleted successfully");
    }
}
