package com.hhh.doctor_appointment_app.dto.request;

import lombok.*;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserCreateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Date dateOfBirth;
    private Boolean gender;
    private Set<String> roles;
}
