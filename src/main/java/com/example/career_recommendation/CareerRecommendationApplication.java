package com.example.career_recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CareerRecommendationApplication — Spring Boot entry point.
 *
 * FIX: Removed the conflicting @RestController + @GetMapping("/") that was
 * originally here. It clashed with PageController's @GetMapping("/"),
 * causing an "Ambiguous handler methods" error at startup.
 *
 * @SpringBootApplication is a meta-annotation that combines:
 *   - @Configuration       : marks this as a config class
 *   - @EnableAutoConfiguration : lets Spring Boot auto-configure beans
 *   - @ComponentScan       : scans all sub-packages for @Component, @Service, etc.
 */
@SpringBootApplication
public class CareerRecommendationApplication {

    public static void main(String[] args) {
        SpringApplication.run(CareerRecommendationApplication.class, args);
    }
}
