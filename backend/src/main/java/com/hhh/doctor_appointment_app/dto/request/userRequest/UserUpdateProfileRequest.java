package com.hhh.doctor_appointment_app.dto.request.userRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateProfileRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private boolean gender;
    private Date dateOfBirth;
    private String address;
}
