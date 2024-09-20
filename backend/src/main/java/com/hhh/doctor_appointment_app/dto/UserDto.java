package com.hhh.doctor_appointment_app.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private String fullname;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Date dateOfBirth;
    private Boolean gender;
}
