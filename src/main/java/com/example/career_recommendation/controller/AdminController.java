package com.example.career_recommendation.controller;

import com.example.career_recommendation.model.*;
import com.example.career_recommendation.repository.*;
import com.example.career_recommendation.service.RecommendationService;
import com.example.career_recommendation.util.FileHandlingUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final StudentRepository studentRepo;
    private final CareerRepository careerRepo;
    private final SkillRepository skillRepo;
    private final StudentSkillRepository studentSkillRepo;
    private final InterestRepository interestRepo;
    private final AptitudeTestRepository aptitudeTestRepo;
    private final AptitudeQuestionRepository questionRepo;
    private final AptitudeResultRepository aptitudeResultRepo;
    private final RecommendationService recommendationService;
    private final RecommendationResultRepository recommendationResultRepo;
    private final UserRepository userRepo;
    private final FileHandlingUtil fileHandlingUtil;

    public AdminController(
            StudentRepository studentRepo, CareerRepository careerRepo,
            SkillRepository skillRepo, StudentSkillRepository studentSkillRepo,
            InterestRepository interestRepo, AptitudeTestRepository aptitudeTestRepo,
            AptitudeQuestionRepository questionRepo, AptitudeResultRepository aptitudeResultRepo,
            RecommendationService recommendationService,
            RecommendationResultRepository recommendationResultRepo,
            UserRepository userRepo, FileHandlingUtil fileHandlingUtil) {
        this.studentRepo = studentRepo; this.careerRepo = careerRepo;
        this.skillRepo = skillRepo; this.studentSkillRepo = studentSkillRepo;
        this.interestRepo = interestRepo; this.aptitudeTestRepo = aptitudeTestRepo;
        this.questionRepo = questionRepo; this.aptitudeResultRepo = aptitudeResultRepo;
        this.recommendationService = recommendationService;
        this.recommendationResultRepo = recommendationResultRepo;
        this.userRepo = userRepo; this.fileHandlingUtil = fileHandlingUtil;
    }

    // ── DASHBOARD ────────────────────────────────────────────────────────────
    @GetMapping({"/dashboard", ""})
    public String dashboard(Model model) {
        model.addAttribute("studentCount", studentRepo.count());
        model.addAttribute("careerCount", careerRepo.count());
        model.addAttribute("skillCount", skillRepo.count());
        model.addAttribute("userCount", userRepo.count());
        model.addAttribute("recommendationCount", recommendationResultRepo.count());
        model.addAttribute("testCount", aptitudeTestRepo.count());
        model.addAttribute("recentStudents", studentRepo.findAll().stream().limit(5).toList());
        return "admin/dashboard";
    }

    // ── STUDENTS ─────────────────────────────────────────────────────────────
    @GetMapping("/students")
    public String students(Model model) {
        model.addAttribute("students", studentRepo.findAll());
        return "admin/students";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes ra) {
        studentRepo.deleteById(id);
        ra.addFlashAttribute("success", "Student deleted.");
        return "redirect:/admin/students";
    }

    // ── CAREERS ──────────────────────────────────────────────────────────────
    @GetMapping("/careers")
    public String careers(Model model) {
        model.addAttribute("careers", careerRepo.findAll());
        return "admin/careers";
    }

    @PostMapping("/careers/save")
    public String saveCareer(@RequestParam String careerName, @RequestParam String domain,
            @RequestParam(required=false,defaultValue="") String description,
            @RequestParam(required=false,defaultValue="") String requiredSkills,
            @RequestParam double minimumCGPA,
            @RequestParam(required=false,defaultValue="0") double averageSalary,
            RedirectAttributes ra) {
        Career c = new Career();
        c.setCareerName(careerName); c.setDomain(domain); c.setDescription(description);
        c.setRequiredSkills(requiredSkills); c.setMinimumCGPA(minimumCGPA); c.setAverageSalary(averageSalary);
        careerRepo.save(c);
        ra.addFlashAttribute("success", "Career '" + careerName + "' added.");
        return "redirect:/admin/careers";
    }

    @GetMapping("/careers/delete/{id}")
    public String deleteCareer(@PathVariable Long id, RedirectAttributes ra) {
        careerRepo.deleteById(id);
        ra.addFlashAttribute("success", "Career deleted.");
        return "redirect:/admin/careers";
    }

    // ── SKILLS ───────────────────────────────────────────────────────────────
    @GetMapping("/skills")
    public String skills(Model model) {
        model.addAttribute("skills", skillRepo.findAll());
        return "admin/skills";
    }

    @PostMapping("/skills/save")
    public String saveSkill(@RequestParam String skillName, @RequestParam String skillCategory,
            @RequestParam(required=false,defaultValue="") String description,
            @RequestParam(required=false,defaultValue="MEDIUM") String difficultyLevel,
            @RequestParam(required=false,defaultValue="") String industryRelevance,
            RedirectAttributes ra) {
        Skill sk = new Skill();
        sk.setSkillName(skillName); sk.setSkillCategory(skillCategory);
        sk.setDescription(description); sk.setDifficultyLevel(difficultyLevel);
        sk.setIndustryRelevance(industryRelevance); sk.setActive(true);
        skillRepo.save(sk);
        ra.addFlashAttribute("success", "Skill '" + skillName + "' added.");
        return "redirect:/admin/skills";
    }

    @GetMapping("/skills/toggle/{id}")
    public String toggleSkill(@PathVariable Long id, RedirectAttributes ra) {
        skillRepo.findById(id).ifPresent(sk -> { sk.setActive(!sk.isActive()); skillRepo.save(sk); });
        return "redirect:/admin/skills";
    }

    // ── APTITUDE TESTS ───────────────────────────────────────────────────────
    @GetMapping("/aptitude")
    public String aptitudePage(Model model) {
        List<AptitudeTest> tests = aptitudeTestRepo.findAll();
        model.addAttribute("tests", tests);
        // question counts per test
        java.util.Map<Long,Long> qCounts = new java.util.HashMap<>();
        tests.forEach(t -> qCounts.put(t.getTestId(), questionRepo.countByTest_TestId(t.getTestId())));
        model.addAttribute("questionCounts", qCounts);
        return "admin/aptitude";
    }

    @PostMapping("/aptitude/save")
    public String saveTest(@RequestParam String testName, @RequestParam String testType,
            @RequestParam(required=false,defaultValue="MEDIUM") String difficultyLevel,
            @RequestParam int duration,
            RedirectAttributes ra) {
        AptitudeTest test = new AptitudeTest();
        test.setTestName(testName); test.setTestType(testType);
        test.setDifficultyLevel(difficultyLevel); test.setDuration(duration);
        test.setTotalQuestions(0); test.setTotalMarks(0); test.setStatus("ACTIVE");
        aptitudeTestRepo.save(test);
        ra.addFlashAttribute("success", "Test '" + testName + "' created. Now add questions to it.");
        return "redirect:/admin/aptitude";
    }

    @GetMapping("/aptitude/delete/{id}")
    public String deleteTest(@PathVariable Long id, RedirectAttributes ra) {
        // delete questions first
        questionRepo.deleteAll(questionRepo.findByTest_TestId(id));
        aptitudeTestRepo.deleteById(id);
        ra.addFlashAttribute("success", "Test deleted.");
        return "redirect:/admin/aptitude";
    }

    @GetMapping("/aptitude/toggle/{id}")
    public String toggleTest(@PathVariable Long id, RedirectAttributes ra) {
        aptitudeTestRepo.findById(id).ifPresent(t -> {
            t.setStatus("ACTIVE".equals(t.getStatus()) ? "INACTIVE" : "ACTIVE");
            aptitudeTestRepo.save(t);
        });
        return "redirect:/admin/aptitude";
    }

    // ── APTITUDE QUESTIONS ───────────────────────────────────────────────────
    @GetMapping("/aptitude/{testId}/questions")
    public String questionsPage(@PathVariable Long testId, Model model) {
        AptitudeTest test = aptitudeTestRepo.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));
        model.addAttribute("test", test);
        model.addAttribute("questions", questionRepo.findByTest_TestId(testId));
        return "admin/questions";
    }

    @PostMapping("/aptitude/{testId}/questions/save")
    public String saveQuestion(@PathVariable Long testId,
            @RequestParam String questionText,
            @RequestParam String optionA, @RequestParam String optionB,
            @RequestParam String optionC, @RequestParam String optionD,
            @RequestParam String correctAnswer,
            @RequestParam(required=false,defaultValue="1") int marks,
            @RequestParam String section,
            RedirectAttributes ra) {

        AptitudeTest test = aptitudeTestRepo.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        AptitudeQuestion q = new AptitudeQuestion();
        q.setTest(test); q.setQuestionText(questionText);
        q.setOptionA(optionA); q.setOptionB(optionB);
        q.setOptionC(optionC); q.setOptionD(optionD);
        q.setCorrectAnswer(correctAnswer.toUpperCase());
        q.setMarks(marks); q.setSection(section);
        questionRepo.save(q);

        // Update test totals
        long qCount = questionRepo.countByTest_TestId(testId);
        int totalMarks = questionRepo.findByTest_TestId(testId)
                .stream().mapToInt(AptitudeQuestion::getMarks).sum();
        test.setTotalQuestions((int) qCount);
        test.setTotalMarks(totalMarks);
        aptitudeTestRepo.save(test);

        ra.addFlashAttribute("success", "Question added successfully!");
        return "redirect:/admin/aptitude/" + testId + "/questions";
    }

    @GetMapping("/aptitude/{testId}/questions/delete/{qId}")
    public String deleteQuestion(@PathVariable Long testId, @PathVariable Long qId,
            RedirectAttributes ra) {
        questionRepo.deleteById(qId);
        // Recalculate test totals
        aptitudeTestRepo.findById(testId).ifPresent(test -> {
            List<AptitudeQuestion> remaining = questionRepo.findByTest_TestId(testId);
            test.setTotalQuestions(remaining.size());
            test.setTotalMarks(remaining.stream().mapToInt(AptitudeQuestion::getMarks).sum());
            aptitudeTestRepo.save(test);
        });
        ra.addFlashAttribute("success", "Question deleted.");
        return "redirect:/admin/aptitude/" + testId + "/questions";
    }

    // ── RECOMMENDATIONS ──────────────────────────────────────────────────────
    @GetMapping("/recommend")
    public String recommendPage(Model model) {
        model.addAttribute("students", studentRepo.findAll());
        return "admin/recommend";
    }

    @PostMapping("/recommend")
    public String generate(@RequestParam Long studentId, Model model) {
        try {
            List<RecommendationResult> results = recommendationService.generate(studentId);
            model.addAttribute("results", results);
            model.addAttribute("studentId", studentId);
            model.addAttribute("studentName",
                    results.isEmpty() ? studentRepo.findById(studentId)
                            .map(Student::getFullName).orElse("N/A")
                            : results.get(0).getStudent().getFullName());
        } catch (Exception e) {
            log.error("Recommendation error for student {}: {}", studentId, e.getMessage());
            model.addAttribute("error", "Could not generate: " + e.getMessage());
        }
        model.addAttribute("students", studentRepo.findAll());
        return "admin/recommend";
    }

    @GetMapping("/recommend/download/{studentId}")
    public void downloadReport(@PathVariable Long studentId, HttpServletResponse response) throws IOException {
        List<RecommendationResult> results =
                recommendationResultRepo.findByStudent_StudentIdOrderByRankAsc(studentId);
        String content = fileHandlingUtil.export(results, studentId);
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"recommendation_" + studentId + ".txt\"");
        response.getWriter().write(content);
    }

    // ── USERS ────────────────────────────────────────────────────────────────
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "admin/users";
    }
}
