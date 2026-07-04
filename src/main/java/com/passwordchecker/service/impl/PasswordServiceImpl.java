package com.passwordchecker.service.impl;

import com.passwordchecker.dto.PasswordReport;
import com.passwordchecker.exception.PasswordValidationException;
import com.passwordchecker.model.PasswordStrength;
import com.passwordchecker.model.ValidationRule;
import com.passwordchecker.service.PasswordService;
import com.passwordchecker.util.PasswordScorer;
import com.passwordchecker.util.PasswordValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of the {@link PasswordService} interface.
 *
 * <p>Orchestrates the full password validation pipeline:</p>
 * <ol>
 *   <li>Input validation (null/empty checks)</li>
 *   <li>Rule-based validation via {@link PasswordValidator}</li>
 *   <li>Score calculation via {@link PasswordScorer}</li>
 *   <li>Strength classification via {@link PasswordStrength}</li>
 *   <li>Suggestion generation for failed rules</li>
 *   <li>Report assembly into {@link PasswordReport}</li>
 * </ol>
 *
 * <p>All dependencies are injected via constructor injection (no field injection).
 * The class is annotated with {@code @Service} for Spring IoC registration.</p>
 *
 * @author Password Checker Team
 * @version 1.0.0
 */
@Service
public class PasswordServiceImpl implements PasswordService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordServiceImpl.class);

    private final PasswordValidator passwordValidator;
    private final PasswordScorer passwordScorer;

    /**
     * Constructs the service with required dependencies (constructor injection).
     *
     * @param passwordValidator the validation engine
     * @param passwordScorer    the scoring engine
     */
    public PasswordServiceImpl(PasswordValidator passwordValidator,
                                PasswordScorer passwordScorer) {
        this.passwordValidator = passwordValidator;
        this.passwordScorer = passwordScorer;
    }

    /**
     * {@inheritDoc}
     *
     * @throws PasswordValidationException if password is null or empty
     */
    @Override
    public PasswordReport validatePassword(String password, String username) {
        logger.info("Starting password validation for user: {}",
                username != null ? username : "anonymous");

        // Input validation
        validateInput(password);

        // Step 1: Run all validation rules
        List<ValidationRule> validationRules = passwordValidator.validate(password, username);

        // Step 2: Calculate score
        int score = passwordScorer.calculateScore(password, validationRules);

        // Step 3: Determine strength
        PasswordStrength strength = PasswordStrength.fromScore(score);

        // Step 4: Generate suggestions for failed rules
        List<String> suggestions = generateSuggestions(validationRules, password);

        // Step 5: Compute statistics
        long passedCount = validationRules.stream().filter(ValidationRule::isPassed).count();
        long failedCount = validationRules.size() - passedCount;
        boolean isValid = failedCount == 0;

        // Step 6: Assemble report
        PasswordReport report = new PasswordReport();
        report.setValidationRules(validationRules);
        report.setScore(score);
        report.setStrength(strength);
        report.setSuggestions(suggestions);
        report.setValid(isValid);
        report.setTotalRules(validationRules.size());
        report.setPassedRules((int) passedCount);
        report.setFailedRules((int) failedCount);

        logger.info("Validation complete: {}", report);

        return report;
    }

    /**
     * Validates that the password input is not null or empty.
     *
     * @param password the password to check
     * @throws PasswordValidationException if validation fails
     */
    private void validateInput(String password) {
        if (password == null) {
            throw new PasswordValidationException(
                    "Password cannot be null", "NULL_PASSWORD");
        }
        if (password.isEmpty()) {
            throw new PasswordValidationException(
                    "Password cannot be empty", "EMPTY_PASSWORD");
        }
    }

    /**
     * Generates actionable suggestions for every failed validation rule.
     * Also adds bonus tips based on overall password characteristics.
     *
     * @param rules    the validation rule results
     * @param password the original password
     * @return a list of suggestion strings
     */
    private List<String> generateSuggestions(List<ValidationRule> rules, String password) {
        List<String> suggestions = new ArrayList<>();

        for (ValidationRule rule : rules) {
            if (!rule.isPassed()) {
                suggestions.add(generateSuggestionForRule(rule));
            }
        }

        // Bonus tips
        if (password.length() < 12) {
            suggestions.add("💡 Tip: Use 12+ characters for significantly better security.");
        }

        if (password.length() >= 8 && suggestions.isEmpty()) {
            suggestions.add("✅ Your password meets all requirements. Consider using a passphrase for even stronger security.");
        }

        boolean hasAllCategories = password.chars().anyMatch(Character::isUpperCase)
                && password.chars().anyMatch(Character::isLowerCase)
                && password.chars().anyMatch(Character::isDigit)
                && password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch) && !Character.isWhitespace(ch));

        if (!hasAllCategories) {
            suggestions.add("💡 Tip: Mix uppercase, lowercase, digits, and special characters for maximum strength.");
        }

        return suggestions;
    }

    /**
     * Maps a failed validation rule to a specific, actionable suggestion string.
     *
     * @param rule the failed validation rule
     * @return the suggestion text
     */
    private String generateSuggestionForRule(ValidationRule rule) {
        return switch (rule.getRuleName()) {
            case "Minimum Length" ->
                    "🔑 Increase your password length. Longer passwords are exponentially harder to crack.";
            case "Maximum Length" ->
                    "✂️ Shorten your password. Most systems support up to 128 characters.";
            case "Uppercase Letter" ->
                    "🔠 Add at least one uppercase letter (A-Z) to improve complexity.";
            case "Lowercase Letter" ->
                    "🔡 Add at least one lowercase letter (a-z) to improve complexity.";
            case "Digit" ->
                    "🔢 Include at least one number (0-9) in your password.";
            case "Special Character" ->
                    "✨ Add a special character like !@#$%^&* to strengthen your password.";
            case "Username Detection" ->
                    "🚫 Remove your username from the password — it makes it easier to guess.";
            case "Common Password" ->
                    "⚠️ Your password is on a list of commonly used passwords. Choose something unique.";
            case "Sequential Characters" ->
                    "🔄 Avoid sequential characters like 'abc' or '123' — they are easy to predict.";
            case "Repeated Characters" ->
                    "🔁 Avoid repeating the same character multiple times in a row (e.g., 'aaa').";
            case "Consecutive Characters" ->
                    "📝 Avoid patterns like 'aabb' — use more varied character placement.";
            case "Whitespace Detection" ->
                    "⬜ Remove spaces and whitespace characters from your password.";
            default ->
                    "⚙️ " + rule.getMessage();
        };
    }
}
