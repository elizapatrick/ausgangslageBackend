package com.ausganslage.ausgangslageBackend.exception;

/**
 * Exception thrown when login credentials are invalid
 * Extends AppException for specific authentication error handling
 */
public class InvalidCredentialsException extends AppException {
    public InvalidCredentialsException(String message) {
        super(message, "INVALID_CREDENTIALS");
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, "INVALID_CREDENTIALS", cause);
    }
}
