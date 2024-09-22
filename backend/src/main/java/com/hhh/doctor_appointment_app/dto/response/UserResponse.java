package com.hhh.doctor_appointment_app.dto.response;

import lombok.*;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
    private String fullname;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private Date dateOfBirth;
    private Boolean gender;
    private Set<String> roles;
}
