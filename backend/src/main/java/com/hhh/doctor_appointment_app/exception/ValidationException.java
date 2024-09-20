package com.hhh.doctor_appointment_app.exception;
import lombok.Getter;

import java.util.Map;
@Getter
public class ValidationException extends RuntimeException{
    public ValidationException(String message) {
        super(message);
    }

    private Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        super("Validation failed");
        this.errors = errors;
    }
}
