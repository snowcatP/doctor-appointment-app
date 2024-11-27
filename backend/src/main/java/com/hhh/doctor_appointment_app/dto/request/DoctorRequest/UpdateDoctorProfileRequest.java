package com.hhh.doctor_appointment_app.dto.request.DoctorRequest;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class UpdateDoctorProfileRequest {
    @Size(max = 50, message = "Firstname must not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Firstname contains invalid characters")
    private String firstName;

    @Size(max = 50, message = "Lastname must not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Lastname contains invalid characters")
    private String lastName;

    private boolean gender;

    @Pattern(regexp = "^\\+?[0-9\\-\\s]+$", message = "Phone number contains invalid characters")
    private String phone;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // Định dạng ngày yyyy-MM-dd
    private Date dateOfBirth;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    private String avatarFilePath;
}
