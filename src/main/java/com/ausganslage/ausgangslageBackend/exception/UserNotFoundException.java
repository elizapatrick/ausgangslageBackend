package com.ausganslage.ausgangslageBackend.exception;

/**
 * Exception thrown when a user is not found
 * Extends AppException for specific user resource errors
 */
public class UserNotFoundException extends AppException {
    public UserNotFoundException(String message) {
        super(message, "USER_NOT_FOUND");
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, "USER_NOT_FOUND", cause);
    }
}
