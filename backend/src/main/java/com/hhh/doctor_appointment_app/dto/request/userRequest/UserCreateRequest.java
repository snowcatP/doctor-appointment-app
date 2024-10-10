package com.hhh.doctor_appointment_app.dto.request.userRequest;

import lombok.*;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Date dateOfBirth;
    private Boolean gender;
    private String role;
}
