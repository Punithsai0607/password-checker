package com.passwordchecker.util;

import com.passwordchecker.model.ValidationRule;

import java.util.List;

/**
 * Utility component that calculates a numeric password score (0-100)
 * based on multiple weighted criteria.
 *
 * <p>The scoring algorithm considers:</p>
 * <ul>
 *   <li>Validation rule pass rate (40% weight)</li>
 *   <li>Password length bonus (20% weight)</li>
 *   <li>Character variety bonus (20% weight)</li>
 *   <li>Entropy bonus (20% weight)</li>
 * </ul>
 *
 * @author Password Checker Team
 * @version 1.0.0
 */
public class PasswordScorer {

    private static final int MAX_SCORE = 100;
    private static final double RULE_WEIGHT = 0.40;
    private static final double LENGTH_WEIGHT = 0.20;
    private static final double VARIETY_WEIGHT = 0.20;
    private static final double ENTROPY_WEIGHT = 0.20;

    /**
     * Default constructor.
     */
    public PasswordScorer() {
    }

    /**
     * Calculates the overall password score.
     *
     * @param password        the password to score
     * @param validationRules the list of validation rule results
     * @return the score (0-100)
     */
    public int calculateScore(String password, List<ValidationRule> validationRules) {
        if (password == null || password.isEmpty()) {
            return 0;
        }

        double ruleScore = calculateRuleScore(validationRules);
        double lengthScore = calculateLengthScore(password);
        double varietyScore = calculateVarietyScore(password);
        double entropyScore = calculateEntropyScore(password);

        double rawScore = (ruleScore * RULE_WEIGHT)
                + (lengthScore * LENGTH_WEIGHT)
                + (varietyScore * VARIETY_WEIGHT)
                + (entropyScore * ENTROPY_WEIGHT);

        return Math.min(MAX_SCORE, Math.max(0, (int) Math.round(rawScore)));
    }

    /**
     * Calculates the score component based on how many rules passed.
     *
     * @param rules the validation rule results
     * @return score out of 100
     */
    private double calculateRuleScore(List<ValidationRule> rules) {
        if (rules == null || rules.isEmpty()) {
            return 0;
        }

        long passed = rules.stream().filter(ValidationRule::isPassed).count();
        return ((double) passed / rules.size()) * MAX_SCORE;
    }

    /**
     * Calculates the score component based on password length.
     * Rewards longer passwords with diminishing returns.
     *
     * @param password the password
     * @return score out of 100
     */
    private double calculateLengthScore(String password) {
        int length = password.length();

        if (length >= 20) {
            return 100;
        } else if (length >= 16) {
            return 90;
        } else if (length >= 12) {
            return 75;
        } else if (length >= 10) {
            return 60;
        } else if (length >= 8) {
            return 45;
        } else if (length >= 6) {
            return 25;
        } else {
            return Math.max(0, length * 4.0);
        }
    }

    /**
     * Calculates the score component based on character class variety.
     * Checks for lowercase, uppercase, digits, and special characters.
     *
     * @param password the password
     * @return score out of 100
     */
    private double calculateVarietyScore(String password) {
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char ch : password.toCharArray()) {
            if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isDigit(ch)) hasDigit = true;
            else if (!Character.isWhitespace(ch)) hasSpecial = true;
        }

        int categories = 0;
        if (hasLower) categories++;
        if (hasUpper) categories++;
        if (hasDigit) categories++;
        if (hasSpecial) categories++;

        return switch (categories) {
            case 4 -> 100;
            case 3 -> 70;
            case 2 -> 40;
            case 1 -> 15;
            default -> 0;
        };
    }

    /**
     * Calculates the score component based on character uniqueness (entropy proxy).
     * A password with more unique characters relative to its length scores higher.
     *
     * @param password the password
     * @return score out of 100
     */
    private double calculateEntropyScore(String password) {
        long uniqueChars = password.chars().distinct().count();
        int length = password.length();

        if (length == 0) {
            return 0;
        }

        double uniqueRatio = (double) uniqueChars / length;
        double baseScore = uniqueRatio * 70;

        // Bonus for absolute number of unique characters
        double uniqueBonus = Math.min(30, uniqueChars * 2.5);

        return Math.min(MAX_SCORE, baseScore + uniqueBonus);
    }
}
