package com.hhh.doctor_appointment_app.dto.mapper;

import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mappings({
            @Mapping(source = "patient.profile.firstName", target = "firstName"),
            @Mapping(source = "patient.profile.lastName", target = "lastName"),
            @Mapping(source = "patient.profile.fullname", target = "fullname"),
            @Mapping(source = "patient.profile.gender", target = "gender"),
            @Mapping(source = "patient.profile.phone", target = "phone"),
            @Mapping(source = "patient.profile.email", target = "email"),
            @Mapping(source = "patient.profile.dateOfBirth", target = "dateOfBirth"),
            @Mapping(source = "patient.profile.address", target = "address")
    })
    PatientResponse toResponse(Patient patient);
}
