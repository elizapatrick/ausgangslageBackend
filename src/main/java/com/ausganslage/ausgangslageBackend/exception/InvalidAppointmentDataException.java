package com.ausganslage.ausgangslageBackend.exception;

public class InvalidAppointmentDataException extends AppException {
    public InvalidAppointmentDataException(String message) {
        super(message, "INVALID_APPOINTMENT_DATA");
    }

    public InvalidAppointmentDataException(String message, Throwable cause) {
        super(message, "INVALID_APPOINTMENT_DATA", cause);
    }
}
