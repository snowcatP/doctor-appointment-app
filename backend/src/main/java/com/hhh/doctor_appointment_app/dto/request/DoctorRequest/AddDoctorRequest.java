package com.hhh.doctor_appointment_app.dto.request.DoctorRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AddDoctorRequest {
    @Size(max = 50, message = "Firstname must not exceed 50 characters")
    private String firstName;

    @Size(max = 50, message = "Lastname must not exceed 50 characters")
    private String lastName;

    private boolean gender;

    private String phone;

    @Email(message = "Invalid Email")
    private String email;

    private Date dateOfBirth;

    private String address;

    private Long specialtyID;
}
