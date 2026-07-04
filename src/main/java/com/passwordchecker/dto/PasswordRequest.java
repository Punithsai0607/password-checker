package com.passwordchecker.dto;

/**
 * Data Transfer Object for incoming password validation requests.
 *
 * <p>Encapsulates the username and password submitted by the user
 * through the HTML form. Used by the controller to pass data into
 * the service layer.</p>
 *
 * @author Password Checker Team
 * @version 1.0.0
 */
public class PasswordRequest {

    private String username;
    private String password;

    /**
     * Default no-argument constructor required by Spring MVC form binding.
     */
    public PasswordRequest() {
    }

    /**
     * Constructs a PasswordRequest with the given username and password.
     *
     * @param username the username to validate against
     * @param password the password to validate
     */
    public PasswordRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("PasswordRequest{username='%s', password='[PROTECTED]'}", username);
    }
}
