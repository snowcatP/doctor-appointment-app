package com.hhh.doctor_appointment_app.dto.response.PatientResponse;

import com.hhh.doctor_appointment_app.entity.Specialty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponse {
    private Long id;

    private String fullname;

    private boolean gender;

    private String phone;

    private String email;

    private Date dateOfBirth;

    private String address;

}
