-- Career Recommendation System — Seed Data
-- admin123   : $2a$10$3dDBUI97cb8Lh.xP0w7UhOvYJ6c0xODma7UnEeSD6yL/TLgu3P3si
-- student123 : $2a$10$2r4dAfjdB01Y7ao4MkR70OS8hnXvFb4V/31sA8d3l8FuQm.Tn/GDK

INSERT INTO users (username, password, role, email, phone_number, status, security_question, security_answer, created_date, updated_date)
VALUES ('admin', '$2a$10$3dDBUI97cb8Lh.xP0w7UhOvYJ6c0xODma7UnEeSD6yL/TLgu3P3si', 'ADMIN', 'admin@college.edu', '9876543210', 'ACTIVE', 'Pet name?', 'Tiger', NOW(), NOW());

INSERT INTO users (username, password, role, email, phone_number, status, security_question, security_answer, created_date, updated_date)
VALUES ('student1', '$2a$10$2r4dAfjdB01Y7ao4MkR70OS8hnXvFb4V/31sA8d3l8FuQm.Tn/GDK', 'STUDENT', 'ravi@gmail.com', '9123456780', 'ACTIVE', 'School name?', 'GHS', NOW(), NOW());

INSERT INTO student (user_id, full_name, gender, department, year_of_study, college_name, cgpa, enrollment_number, address, created_date, updated_date)
VALUES ((SELECT user_id FROM users WHERE username='student1'), 'Ravi Kumar', 'Male', 'Computer Science', 3, 'Sri Krishna College', 8.5, 'CS2021001', 'Kovilpatti', NOW(), NOW());

INSERT INTO skill (skill_name, skill_category, description, difficulty_level, industry_relevance, is_active, created_date, updated_date) VALUES ('Java','Programming','Core Java programming','MEDIUM','IT',true,NOW(),NOW());
INSERT INTO skill (skill_name, skill_category, description, difficulty_level, industry_relevance, is_active, created_date, updated_date) VALUES ('Python','Programming','Python and data analysis','MEDIUM','IT/Data Science',true,NOW(),NOW());
INSERT INTO skill (skill_name, skill_category, description, difficulty_level, industry_relevance, is_active, created_date, updated_date) VALUES ('Machine Learning','AI/ML','ML algorithms','HIGH','Data Science',true,NOW(),NOW());
INSERT INTO skill (skill_name, skill_category, description, difficulty_level, industry_relevance, is_active, created_date, updated_date) VALUES ('Spring Boot','Framework','Java Spring Boot','MEDIUM','IT',true,NOW(),NOW());
INSERT INTO skill (skill_name, skill_category, description, difficulty_level, industry_relevance, is_active, created_date, updated_date) VALUES ('SQL','Database','Relational database queries','LOW','IT/Finance',true,NOW(),NOW());

INSERT INTO student_skill (student_id, skill_id, skill_level, experience_years, certification, proficiency_score, created_date, updated_date)
VALUES ((SELECT student_id FROM student WHERE enrollment_number='CS2021001'),(SELECT skill_id FROM skill WHERE skill_name='Java'),'INTERMEDIATE',2,'Oracle Java SE',60,NOW(),NOW());
INSERT INTO student_skill (student_id, skill_id, skill_level, experience_years, certification, proficiency_score, created_date, updated_date)
VALUES ((SELECT student_id FROM student WHERE enrollment_number='CS2021001'),(SELECT skill_id FROM skill WHERE skill_name='Python'),'BEGINNER',1,NULL,30,NOW(),NOW());

INSERT INTO interest (student_id, interest_area, interest_level, preferred_domain, created_date, updated_date)
VALUES ((SELECT student_id FROM student WHERE enrollment_number='CS2021001'),'Software Development','HIGH','IT',NOW(),NOW());
INSERT INTO interest (student_id, interest_area, interest_level, preferred_domain, created_date, updated_date)
VALUES ((SELECT student_id FROM student WHERE enrollment_number='CS2021001'),'Data Analysis','MEDIUM','Data Science',NOW(),NOW());

INSERT INTO academic_profile (student_id, tenth_percentage, twelfth_percentage, diploma_percentage, current_cgpa, backlogs, specialization, university_name, academic_score, created_date, updated_date)
VALUES ((SELECT student_id FROM student WHERE enrollment_number='CS2021001'),88.0,82.5,0,8.5,0,'Computer Science','Anna University',85.0,NOW(),NOW());

-- Aptitude test seeded for admin to add questions to
INSERT INTO aptitude_test (test_name, test_type, total_questions, total_marks, duration, difficulty_level, status, created_date, updated_date)
VALUES ('General Aptitude Test','MIXED',0,0,30,'MEDIUM','ACTIVE',NOW(),NOW());

INSERT INTO career (career_name, domain, description, required_skills, minimum_cgpa, average_salary, created_date, updated_date) VALUES ('Software Developer','IT','Develop software applications','Java, Python, Spring Boot',6.0,600000,NOW(),NOW());
INSERT INTO career (career_name, domain, description, required_skills, minimum_cgpa, average_salary, created_date, updated_date) VALUES ('Data Scientist','Data Science','Analyze data and build ML models','Python, ML, SQL',7.0,900000,NOW(),NOW());
INSERT INTO career (career_name, domain, description, required_skills, minimum_cgpa, average_salary, created_date, updated_date) VALUES ('Database Administrator','IT','Manage and optimize databases','SQL, Oracle, MySQL',6.0,550000,NOW(),NOW());
INSERT INTO career (career_name, domain, description, required_skills, minimum_cgpa, average_salary, created_date, updated_date) VALUES ('Machine Learning Engineer','AI/ML','Build and deploy ML models','Python, ML, TensorFlow',7.5,1100000,NOW(),NOW());
INSERT INTO career (career_name, domain, description, required_skills, minimum_cgpa, average_salary, created_date, updated_date) VALUES ('Full Stack Developer','IT','Frontend and backend development','Java, Spring Boot, React',6.5,700000,NOW(),NOW());

INSERT INTO recommendation_rule (career_id, academic_weight, skill_weight, interest_weight, aptitude_weight, minimum_score, created_date, updated_date) VALUES ((SELECT career_id FROM career WHERE career_name='Software Developer'),0.3,0.4,0.2,0.1,30.0,NOW(),NOW());
INSERT INTO recommendation_rule (career_id, academic_weight, skill_weight, interest_weight, aptitude_weight, minimum_score, created_date, updated_date) VALUES ((SELECT career_id FROM career WHERE career_name='Data Scientist'),0.2,0.3,0.3,0.2,30.0,NOW(),NOW());
INSERT INTO recommendation_rule (career_id, academic_weight, skill_weight, interest_weight, aptitude_weight, minimum_score, created_date, updated_date) VALUES ((SELECT career_id FROM career WHERE career_name='Database Administrator'),0.3,0.3,0.2,0.2,25.0,NOW(),NOW());
INSERT INTO recommendation_rule (career_id, academic_weight, skill_weight, interest_weight, aptitude_weight, minimum_score, created_date, updated_date) VALUES ((SELECT career_id FROM career WHERE career_name='Machine Learning Engineer'),0.25,0.35,0.25,0.15,30.0,NOW(),NOW());
INSERT INTO recommendation_rule (career_id, academic_weight, skill_weight, interest_weight, aptitude_weight, minimum_score, created_date, updated_date) VALUES ((SELECT career_id FROM career WHERE career_name='Full Stack Developer'),0.3,0.35,0.2,0.15,30.0,NOW(),NOW());
