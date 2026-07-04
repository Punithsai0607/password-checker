package com.passwordchecker.util;

import com.passwordchecker.model.ValidationRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Core validation engine that evaluates a password against a comprehensive
 * set of security rules.
 *
 * <p>Supports both <strong>basic</strong> rules (length, character classes) and
 * <strong>advanced</strong> rules (username detection, common passwords,
 * sequential/repeated/consecutive characters, whitespace).</p>
 *
 * <p>This class is instantiated as a Spring bean by {@link com.passwordchecker.config.AppConfig}
 * and injected into the service layer via constructor injection.</p>
 *
 * @author Password Checker Team
 * @version 1.0.0
 */
public class PasswordValidator {

    private final int minLength;
    private final int maxLength;
    private final int maxRepeatedChars;
    private final int maxSequentialChars;
    private final CommonPasswordChecker commonPasswordChecker;

    /**
     * Constructs a PasswordValidator with the given policy parameters.
     *
     * @param minLength             minimum required password length
     * @param maxLength             maximum allowed password length
     * @param maxRepeatedChars      maximum allowed consecutive repeated characters
     * @param maxSequentialChars    maximum allowed sequential characters (abc, 123)
     * @param commonPasswordChecker the common password checker dependency
     */
    public PasswordValidator(int minLength, int maxLength, int maxRepeatedChars,
                             int maxSequentialChars, CommonPasswordChecker commonPasswordChecker) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.maxRepeatedChars = maxRepeatedChars;
        this.maxSequentialChars = maxSequentialChars;
        this.commonPasswordChecker = commonPasswordChecker;
    }

    /**
     * Validates the password against all configured rules.
     *
     * @param password the password to validate
     * @param username the username to check against (may be null or empty)
     * @return a list of {@link ValidationRule} results
     */
    public List<ValidationRule> validate(String password, String username) {
        List<ValidationRule> rules = new ArrayList<>();

        // Basic Validation
        rules.add(checkMinLength(password));
        rules.add(checkMaxLength(password));
        rules.add(checkUppercase(password));
        rules.add(checkLowercase(password));
        rules.add(checkDigit(password));
        rules.add(checkSpecialCharacter(password));

        // Advanced Validation
        rules.add(checkUsernameDetection(password, username));
        rules.add(checkCommonPassword(password));
        rules.add(checkSequentialCharacters(password));
        rules.add(checkRepeatedCharacters(password));
        rules.add(checkConsecutiveCharacters(password));
        rules.add(checkWhitespace(password));

        return rules;
    }

    // ==================== BASIC VALIDATION ====================

    /**
     * Checks that the password meets the minimum length requirement.
     *
     * @param password the password to check
     * @return the validation rule result
     */
    private ValidationRule checkMinLength(String password) {
        boolean passed = password.length() >= minLength;
        String message = passed
                ? String.format("Password meets minimum length of %d characters", minLength)
                : String.format("Password must be at least %d characters (currently %d)",
                        minLength, password.length());
        return new ValidationRule("Minimum Length", passed, message);
    }

    /**
     * Checks that the password does not exceed the maximum length.
     *
     * @param password the password to check
     * @return the validation rule result
     */
    private ValidationRule checkMaxLength(String password) {
        boolean passed = password.length() <= maxLength;
        String message = passed
                ? String.format("Password is within maximum length of %d characters", maxLength)
                : String.format("Password must not exceed %d characters (currently %d)",
                        maxLength, password.length());
        return new ValidationRule("Maximum Length", passed, message);
    }

    /**
     * Checks that the password contains at least one uppercase letter.
     *
     * @param password the password to check
     * @return the validation rule result
     */
    private ValidationRule checkUppercase(String password) {
        boolean passed = password.chars().anyMatch(Character::isUpperCase);
        String message = passed
                ? "Password contains an uppercase letter"
                : "Password must contain at least one uppercase letter (A-Z)";
        return new ValidationRule("Uppercase Letter", passed, message);
    }

    /**
     * Checks that the password contains at least one lowercase letter.
     *
     * @param password the password to check
     * @return the validation rule result
     */
    private ValidationRule checkLowercase(String password) {
        boolean passed = password.chars().anyMatch(Character::isLowerCase);
        String message = passed
                ? "Password contains a lowercase letter"
                : "Password must contain at least one lowercase letter (a-z)";
        return new ValidationRule("Lowercase Letter", passed, message);
    }

    /**
     * Checks that the password contains at least one digit.
     *
     * @param password the password to check
     * @return the validation rule result
     */
    private ValidationRule checkDigit(String password) {
        boolean passed = password.chars().anyMatch(Character::isDigit);
        String message = passed
                ? "Password contains a digit"
                : "Password must contain at least one digit (0-9)";
        return new ValidationRule("Digit", passed, message);
    }

    /**
     * Checks that the password contains at least one special character.
     *
     * @param password the password to check
     * @return the validation rule result
     */
    private ValidationRule checkSpecialCharacter(String password) {
        boolean passed = password.chars().anyMatch(ch ->
                !Character.isLetterOrDigit(ch) && !Character.isWhitespace(ch));
        String message = passed
                ? "Password contains a special character"
                : "Password must contain at least one special character (!@#$%^&*...)";
        return new ValidationRule("Special Character", passed, message);
    }

    // ==================== ADVANCED VALIDATION ====================

    /**
     * Checks whether the password contains the username.
     *
     * @param password the password to check
     * @param username the username to detect
     * @return the validation rule result
     */
    private ValidationRule checkUsernameDetection(String password, String username) {
        if (username == null || username.trim().isEmpty()) {
            return new ValidationRule("Username Detection", true,
                    "No username provided — skipping username detection");
        }

        boolean containsUsername = password.toLowerCase().contains(username.toLowerCase().trim());
        boolean passed = !containsUsername;
        String message = passed
                ? "Password does not contain the username"
                : "Password must not contain your username";
        return new ValidationRule("Username Detection", passed, message);
    }

    /**
     * Checks whether the password is a commonly used password.
     *
     * @param password the password to check
     * @return the validation rule result
     */
    private ValidationRule checkCommonPassword(String password) {
        boolean isCommon = commonPasswordChecker.isCommonPassword(password);
        boolean passed = !isCommon;
        String message = passed
                ? "Password is not a commonly used password"
                : "Password is too common — please choose a more unique password";
        return new ValidationRule("Common Password", passed, message);
    }

    /**
     * Checks for sequential characters (e.g., abc, 123, xyz).
     *
     * @param password the password to check
     * @return the validation rule result
     */
    private ValidationRule checkSequentialCharacters(String password) {
        boolean hasSequential = containsSequentialCharacters(password, maxSequentialChars);
        boolean passed = !hasSequential;
        String message = passed
                ? "Password does not contain sequential characters"
                : String.format("Password must not contain %d or more sequential characters (e.g., abc, 123)",
                        maxSequentialChars);
        return new ValidationRule("Sequential Characters", passed, message);
    }

    /**
     * Checks for repeated characters (e.g., aaa, 111).
     *
     * @param password the password to check
     * @return the validation rule result
     */
    private ValidationRule checkRepeatedCharacters(String password) {
        boolean hasRepeated = containsRepeatedCharacters(password, maxRepeatedChars);
        boolean passed = !hasRepeated;
        String message = passed
                ? "Password does not contain excessive repeated characters"
                : String.format("Password must not contain %d or more repeated characters in a row (e.g., aaa)",
                        maxRepeatedChars);
        return new ValidationRule("Repeated Characters", passed, message);
    }

    /**
     * Checks for consecutive identical character pairs (e.g., aabb).
     *
     * @param password the password to check
     * @return the validation rule result
     */
    private ValidationRule checkConsecutiveCharacters(String password) {
        boolean hasConsecutive = containsConsecutivePairs(password);
        boolean passed = !hasConsecutive;
        String message = passed
                ? "Password does not contain excessive consecutive character pairs"
                : "Password should avoid multiple consecutive character pairs (e.g., aabb)";
        return new ValidationRule("Consecutive Characters", passed, message);
    }

    /**
     * Checks that the password does not contain whitespace characters.
     *
     * @param password the password to check
     * @return the validation rule result
     */
    private ValidationRule checkWhitespace(String password) {
        boolean hasWhitespace = password.chars().anyMatch(Character::isWhitespace);
        boolean passed = !hasWhitespace;
        String message = passed
                ? "Password does not contain whitespace"
                : "Password must not contain spaces or whitespace characters";
        return new ValidationRule("Whitespace Detection", passed, message);
    }

    // ==================== HELPER METHODS ====================

    /**
     * Detects whether the password contains a run of sequential characters
     * (ascending or descending) of the given threshold length.
     *
     * @param password  the password to scan
     * @param threshold the minimum run length to flag
     * @return {@code true} if a sequential run is found
     */
    private boolean containsSequentialCharacters(String password, int threshold) {
        if (password.length() < threshold) {
            return false;
        }

        String lower = password.toLowerCase();
        int ascCount = 1;
        int descCount = 1;

        for (int i = 1; i < lower.length(); i++) {
            char prev = lower.charAt(i - 1);
            char curr = lower.charAt(i);

            if (curr == prev + 1) {
                ascCount++;
                descCount = 1;
            } else if (curr == prev - 1) {
                descCount++;
                ascCount = 1;
            } else {
                ascCount = 1;
                descCount = 1;
            }

            if (ascCount >= threshold || descCount >= threshold) {
                return true;
            }
        }

        return false;
    }

    /**
     * Detects whether the password contains a run of identical characters
     * of the given threshold length.
     *
     * @param password  the password to scan
     * @param threshold the minimum run length to flag
     * @return {@code true} if a repeated run is found
     */
    private boolean containsRepeatedCharacters(String password, int threshold) {
        if (password.length() < threshold) {
            return false;
        }

        int count = 1;
        for (int i = 1; i < password.length(); i++) {
            if (password.charAt(i) == password.charAt(i - 1)) {
                count++;
                if (count >= threshold) {
                    return true;
                }
            } else {
                count = 1;
            }
        }

        return false;
    }

    /**
     * Detects whether the password contains three or more consecutive
     * character pairs (e.g., {@code aabbcc}).
     *
     * @param password the password to scan
     * @return {@code true} if consecutive pairs are detected
     */
    private boolean containsConsecutivePairs(String password) {
        if (password.length() < 6) {
            return false;
        }

        int pairCount = 0;
        for (int i = 0; i < password.length() - 1; i += 2) {
            if (password.charAt(i) == password.charAt(i + 1)) {
                pairCount++;
                if (pairCount >= 3) {
                    return true;
                }
            } else {
                pairCount = 0;
            }
        }

        return false;
    }
}
