package com.ausganslage.ausgangslageBackend.exception;

/**
 * Exception thrown when an appointment is not found
 * Extends AppException for specific resource not found error handling
 */
public class AppointmentNotFoundException extends AppException {
    public AppointmentNotFoundException(String message) {
        super(message, "APPOINTMENT_NOT_FOUND");
    }

    public AppointmentNotFoundException(String message, Throwable cause) {
        super(message, "APPOINTMENT_NOT_FOUND", cause);
    }
}
