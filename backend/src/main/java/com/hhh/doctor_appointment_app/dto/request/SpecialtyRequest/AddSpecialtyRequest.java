package com.hhh.doctor_appointment_app.dto.request.SpecialtyRequest;

import com.hhh.doctor_appointment_app.entity.Doctor;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddSpecialtyRequest {
    @Size(max = 50, message = "Full name must not exceed 100 characters")
    private String specialtyName;

    private Long headDoctorId;

    private List<Long> listDoctorId;

}
