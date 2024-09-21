package com.hhh.doctor_appointment_app.exception;

import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AuthenticationResponse;

public class ApplicationException extends RuntimeException{

    public ApplicationException(String message) {
        super(message);
    }
}
