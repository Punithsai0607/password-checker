package com.passwordchecker.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.passwordchecker.dto.PasswordRequest;

/**
 * Global exception handler that intercepts exceptions thrown by controllers
 * and returns user-friendly error pages via Thymeleaf.
 *
 * <p>Handles {@link PasswordValidationException} and generic
 * {@link Exception} types, logging the error and populating the
 * model with error details for the UI.</p>
 *
 * @author Password Checker Team
 * @version 1.0.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles {@link PasswordValidationException} instances.
     *
     * @param ex    the thrown exception
     * @param model the Spring MVC model
     * @return the view name to render
     */
    @ExceptionHandler(PasswordValidationException.class)
    public String handlePasswordValidationException(PasswordValidationException ex, Model model) {
        logger.error("Password validation error [{}]: {}", ex.getErrorCode(), ex.getMessage());

        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", ex.getErrorCode());
        model.addAttribute("passwordRequest", new PasswordRequest());

        return "index";
    }

    /**
     * Handles {@link IllegalArgumentException} instances.
     *
     * @param ex    the thrown exception
     * @param model the Spring MVC model
     * @return the view name to render
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        logger.error("Illegal argument: {}", ex.getMessage());

        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", "INVALID_INPUT");
        model.addAttribute("passwordRequest", new PasswordRequest());

        return "index";
    }

    /**
     * Catch-all handler for unexpected exceptions.
     *
     * @param ex    the thrown exception
     * @param model the Spring MVC model
     * @return the error view name
     */
    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);

        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again.");
        model.addAttribute("errorCode", "INTERNAL_ERROR");
        model.addAttribute("passwordRequest", new PasswordRequest());

        return "error";
    }
}
