package com.hhh.doctor_appointment_app.dto.request.PatientRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AddPatientRequest {
    @Size(max = 50, message = "Full name must not exceed 50 characters")
    private String fullname;

    private boolean gender;

    private String phone;

    @Email(message = "Invalid Email")
    private String email;

    private Date dateOfBirth;

    private String address;

}
