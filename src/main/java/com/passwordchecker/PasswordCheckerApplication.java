package com.passwordchecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Password Strength and Policy Validator application.
 *
 * <p>This Spring Boot application provides a comprehensive password validation
 * engine that checks passwords against multiple security rules, calculates a
 * strength score, and generates actionable improvement suggestions.</p>
 *
 * @author Password Checker Team
 * @version 1.0.0
 */
@SpringBootApplication
public class PasswordCheckerApplication {

    /**
     * Bootstraps the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(PasswordCheckerApplication.class, args);
    }
}
