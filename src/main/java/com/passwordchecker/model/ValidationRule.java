package com.passwordchecker.model;

/**
 * Represents a single password validation rule and its outcome.
 *
 * <p>Each instance captures the rule name, whether the password passed
 * that rule, and a descriptive message explaining the result.</p>
 *
 * @author Password Checker Team
 * @version 1.0.0
 */
public class ValidationRule {

    private final String ruleName;
    private final boolean passed;
    private final String message;

    /**
     * Constructs a ValidationRule with all fields.
     *
     * @param ruleName the name of the validation rule
     * @param passed   {@code true} if the password passed this rule
     * @param message  descriptive message about the rule outcome
     */
    public ValidationRule(String ruleName, boolean passed, String message) {
        this.ruleName = ruleName;
        this.passed = passed;
        this.message = message;
    }

    /**
     * Returns the name of the validation rule.
     *
     * @return the rule name
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * Returns whether the password passed this rule.
     *
     * @return {@code true} if passed, {@code false} otherwise
     */
    public boolean isPassed() {
        return passed;
    }

    /**
     * Returns the descriptive message about the rule outcome.
     *
     * @return the result message
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("ValidationRule{ruleName='%s', passed=%s, message='%s'}",
                ruleName, passed, message);
    }
}
