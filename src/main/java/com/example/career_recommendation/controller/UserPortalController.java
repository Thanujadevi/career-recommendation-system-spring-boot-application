package com.example.career_recommendation.controller;

import com.example.career_recommendation.model.*;
import com.example.career_recommendation.repository.*;
import com.example.career_recommendation.service.RecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/portal")
public class UserPortalController {

    private static final Logger log = LoggerFactory.getLogger(UserPortalController.class);

    private final UserRepository userRepo;
    private final StudentRepository studentRepo;
    private final SkillRepository skillRepo;
    private final StudentSkillRepository studentSkillRepo;
    private final InterestRepository interestRepo;
    private final AptitudeTestRepository aptitudeTestRepo;
    private final AptitudeQuestionRepository questionRepo;
    private final AptitudeResultRepository aptitudeResultRepo;
    private final RecommendationService recommendationService;
    private final RecommendationResultRepository recommendationResultRepo;
    private final PasswordEncoder passwordEncoder;

    public UserPortalController(
            UserRepository userRepo, StudentRepository studentRepo,
            SkillRepository skillRepo, StudentSkillRepository studentSkillRepo,
            InterestRepository interestRepo, AptitudeTestRepository aptitudeTestRepo,
            AptitudeQuestionRepository questionRepo, AptitudeResultRepository aptitudeResultRepo,
            RecommendationService recommendationService,
            RecommendationResultRepository recommendationResultRepo,
            PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo; this.studentRepo = studentRepo;
        this.skillRepo = skillRepo; this.studentSkillRepo = studentSkillRepo;
        this.interestRepo = interestRepo; this.aptitudeTestRepo = aptitudeTestRepo;
        this.questionRepo = questionRepo; this.aptitudeResultRepo = aptitudeResultRepo;
        this.recommendationService = recommendationService;
        this.recommendationResultRepo = recommendationResultRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // ── HELPER ───────────────────────────────────────────────────────────────
    private Student getCurrentStudent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return studentRepo.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("No student profile found"));
    }

    // ── LOGIN / REGISTER ──────────────────────────────────────────────────────
    @GetMapping("/login")
    public String loginPage(@RequestParam(required=false) String error,
                            @RequestParam(required=false) String logout, Model model) {
        if (error != null) model.addAttribute("error", "Invalid username or password.");
        if (logout != null) model.addAttribute("success", "You have been logged out.");
        return "portal/login";
    }

    @GetMapping("/register")
    public String registerPage() { return "portal/register"; }

    @PostMapping("/register")
    public String processRegister(
            @RequestParam String username, @RequestParam String password,
            @RequestParam String email, @RequestParam String fullName,
            @RequestParam String department, @RequestParam double cgpa,
            @RequestParam(required=false,defaultValue="") String gender,
            @RequestParam(required=false,defaultValue="1") int yearOfStudy,
            @RequestParam(required=false,defaultValue="") String collegeName,
            @RequestParam(required=false,defaultValue="") String enrollmentNumber,
            RedirectAttributes ra) {
        if (userRepo.findByUsername(username).isPresent()) {
            ra.addFlashAttribute("error", "Username already exists.");
            return "redirect:/portal/register";
        }
        if (password.length() < 6) {
            ra.addFlashAttribute("error", "Password must be at least 6 characters.");
            return "redirect:/portal/register";
        }
        User user = new User();
        user.setUsername(username); user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email); user.setRole("STUDENT"); user.setStatus("ACTIVE");
        userRepo.save(user);

        Student s = new Student();
        s.setUser(user); s.setFullName(fullName); s.setDepartment(department);
        s.setCgpa(cgpa); s.setGender(gender); s.setYearOfStudy(yearOfStudy);
        s.setCollegeName(collegeName.isEmpty() ? null : collegeName);
        s.setEnrollmentNumber(enrollmentNumber.isEmpty() ? null : enrollmentNumber);
        studentRepo.save(s);

        ra.addFlashAttribute("success", "Registration successful! Please log in.");
        return "redirect:/portal/login";
    }

    // ── DASHBOARD ────────────────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Student student = getCurrentStudent();
        Long sid = student.getStudentId();
        model.addAttribute("student", student);
        model.addAttribute("skillCount", studentSkillRepo.findByStudent_StudentId(sid).size());
        model.addAttribute("interestCount", interestRepo.findByStudent_StudentId(sid).size());
        model.addAttribute("aptitudeCount", aptitudeResultRepo.findByStudent_StudentId(sid).size());
        List<RecommendationResult> results =
                recommendationResultRepo.findByStudent_StudentIdOrderByRankAsc(sid);
        model.addAttribute("topCareer", results.isEmpty() ? null : results.get(0));
        model.addAttribute("recommendationCount", results.size());
        return "portal/dashboard";
    }

    // ── PROFILE ───────────────────────────────────────────────────────────────
    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("student", getCurrentStudent());
        return "portal/profile";
    }

    @PostMapping("/portal/profile/update")
public String updateProfile(@ModelAttribute Student student, Model model) {
    // Save the updated student info (implement your service logic)
    studentService.updateStudent(student);
    model.addAttribute("student", student);
    model.addAttribute("success", "Profile updated successfully!");
    return "portal/profile";
}
    // ── SKILLS ───────────────────────────────────────────────────────────────
    @GetMapping("/skills")
    public String skillsPage(Model model) {
        Student student = getCurrentStudent();
        model.addAttribute("student", student);
        model.addAttribute("mySkills", studentSkillRepo.findByStudent_StudentId(student.getStudentId()));
        model.addAttribute("allSkills", skillRepo.findByActiveTrue());
        return "portal/skills";
    }

    @PostMapping("/skills/save")
    public String saveSkill(@RequestParam Long skillId, @RequestParam String skillLevel,
            @RequestParam int experienceYears,
            @RequestParam(required=false,defaultValue="") String certification,
            RedirectAttributes ra) {
        Student student = getCurrentStudent();
        Skill skill = skillRepo.findById(skillId).orElseThrow(() -> new RuntimeException("Skill not found"));

        StudentSkill ss = new StudentSkill();
        ss.setStudent(student); ss.setSkill(skill);
        ss.setSkillLevel(skillLevel);
        ss.setExperienceYears(experienceYears);
        ss.setCertification(certification.isEmpty() ? null : certification);
        // AUTO-DERIVE proficiency score from level — no manual entry needed
        ss.setProficiencyScore(StudentSkill.deriveScore(skillLevel));
        studentSkillRepo.save(ss);

        ra.addFlashAttribute("success", "Skill added! Proficiency score auto-set to "
                + (int) StudentSkill.deriveScore(skillLevel) + "/100 for " + skillLevel + " level.");
        return "redirect:/portal/skills";
    }

    @GetMapping("/skills/delete/{id}")
    public String deleteSkill(@PathVariable Long id, RedirectAttributes ra) {
        studentSkillRepo.deleteById(id);
        ra.addFlashAttribute("success", "Skill removed.");
        return "redirect:/portal/skills";
    }

    // ── INTERESTS ────────────────────────────────────────────────────────────
    @GetMapping("/interests")
    public String interestsPage(Model model) {
        Student student = getCurrentStudent();
        model.addAttribute("student", student);
        model.addAttribute("interests", interestRepo.findByStudent_StudentId(student.getStudentId()));
        return "portal/interests";
    }

    @PostMapping("/interests/save")
    public String saveInterest(@RequestParam String interestArea,
            @RequestParam String interestLevel,
            @RequestParam(required=false,defaultValue="") String preferredDomain,
            RedirectAttributes ra) {
        Student student = getCurrentStudent();
        Interest interest = new Interest();
        interest.setStudent(student); interest.setInterestArea(interestArea);
        interest.setInterestLevel(interestLevel);
        interest.setPreferredDomain(preferredDomain.isEmpty() ? null : preferredDomain);
        interestRepo.save(interest);
        ra.addFlashAttribute("success", "Interest added!");
        return "redirect:/portal/interests";
    }

    @GetMapping("/interests/delete/{id}")
    public String deleteInterest(@PathVariable Long id, RedirectAttributes ra) {
        interestRepo.deleteById(id);
        ra.addFlashAttribute("success", "Interest removed.");
        return "redirect:/portal/interests";
    }

    // ── APTITUDE — list available tests ──────────────────────────────────────
    @GetMapping("/aptitude")
    public String aptitudePage(Model model) {
        Student student = getCurrentStudent();
        List<AptitudeTest> activeTests = aptitudeTestRepo.findByStatus("ACTIVE");

        // Which tests has this student already attempted?
        List<Long> attemptedTestIds = aptitudeResultRepo
                .findByStudent_StudentId(student.getStudentId())
                .stream().map(r -> r.getTest().getTestId()).toList();

        model.addAttribute("student", student);
        model.addAttribute("tests", activeTests);
        model.addAttribute("attemptedTestIds", attemptedTestIds);
        model.addAttribute("results", aptitudeResultRepo.findByStudent_StudentId(student.getStudentId()));
        return "portal/aptitude";
    }

    // ── APTITUDE — take a specific test ──────────────────────────────────────
    @GetMapping("/aptitude/take/{testId}")
    public String takeTest(@PathVariable Long testId, Model model, RedirectAttributes ra) {
        AptitudeTest test = aptitudeTestRepo.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));
        List<AptitudeQuestion> questions = questionRepo.findByTest_TestId(testId);

        if (questions.isEmpty()) {
            ra.addFlashAttribute("error", "This test has no questions yet. Please check back later.");
            return "redirect:/portal/aptitude";
        }

        // Check already attempted
        Student student = getCurrentStudent();
        boolean attempted = aptitudeResultRepo.findByStudent_StudentId(student.getStudentId())
                .stream().anyMatch(r -> r.getTest().getTestId().equals(testId));
        if (attempted) {
            ra.addFlashAttribute("error", "You have already attempted this test.");
            return "redirect:/portal/aptitude";
        }

        model.addAttribute("test", test);
        model.addAttribute("questions", questions);
        return "portal/take-test";
    }

    // ── APTITUDE — submit answers, auto-calculate score ───────────────────────
    @PostMapping("/aptitude/submit/{testId}")
    public String submitTest(@PathVariable Long testId,
            @RequestParam Map<String, String> allParams,
            RedirectAttributes ra) {

        AptitudeTest test = aptitudeTestRepo.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));
        List<AptitudeQuestion> questions = questionRepo.findByTest_TestId(testId);
        Student student = getCurrentStudent();

        double totalScore = 0, logicalScore = 0, verbalScore = 0, quantScore = 0;

        for (AptitudeQuestion q : questions) {
            String paramKey = "answer_" + q.getQuestionId();
            String studentAnswer = allParams.getOrDefault(paramKey, "");
            if (q.getCorrectAnswer().equalsIgnoreCase(studentAnswer)) {
                totalScore += q.getMarks();
                switch (q.getSection().toUpperCase()) {
                    case "LOGICAL"       -> logicalScore += q.getMarks();
                    case "VERBAL"        -> verbalScore  += q.getMarks();
                    case "QUANTITATIVE"  -> quantScore   += q.getMarks();
                    // MIXED counts toward all proportionally
                    default -> {
                        logicalScore += q.getMarks() / 3.0;
                        verbalScore  += q.getMarks() / 3.0;
                        quantScore   += q.getMarks() / 3.0;
                    }
                }
            }
        }

        double percentage = test.getTotalMarks() > 0
                ? Math.min((totalScore / test.getTotalMarks()) * 100.0, 100.0) : 0;

        AptitudeResult result = new AptitudeResult();
        result.setStudent(student); result.setTest(test);
        result.setScore(totalScore); result.setPercentage(percentage);
        result.setLogicalScore(logicalScore);
        result.setVerbalScore(verbalScore);
        result.setQuantitativeScore(quantScore);
        aptitudeResultRepo.save(result);

        log.info("Student {} completed test '{}': {}/{} = {:.1f}%",
                student.getFullName(), test.getTestName(), totalScore, test.getTotalMarks(), percentage);

        ra.addFlashAttribute("success", String.format(
                "Test submitted! You scored %.0f / %d (%.1f%%)",
                totalScore, test.getTotalMarks(), percentage));
        return "redirect:/portal/aptitude";
    }

    // ── RECOMMENDATIONS ───────────────────────────────────────────────────────
    @GetMapping("/recommendation")
    public String myRecommendations(Model model) {
        Student student = getCurrentStudent();
        model.addAttribute("student", student);
        model.addAttribute("results",
                recommendationResultRepo.findByStudent_StudentIdOrderByRankAsc(student.getStudentId()));
        return "portal/recommendation";
    }

    @PostMapping("/recommendation/generate")
    public String generateRecommendation(RedirectAttributes ra) {
        Student student = getCurrentStudent();
        try {
            List<RecommendationResult> results = recommendationService.generate(student.getStudentId());
            if (results.isEmpty()) {
                ra.addFlashAttribute("error",
                        "No careers matched your profile yet. Make sure you have added skills, " +
                        "interests, and completed at least one aptitude test.");
            } else {
                ra.addFlashAttribute("success",
                        "Generated " + results.size() + " career recommendations for you!");
            }
        } catch (Exception e) {
            log.error("Recommendation generation failed: {}", e.getMessage());
            ra.addFlashAttribute("error", "Could not generate recommendations: " + e.getMessage());
        }
        return "redirect:/portal/recommendation";
    }
}
