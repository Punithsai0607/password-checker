package com.passwordchecker.dto;

import com.passwordchecker.model.PasswordStrength;
import com.passwordchecker.model.ValidationRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object representing the complete password validation report.
 *
 * <p>Contains validation results, score, strength classification,
 * and improvement suggestions. This is the primary output produced
 * by the service layer and consumed by the Thymeleaf template.</p>
 *
 * @author Password Checker Team
 * @version 1.0.0
 */
public class PasswordReport {

    private List<ValidationRule> validationRules;
    private int score;
    private PasswordStrength strength;
    private List<String> suggestions;
    private boolean valid;
    private int totalRules;
    private int passedRules;
    private int failedRules;

    /**
     * Default constructor initializing empty collections.
     */
    public PasswordReport() {
        this.validationRules = new ArrayList<>();
        this.suggestions = new ArrayList<>();
        this.score = 0;
        this.strength = PasswordStrength.WEAK;
        this.valid = false;
        this.totalRules = 0;
        this.passedRules = 0;
        this.failedRules = 0;
    }

    /**
     * Returns the list of individual validation rule results.
     *
     * @return the validation rules
     */
    public List<ValidationRule> getValidationRules() {
        return validationRules;
    }

    /**
     * Sets the list of validation rule results.
     *
     * @param validationRules the rules to set
     */
    public void setValidationRules(List<ValidationRule> validationRules) {
        this.validationRules = validationRules;
    }

    /**
     * Returns the password score (0-100).
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the password score.
     *
     * @param score the score to set (0-100)
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns the password strength classification.
     *
     * @return the strength enum value
     */
    public PasswordStrength getStrength() {
        return strength;
    }

    /**
     * Sets the password strength classification.
     *
     * @param strength the strength to set
     */
    public void setStrength(PasswordStrength strength) {
        this.strength = strength;
    }

    /**
     * Returns the list of improvement suggestions.
     *
     * @return the suggestions
     */
    public List<String> getSuggestions() {
        return suggestions;
    }

    /**
     * Sets the list of improvement suggestions.
     *
     * @param suggestions the suggestions to set
     */
    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    /**
     * Returns whether the password passed all validation rules.
     *
     * @return {@code true} if all rules passed
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Sets whether the password is valid overall.
     *
     * @param valid validity flag
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * Returns the total number of rules evaluated.
     *
     * @return the total rule count
     */
    public int getTotalRules() {
        return totalRules;
    }

    /**
     * Sets the total number of rules evaluated.
     *
     * @param totalRules the total count
     */
    public void setTotalRules(int totalRules) {
        this.totalRules = totalRules;
    }

    /**
     * Returns the number of rules that passed.
     *
     * @return the passed rule count
     */
    public int getPassedRules() {
        return passedRules;
    }

    /**
     * Sets the number of rules that passed.
     *
     * @param passedRules the passed count
     */
    public void setPassedRules(int passedRules) {
        this.passedRules = passedRules;
    }

    /**
     * Returns the number of rules that failed.
     *
     * @return the failed rule count
     */
    public int getFailedRules() {
        return failedRules;
    }

    /**
     * Sets the number of rules that failed.
     *
     * @param failedRules the failed count
     */
    public void setFailedRules(int failedRules) {
        this.failedRules = failedRules;
    }

    @Override
    public String toString() {
        return String.format(
                "PasswordReport{score=%d, strength=%s, valid=%s, passed=%d/%d, suggestions=%d}",
                score, strength.getLabel(), valid, passedRules, totalRules, suggestions.size());
    }
}
