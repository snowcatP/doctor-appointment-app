package com.hhh.doctor_appointment_app.dto.response;

import com.hhh.doctor_appointment_app.entity.Role;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
    private Long id;
    private String fullName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private Date dateOfBirth;
    private Boolean gender;
    private Role role;
}
