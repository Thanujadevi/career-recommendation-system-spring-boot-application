package com.example.career_recommendation.config;

import com.example.career_recommendation.model.*;
import com.example.career_recommendation.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * DataInitializer — seeds H2 with BCrypt-encoded demo data on startup.
 * Replaces the plain-text data.sql seeding for users.
 *
 * Credentials:
 *   Admin   -> username: admin,    password: admin123
 *   Student -> username: student1, password: student123
 */
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner seedDatabase(
            UserRepository userRepo,
            StudentRepository studentRepo,
            SkillRepository skillRepo,
            StudentSkillRepository studentSkillRepo,
            InterestRepository interestRepo,
            AptitudeTestRepository aptitudeTestRepo,
            AptitudeResultRepository aptitudeResultRepo,
            CareerRepository careerRepo,
            RecommendationRuleRepository ruleRepo,
            PasswordEncoder encoder) {

        return args -> {
            if (userRepo.count() > 0) {
                log.info("DB already seeded — skipping.");
                return;
            }
            log.info("Seeding database...");

            // ADMIN USER
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(encoder.encode("admin123"));
            adminUser.setEmail("admin@college.edu");
            adminUser.setRole("ADMIN");
            adminUser.setStatus("ACTIVE");
            adminUser.setPhoneNumber("9876543210");
            adminUser.setSecurityQuestion("What is your pet name?");
            adminUser.setSecurityAnswer("Tiger");
            userRepo.save(adminUser);

            // STUDENT USER
            User studentUser = new User();
            studentUser.setUsername("student1");
            studentUser.setPassword(encoder.encode("student123"));
            studentUser.setEmail("ravi@gmail.com");
            studentUser.setRole("STUDENT");
            studentUser.setStatus("ACTIVE");
            studentUser.setPhoneNumber("9123456780");
            studentUser.setSecurityQuestion("What is your school name?");
            studentUser.setSecurityAnswer("GHS");
            userRepo.save(studentUser);

            // STUDENT PROFILE
            Student student = new Student();
            student.setUser(studentUser);
            student.setFullName("Ravi Kumar");
            student.setGender("Male");
            student.setDepartment("Computer Science");
            student.setYearOfStudy(3);
            student.setCollegeName("Sri Krishna College");
            student.setCgpa(8.5);
            student.setEnrollmentNumber("CS2021001");
            student.setAddress("Kovilpatti");
            studentRepo.save(student);

            // SKILLS
            Skill java   = buildSkill("Java",             "Programming", "Core Java programming",                  "MEDIUM", "IT");
            Skill python = buildSkill("Python",           "Programming", "Python programming and data analysis",   "MEDIUM", "IT/Data Science");
            Skill ml     = buildSkill("Machine Learning", "AI/ML",       "ML algorithms and model building",       "HIGH",   "Data Science");
            Skill spring = buildSkill("Spring Boot",      "Framework",   "Java Spring Boot backend",               "MEDIUM", "IT");
            Skill sql    = buildSkill("SQL",              "Database",    "Relational database queries",            "LOW",    "IT/Finance");
            skillRepo.saveAll(List.of(java, python, ml, spring, sql));

            // STUDENT SKILLS
            StudentSkill ss1 = new StudentSkill();
            ss1.setStudent(student); ss1.setSkill(java);
            ss1.setSkillLevel("INTERMEDIATE"); ss1.setExperienceYears(2);
            ss1.setCertification("Oracle Java SE"); ss1.setProficiencyScore(75);

            StudentSkill ss2 = new StudentSkill();
            ss2.setStudent(student); ss2.setSkill(python);
            ss2.setSkillLevel("BEGINNER"); ss2.setExperienceYears(1);
            ss2.setProficiencyScore(55);
            studentSkillRepo.saveAll(List.of(ss1, ss2));

            // INTERESTS
            Interest i1 = new Interest();
            i1.setStudent(student); i1.setInterestArea("Software Development");
            i1.setInterestLevel("HIGH"); i1.setPreferredDomain("IT");

            Interest i2 = new Interest();
            i2.setStudent(student); i2.setInterestArea("Data Analysis");
            i2.setInterestLevel("MEDIUM"); i2.setPreferredDomain("Data Science");
            interestRepo.saveAll(List.of(i1, i2));

            // APTITUDE TEST
            AptitudeTest test = new AptitudeTest();
            test.setTestName("General Aptitude Test"); test.setTestType("MIXED");
            test.setTotalQuestions(30); test.setTotalMarks(100);
            test.setDuration(60); test.setDifficultyLevel("MEDIUM"); test.setStatus("ACTIVE");
            aptitudeTestRepo.save(test);

            // APTITUDE RESULT
            AptitudeResult ar = new AptitudeResult();
            ar.setStudent(student); ar.setTest(test);
            ar.setScore(78); ar.setPercentage(78.0);
            ar.setLogicalScore(80); ar.setVerbalScore(72); ar.setQuantitativeScore(82);
            aptitudeResultRepo.save(ar);

            // CAREERS
            Career c1 = buildCareer("Software Developer",       "IT",           "Develop software applications",          "Java, Python, Spring Boot",  6.0, 600000);
            Career c2 = buildCareer("Data Scientist",           "Data Science", "Analyze data and build ML models",       "Python, ML, SQL",            7.0, 900000);
            Career c3 = buildCareer("Database Administrator",   "IT",           "Manage and optimize databases",          "SQL, Oracle, MySQL",         6.0, 550000);
            Career c4 = buildCareer("Machine Learning Engineer","AI/ML",        "Build and deploy ML models",             "Python, ML, TensorFlow",     7.5, 1100000);
            Career c5 = buildCareer("Full Stack Developer",     "IT",           "Frontend and backend web development",   "Java, Spring Boot, React",   6.5, 700000);
            careerRepo.saveAll(List.of(c1, c2, c3, c4, c5));

            // RECOMMENDATION RULES
            ruleRepo.saveAll(List.of(
                buildRule(c1, 0.30, 0.40, 0.20, 0.10, 50.0),
                buildRule(c2, 0.20, 0.30, 0.30, 0.20, 55.0),
                buildRule(c3, 0.30, 0.30, 0.20, 0.20, 45.0),
                buildRule(c4, 0.25, 0.35, 0.25, 0.15, 60.0),
                buildRule(c5, 0.30, 0.35, 0.20, 0.15, 50.0)
            ));

            log.info("Seeding complete. admin/admin123 | student1/student123");
        };
    }

    private Skill buildSkill(String name, String cat, String desc, String diff, String rel) {
        Skill s = new Skill();
        s.setSkillName(name); s.setSkillCategory(cat); s.setDescription(desc);
        s.setDifficultyLevel(diff); s.setIndustryRelevance(rel); s.setActive(true);
        return s;
    }

    private Career buildCareer(String name, String domain, String desc, String skills, double cgpa, double salary) {
        Career c = new Career();
        c.setCareerName(name); c.setDomain(domain); c.setDescription(desc);
        c.setRequiredSkills(skills); c.setMinimumCGPA(cgpa); c.setAverageSalary(salary);
        return c;
    }

    private RecommendationRule buildRule(Career career, double aw, double sw, double iw, double aptw, double min) {
        RecommendationRule r = new RecommendationRule();
        r.setCareer(career); r.setAcademicWeight(aw); r.setSkillWeight(sw);
        r.setInterestWeight(iw); r.setAptitudeWeight(aptw); r.setMinimumScore(min);
        return r;
    }
}
