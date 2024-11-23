package com.hhh.doctor_appointment_app.dto.response.SpecialtyResponse;

import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecialResponeWithDoctorList {
    private Long id;
    private String specialtyName;
    private List<DoctorResponse> doctorList;
    private Doctor headDoctor;
}
