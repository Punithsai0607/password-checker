package com.passwordchecker.service;

import com.passwordchecker.dto.PasswordReport;
import com.passwordchecker.exception.PasswordValidationException;
import com.passwordchecker.model.PasswordStrength;
import com.passwordchecker.model.ValidationRule;
import com.passwordchecker.service.impl.PasswordServiceImpl;
import com.passwordchecker.util.CommonPasswordChecker;
import com.passwordchecker.util.PasswordScorer;
import com.passwordchecker.util.PasswordValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive JUnit 5 test suite for the password validation service.
 *
 * <p>Tests every validation rule, scoring behaviour, strength classification,
 * and exception handling path.</p>
 *
 * @author Password Checker Team
 * @version 1.0.0
 */
class PasswordServiceImplTest {

    private PasswordService passwordService;

    @BeforeEach
    void setUp() {
        CommonPasswordChecker commonPasswordChecker = new CommonPasswordChecker();
        PasswordValidator passwordValidator = new PasswordValidator(
                8, 128, 3, 3, commonPasswordChecker);
        PasswordScorer passwordScorer = new PasswordScorer();
        passwordService = new PasswordServiceImpl(passwordValidator, passwordScorer);
    }

    // ==================== HELPER ====================

    /**
     * Finds a specific rule result by name from the report.
     */
    private Optional<ValidationRule> findRule(PasswordReport report, String ruleName) {
        return report.getValidationRules().stream()
                .filter(r -> r.getRuleName().equals(ruleName))
                .findFirst();
    }

    // ==================== EXCEPTION HANDLING ====================

    @Nested
    @DisplayName("Exception Handling Tests")
    class ExceptionHandlingTests {

        @Test
        @DisplayName("Null password throws PasswordValidationException")
        void nullPasswordThrowsException() {
            PasswordValidationException ex = assertThrows(
                    PasswordValidationException.class,
                    () -> passwordService.validatePassword(null, "user"));
            assertEquals("NULL_PASSWORD", ex.getErrorCode());
        }

        @Test
        @DisplayName("Empty password throws PasswordValidationException")
        void emptyPasswordThrowsException() {
            PasswordValidationException ex = assertThrows(
                    PasswordValidationException.class,
                    () -> passwordService.validatePassword("", "user"));
            assertEquals("EMPTY_PASSWORD", ex.getErrorCode());
        }
    }

    // ==================== BASIC VALIDATION RULES ====================

    @Nested
    @DisplayName("Minimum Length Rule")
    class MinLengthTests {

        @Test
        @DisplayName("Password shorter than 8 characters fails minimum length")
        void shortPasswordFails() {
            PasswordReport report = passwordService.validatePassword("Ab1!", null);
            ValidationRule rule = findRule(report, "Minimum Length").orElseThrow();
            assertFalse(rule.isPassed());
        }

        @Test
        @DisplayName("Password of exactly 8 characters passes minimum length")
        void exactMinLengthPasses() {
            PasswordReport report = passwordService.validatePassword("Abcdef1!", null);
            ValidationRule rule = findRule(report, "Minimum Length").orElseThrow();
            assertTrue(rule.isPassed());
        }

        @Test
        @DisplayName("Password longer than 8 characters passes minimum length")
        void longPasswordPasses() {
            PasswordReport report = passwordService.validatePassword("Abcdefgh1!xy", null);
            ValidationRule rule = findRule(report, "Minimum Length").orElseThrow();
            assertTrue(rule.isPassed());
        }
    }

    @Nested
    @DisplayName("Maximum Length Rule")
    class MaxLengthTests {

        @Test
        @DisplayName("Password within 128 characters passes maximum length")
        void normalLengthPasses() {
            PasswordReport report = passwordService.validatePassword("Abcdef1!", null);
            ValidationRule rule = findRule(report, "Maximum Length").orElseThrow();
            assertTrue(rule.isPassed());
        }

        @Test
        @DisplayName("Password exceeding 128 characters fails maximum length")
        void excessiveLengthFails() {
            String longPassword = "A".repeat(129) + "a1!";
            PasswordReport report = passwordService.validatePassword(longPassword, null);
            ValidationRule rule = findRule(report, "Maximum Length").orElseThrow();
            assertFalse(rule.isPassed());
        }
    }

    @Nested
    @DisplayName("Uppercase Letter Rule")
    class UppercaseTests {

        @Test
        @DisplayName("Password with uppercase passes")
        void withUppercasePasses() {
            PasswordReport report = passwordService.validatePassword("Abcdef1!", null);
            ValidationRule rule = findRule(report, "Uppercase Letter").orElseThrow();
            assertTrue(rule.isPassed());
        }

        @Test
        @DisplayName("Password without uppercase fails")
        void withoutUppercaseFails() {
            PasswordReport report = passwordService.validatePassword("abcdef1!", null);
            ValidationRule rule = findRule(report, "Uppercase Letter").orElseThrow();
            assertFalse(rule.isPassed());
        }
    }

    @Nested
    @DisplayName("Lowercase Letter Rule")
    class LowercaseTests {

        @Test
        @DisplayName("Password with lowercase passes")
        void withLowercasePasses() {
            PasswordReport report = passwordService.validatePassword("Abcdef1!", null);
            ValidationRule rule = findRule(report, "Lowercase Letter").orElseThrow();
            assertTrue(rule.isPassed());
        }

        @Test
        @DisplayName("Password without lowercase fails")
        void withoutLowercaseFails() {
            PasswordReport report = passwordService.validatePassword("ABCDEF1!", null);
            ValidationRule rule = findRule(report, "Lowercase Letter").orElseThrow();
            assertFalse(rule.isPassed());
        }
    }

    @Nested
    @DisplayName("Digit Rule")
    class DigitTests {

        @Test
        @DisplayName("Password with digit passes")
        void withDigitPasses() {
            PasswordReport report = passwordService.validatePassword("Abcdefg1!", null);
            ValidationRule rule = findRule(report, "Digit").orElseThrow();
            assertTrue(rule.isPassed());
        }

        @Test
        @DisplayName("Password without digit fails")
        void withoutDigitFails() {
            PasswordReport report = passwordService.validatePassword("Abcdefgh!", null);
            ValidationRule rule = findRule(report, "Digit").orElseThrow();
            assertFalse(rule.isPassed());
        }
    }

    @Nested
    @DisplayName("Special Character Rule")
    class SpecialCharacterTests {

        @Test
        @DisplayName("Password with special character passes")
        void withSpecialPasses() {
            PasswordReport report = passwordService.validatePassword("Abcdefg1!", null);
            ValidationRule rule = findRule(report, "Special Character").orElseThrow();
            assertTrue(rule.isPassed());
        }

        @Test
        @DisplayName("Password without special character fails")
        void withoutSpecialFails() {
            PasswordReport report = passwordService.validatePassword("Abcdefg12", null);
            ValidationRule rule = findRule(report, "Special Character").orElseThrow();
            assertFalse(rule.isPassed());
        }
    }

    // ==================== ADVANCED VALIDATION RULES ====================

    @Nested
    @DisplayName("Username Detection Rule")
    class UsernameDetectionTests {

        @Test
        @DisplayName("Password containing username fails")
        void containsUsernameFails() {
            PasswordReport report = passwordService.validatePassword("myJohn123!", "john");
            ValidationRule rule = findRule(report, "Username Detection").orElseThrow();
            assertFalse(rule.isPassed());
        }

        @Test
        @DisplayName("Password not containing username passes")
        void doesNotContainUsernamePasses() {
            PasswordReport report = passwordService.validatePassword("Secure@123", "john");
            ValidationRule rule = findRule(report, "Username Detection").orElseThrow();
            assertTrue(rule.isPassed());
        }

        @Test
        @DisplayName("Null username skips detection and passes")
        void nullUsernameSkips() {
            PasswordReport report = passwordService.validatePassword("Abcdef1!", null);
            ValidationRule rule = findRule(report, "Username Detection").orElseThrow();
            assertTrue(rule.isPassed());
        }

        @Test
        @DisplayName("Empty username skips detection and passes")
        void emptyUsernameSkips() {
            PasswordReport report = passwordService.validatePassword("Abcdef1!", "");
            ValidationRule rule = findRule(report, "Username Detection").orElseThrow();
            assertTrue(rule.isPassed());
        }
    }

    @Nested
    @DisplayName("Common Password Detection Rule")
    class CommonPasswordTests {

        @Test
        @DisplayName("Common password 'password' fails")
        void commonPasswordFails() {
            PasswordReport report = passwordService.validatePassword("password", null);
            ValidationRule rule = findRule(report, "Common Password").orElseThrow();
            assertFalse(rule.isPassed());
        }

        @Test
        @DisplayName("Common password '123456' fails")
        void numericCommonPasswordFails() {
            PasswordReport report = passwordService.validatePassword("123456", null);
            ValidationRule rule = findRule(report, "Common Password").orElseThrow();
            assertFalse(rule.isPassed());
        }

        @Test
        @DisplayName("Unique password passes")
        void uniquePasswordPasses() {
            PasswordReport report = passwordService.validatePassword("X#k9mPq!vR2z", null);
            ValidationRule rule = findRule(report, "Common Password").orElseThrow();
            assertTrue(rule.isPassed());
        }
    }

    @Nested
    @DisplayName("Sequential Characters Rule")
    class SequentialCharactersTests {

        @Test
        @DisplayName("Password with 'abc' fails sequential check")
        void sequentialLettersFails() {
            PasswordReport report = passwordService.validatePassword("Xabc!456Z", null);
            ValidationRule rule = findRule(report, "Sequential Characters").orElseThrow();
            assertFalse(rule.isPassed());
        }

        @Test
        @DisplayName("Password with '123' fails sequential check")
        void sequentialDigitsFails() {
            PasswordReport report = passwordService.validatePassword("Xdef!123Z", null);
            ValidationRule rule = findRule(report, "Sequential Characters").orElseThrow();
            assertFalse(rule.isPassed());
        }

        @Test
        @DisplayName("Password without sequential characters passes")
        void noSequentialPasses() {
            PasswordReport report = passwordService.validatePassword("Xk!9wPzG", null);
            ValidationRule rule = findRule(report, "Sequential Characters").orElseThrow();
            assertTrue(rule.isPassed());
        }
    }

    @Nested
    @DisplayName("Repeated Characters Rule")
    class RepeatedCharactersTests {

        @Test
        @DisplayName("Password with 'aaa' fails repeated check")
        void repeatedCharsFails() {
            PasswordReport report = passwordService.validatePassword("Xaaa!45Z", null);
            ValidationRule rule = findRule(report, "Repeated Characters").orElseThrow();
            assertFalse(rule.isPassed());
        }

        @Test
        @DisplayName("Password without repeated characters passes")
        void noRepeatedPasses() {
            PasswordReport report = passwordService.validatePassword("Xk!9wPzG", null);
            ValidationRule rule = findRule(report, "Repeated Characters").orElseThrow();
            assertTrue(rule.isPassed());
        }
    }

    @Nested
    @DisplayName("Consecutive Characters Rule")
    class ConsecutiveCharactersTests {

        @Test
        @DisplayName("Password with 'aabbcc' pattern fails")
        void consecutivePairsFails() {
            PasswordReport report = passwordService.validatePassword("aabbccX1!", null);
            ValidationRule rule = findRule(report, "Consecutive Characters").orElseThrow();
            assertFalse(rule.isPassed());
        }

        @Test
        @DisplayName("Password without consecutive pairs passes")
        void noConsecutivePasses() {
            PasswordReport report = passwordService.validatePassword("Xk!9wPzG", null);
            ValidationRule rule = findRule(report, "Consecutive Characters").orElseThrow();
            assertTrue(rule.isPassed());
        }
    }

    @Nested
    @DisplayName("Whitespace Detection Rule")
    class WhitespaceTests {

        @Test
        @DisplayName("Password with space fails")
        void whitespaceFailsRule() {
            PasswordReport report = passwordService.validatePassword("Pass word1!", null);
            ValidationRule rule = findRule(report, "Whitespace Detection").orElseThrow();
            assertFalse(rule.isPassed());
        }

        @Test
        @DisplayName("Password without whitespace passes")
        void noWhitespacePasses() {
            PasswordReport report = passwordService.validatePassword("Password1!", null);
            ValidationRule rule = findRule(report, "Whitespace Detection").orElseThrow();
            assertTrue(rule.isPassed());
        }
    }

    // ==================== PASSWORD SCORING ====================

    @Nested
    @DisplayName("Password Scoring Tests")
    class ScoringTests {

        @Test
        @DisplayName("Strong password scores above 50")
        void strongPasswordScoresHigh() {
            PasswordReport report = passwordService.validatePassword("Str0ng!P@ssW0rd", null);
            assertTrue(report.getScore() > 50, "Score should be > 50, was: " + report.getScore());
        }

        @Test
        @DisplayName("Weak password scores below 50")
        void weakPasswordScoresLow() {
            PasswordReport report = passwordService.validatePassword("abc", null);
            assertTrue(report.getScore() < 50, "Score should be < 50, was: " + report.getScore());
        }

        @Test
        @DisplayName("Score is between 0 and 100 inclusive")
        void scoreWithinRange() {
            PasswordReport report = passwordService.validatePassword("Test!ng1", null);
            assertTrue(report.getScore() >= 0 && report.getScore() <= 100);
        }
    }

    // ==================== PASSWORD STRENGTH ====================

    @Nested
    @DisplayName("Password Strength Classification Tests")
    class StrengthTests {

        @Test
        @DisplayName("Very weak password is not classified as STRONG or VERY_STRONG")
        void weakClassification() {
            PasswordReport report = passwordService.validatePassword("a", null);
            assertNotEquals(PasswordStrength.STRONG, report.getStrength());
            assertNotEquals(PasswordStrength.VERY_STRONG, report.getStrength());
            assertTrue(report.getScore() < 51,
                    "Single char password should score below 51, was: " + report.getScore());
        }

        @Test
        @DisplayName("Strong password not classified as WEAK")
        void strongNotWeak() {
            PasswordReport report = passwordService.validatePassword("V3ry$tr0ng!Pwd#2026", null);
            assertNotEquals(PasswordStrength.WEAK, report.getStrength());
        }
    }

    // ==================== REPORT STRUCTURE ====================

    @Nested
    @DisplayName("Password Report Structure Tests")
    class ReportStructureTests {

        @Test
        @DisplayName("Report contains 12 validation rules")
        void reportContainsTwelveRules() {
            PasswordReport report = passwordService.validatePassword("TestPass1!", null);
            assertEquals(12, report.getTotalRules());
            assertEquals(12, report.getValidationRules().size());
        }

        @Test
        @DisplayName("Passed + Failed equals Total")
        void passedPlusFailedEqualsTotal() {
            PasswordReport report = passwordService.validatePassword("TestPass1!", null);
            assertEquals(report.getTotalRules(), report.getPassedRules() + report.getFailedRules());
        }

        @Test
        @DisplayName("All rules passed means password is valid")
        void allRulesPassedIsValid() {
            // This password should pass all 12 rules
            PasswordReport report = passwordService.validatePassword("Xk!9mPq&Rz7W", null);
            if (report.getFailedRules() == 0) {
                assertTrue(report.isValid());
            }
        }

        @Test
        @DisplayName("Failed rules generate suggestions")
        void failedRulesGenerateSuggestions() {
            PasswordReport report = passwordService.validatePassword("abc", null);
            assertFalse(report.getSuggestions().isEmpty(),
                    "Suggestions should not be empty for a weak password");
        }

        @Test
        @DisplayName("Report has non-null strength")
        void reportHasStrength() {
            PasswordReport report = passwordService.validatePassword("TestPass1!", null);
            assertNotNull(report.getStrength());
        }
    }

    // ==================== EDGE CASES ====================

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Single character password")
        void singleCharPassword() {
            PasswordReport report = passwordService.validatePassword("a", null);
            assertNotNull(report);
            assertFalse(report.isValid());
        }

        @Test
        @DisplayName("All special characters password")
        void allSpecialChars() {
            PasswordReport report = passwordService.validatePassword("!@#$%^&*()_+", null);
            assertNotNull(report);
            // Should fail uppercase, lowercase, digit rules
            ValidationRule upper = findRule(report, "Uppercase Letter").orElseThrow();
            assertFalse(upper.isPassed());
        }

        @Test
        @DisplayName("All digits password")
        void allDigits() {
            PasswordReport report = passwordService.validatePassword("98765432", null);
            assertNotNull(report);
            ValidationRule lower = findRule(report, "Lowercase Letter").orElseThrow();
            assertFalse(lower.isPassed());
        }

        @Test
        @DisplayName("Unicode characters in password")
        void unicodePassword() {
            PasswordReport report = passwordService.validatePassword("Pässwörd1!", null);
            assertNotNull(report);
            assertTrue(report.getScore() >= 0);
        }
    }
}
