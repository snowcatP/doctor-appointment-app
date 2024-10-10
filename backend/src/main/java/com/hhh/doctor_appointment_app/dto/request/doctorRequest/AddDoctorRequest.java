package com.hhh.doctor_appointment_app.dto.request.doctorRequest;

import com.hhh.doctor_appointment_app.entity.Specialty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AddDoctorRequest {
    @Size(max = 50, message = "Full name must not exceed 50 characters")
    private String fullname;

    private boolean gender;

    private String phone;

    @Email(message = "Email không hợp lệ")
    private String email;

    private Date dateOfBirth;

    private String address;

    private Specialty specialty;
}
