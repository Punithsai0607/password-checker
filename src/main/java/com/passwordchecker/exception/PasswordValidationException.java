package com.passwordchecker.exception;

/**
 * Custom exception thrown when password validation encounters an
 * unrecoverable error such as a null or empty password input.
 *
 * <p>This exception is handled by the {@link GlobalExceptionHandler}
 * to return user-friendly error messages.</p>
 *
 * @author Password Checker Team
 * @version 1.0.0
 */
public class PasswordValidationException extends RuntimeException {

    private final String errorCode;

    /**
     * Constructs a PasswordValidationException with a message.
     *
     * @param message the error message
     */
    public PasswordValidationException(String message) {
        super(message);
        this.errorCode = "VALIDATION_ERROR";
    }

    /**
     * Constructs a PasswordValidationException with a message and error code.
     *
     * @param message   the error message
     * @param errorCode a short error code for programmatic handling
     */
    public PasswordValidationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a PasswordValidationException with a message, error code,
     * and root cause.
     *
     * @param message   the error message
     * @param errorCode a short error code
     * @param cause     the underlying cause
     */
    public PasswordValidationException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code associated with this exception.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}
