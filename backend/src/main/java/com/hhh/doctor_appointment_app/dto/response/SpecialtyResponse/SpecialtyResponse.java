package com.hhh.doctor_appointment_app.dto.response.SpecialtyResponse;

import com.hhh.doctor_appointment_app.entity.Doctor;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecialtyResponse {
    private Long id;
    private String specialtyName;
    private Doctor headDoctor;
    private List<Doctor> doctorList;
}
