package com.hhh.doctor_appointment_app.dto.mapper;

import com.hhh.doctor_appointment_app.dto.response.SpecialtyResponse.SpecialResponeWithDoctorList;
import com.hhh.doctor_appointment_app.dto.response.SpecialtyResponse.SpecialtyResponse;
import com.hhh.doctor_appointment_app.entity.Specialty;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface SpecialtyWithDoctor {
    SpecialResponeWithDoctorList toResponse(Specialty specialty);
}
