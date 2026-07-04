package com.passwordchecker.model;

/**
 * Enumeration representing the strength levels of a password.
 *
 * <p>Each strength level maps to a score range, a CSS class for visual
 * styling, and a descriptive label displayed in the UI.</p>
 *
 * @author Password Checker Team
 * @version 1.0.0
 */
public enum PasswordStrength {

    /** Score 0-25: Password is easily guessable. */
    WEAK("Weak", "danger", 0),

    /** Score 26-50: Password meets minimal requirements. */
    MEDIUM("Medium", "warning", 25),

    /** Score 51-75: Password is reasonably secure. */
    STRONG("Strong", "info", 50),

    /** Score 76-100: Password is highly secure. */
    VERY_STRONG("Very Strong", "success", 75);

    private final String label;
    private final String cssClass;
    private final int minScore;

    /**
     * Constructs a PasswordStrength enum constant.
     *
     * @param label    human-readable label
     * @param cssClass Bootstrap CSS class for the strength badge
     * @param minScore minimum score threshold for this level
     */
    PasswordStrength(String label, String cssClass, int minScore) {
        this.label = label;
        this.cssClass = cssClass;
        this.minScore = minScore;
    }

    /**
     * Returns the human-readable label.
     *
     * @return the strength label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the Bootstrap CSS class for badge styling.
     *
     * @return the CSS class name
     */
    public String getCssClass() {
        return cssClass;
    }

    /**
     * Returns the minimum score threshold.
     *
     * @return the minimum score
     */
    public int getMinScore() {
        return minScore;
    }

    /**
     * Determines the password strength based on a numeric score.
     *
     * @param score the password score (0-100)
     * @return the corresponding {@link PasswordStrength}
     */
    public static PasswordStrength fromScore(int score) {
        if (score >= 76) {
            return VERY_STRONG;
        } else if (score >= 51) {
            return STRONG;
        } else if (score >= 26) {
            return MEDIUM;
        } else {
            return WEAK;
        }
    }
}
