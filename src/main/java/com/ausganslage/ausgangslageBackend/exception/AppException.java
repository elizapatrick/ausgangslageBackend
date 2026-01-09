package com.ausganslage.ausgangslageBackend.exception;

public class AppException extends Exception {
    private String errorCode;

    public AppException(String message) {
        super(message);
        this.errorCode = "GENERAL_ERROR";
    }

    public AppException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "GENERAL_ERROR";
    }

    public AppException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
