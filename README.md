# Career Recommendation System

## Overview
An intelligent career guidance platform for college students. It analyzes academic records, skills, interests, and aptitude to recommend suitable career paths. The system supports both students and administrators for career planning and management.

## Features

### Student Portal
- Registration and login (role-based)
- Profile management (academics, skills, certifications)
- Skill and interest tracking
- Aptitude test and interactive assessments
- Personalized, ranked career recommendations

### Admin Dashboard
- Student and user management
- Aptitude test/question management
- Career and skill catalog administration
- Recommendation rule configuration
- Bulk operations and reporting

### Recommendation Engine
- Multi-factor weighted scoring (academics, skills, interests, aptitude)
- Configurable rules and thresholds
- Ranked results for best-fit careers

## Tech Stack
- **Backend:** Spring Boot 3.3.2, Spring Security, Spring Data JPA
- **Frontend:** Thymeleaf, HTML/CSS
- **Database:** H2 (in-memory)
- **Build Tool:** Maven 3.9.x
- **Language:** Java 17
- **Testing:** JUnit, Spring Security Test
- **Other:** Lombok, Bean Validation

## Setup & Usage

### Prerequisites
- Java 17+
- Maven 3.9+

### Build & Run
./mvnw clean package
./mvnw spring-boot:run

App runs at: http://localhost:8080

Default Credentials
Admin: admin / admin123
Student: student1 / student123

Student Workflow
Register or log in
Complete profile and add interests/skills
Take aptitude test
View recommendations

Admin Workflow
Log in at /admin
Manage students, careers, skills, tests, and recommendations

Key Files
pom.xml
src/main/resources/application.properties
src/main/resources/data.sql
src/main/java/com/example/career_recommendation/config/SecurityConfig.java
src/main/java/com/example/career_recommendation/service/impl/RecommendationServiceImpl.java


