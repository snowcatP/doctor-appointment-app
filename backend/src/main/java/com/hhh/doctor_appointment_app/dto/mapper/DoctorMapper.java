package com.hhh.doctor_appointment_app.dto.mapper;

import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorResponse toResponse(Doctor doctor);
}
