package com.passwordchecker.service;

import com.passwordchecker.dto.PasswordReport;

/**
 * Service interface for password validation operations.
 *
 * <p>Defines the contract for validating passwords and generating
 * comprehensive reports. Implementations are managed by the Spring
 * IoC container and injected into the controller layer.</p>
 *
 * <p>This interface supports the Dependency Inversion Principle (DIP)
 * by decoupling the controller from the concrete service implementation.</p>
 *
 * @author Password Checker Team
 * @version 1.0.0
 */
public interface PasswordService {

    /**
     * Validates the given password against all configured security rules
     * and returns a comprehensive report.
     *
     * @param password the password to validate (must not be null or empty)
     * @param username the username to check against (may be null or empty)
     * @return a {@link PasswordReport} containing validation results, score,
     *         strength classification, and improvement suggestions
     * @throws com.passwordchecker.exception.PasswordValidationException
     *         if the password is null or empty
     */
    PasswordReport validatePassword(String password, String username);
}
