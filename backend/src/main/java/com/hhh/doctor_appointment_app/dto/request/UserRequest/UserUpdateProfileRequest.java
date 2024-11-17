package com.hhh.doctor_appointment_app.dto.request.UserRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class UserUpdateProfileRequest {
    @Size(max = 50, message = "Firstname must not exceed 50 characters")
    private String firstName;
    @Size(max = 50, message = "Lastname must not exceed 50 characters")
    private String lastName;
    private String phone;
    @Email(message = "Email không hợp lệ")
    private String email;
    private boolean gender;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date dateOfBirth;
    private String address;
    private String avatarFilePath;
}
