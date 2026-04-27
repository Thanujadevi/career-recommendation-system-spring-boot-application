package com.example.career_recommendation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * PageController — handles only the root "/" redirect.
 *
 * FIX: All admin page routes moved to AdminController (/admin/**).
 * All student page routes are in UserPortalController (/portal/**).
 * This class purely redirects the root to the login page.
 * Spring Security will handle the redirect to the correct dashboard after login.
 */
@Controller
public class PageController {

    /**
     * Root "/" — redirect unauthenticated users to login.
     * Spring Security's success handler redirects authenticated users to their dashboard.
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/portal/login";
    }
}
