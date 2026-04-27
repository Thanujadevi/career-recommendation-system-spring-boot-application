package com.example.career_recommendation.security;

import com.example.career_recommendation.model.User;
import com.example.career_recommendation.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CustomUserDetailsService — bridges our User entity with Spring Security.
 *
 * Spring Security calls loadUserByUsername() during login form processing.
 * We load the User from DB, map its role to a GrantedAuthority,
 * and return a UserDetails object that Spring Security uses to verify
 * the submitted password (via BCryptPasswordEncoder).
 *
 * Role mapping:
 *   DB role "ADMIN"   → GrantedAuthority "ROLE_ADMIN"
 *   DB role "STUDENT" → GrantedAuthority "ROLE_STUDENT"
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Login attempt with unknown username: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            log.warn("Login attempt by inactive user: {}", username);
            throw new UsernameNotFoundException("Account is inactive: " + username);
        }

        String role = "ROLE_" + user.getRole().toUpperCase();
        log.info("Authenticated user '{}' with role '{}'", username, role);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}
