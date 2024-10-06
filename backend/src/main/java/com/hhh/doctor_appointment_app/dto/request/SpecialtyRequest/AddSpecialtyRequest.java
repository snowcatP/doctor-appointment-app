package com.hhh.doctor_appointment_app.dto.request.SpecialtyRequest;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddSpecialtyRequest {
    @Size(max = 50, message = "Full name must not exceed 100 characters")
    private String specialtyName;

}