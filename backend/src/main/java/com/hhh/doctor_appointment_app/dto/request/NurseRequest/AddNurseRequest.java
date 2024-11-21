package com.hhh.doctor_appointment_app.dto.request.NurseRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class AddNurseRequest {
    @Size(max = 50, message = "Firstname must not exceed 50 characters")
    private String firstName;

    @Size(max = 50, message = "Lastname must not exceed 50 characters")
    private String lastName;

    private boolean gender;

    private String phone;

    @Email(message = "Invalid Email")
    private String email;

    private String password;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd") // Định dạng ngày yyyy-MM-dd
    private Date dateOfBirth;

    private String address;

    private String avatarFilePath;

    private String role;
}
