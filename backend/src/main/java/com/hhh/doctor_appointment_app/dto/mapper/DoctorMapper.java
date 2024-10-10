package com.hhh.doctor_appointment_app.dto.mapper;

import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mappings({
            @Mapping(source = "doctor.profile.firstName", target = "firstName"),
            @Mapping(source = "doctor.profile.lastName", target = "lastName"),
            @Mapping(source = "doctor.profile.gender", target = "gender"),
            @Mapping(source = "doctor.profile.phone", target = "phone"),
            @Mapping(source = "doctor.profile.email", target = "email"),
            @Mapping(source = "doctor.profile.dateOfBirth", target = "dateOfBirth"),
            @Mapping(source = "doctor.profile.address", target = "address"),
            @Mapping(source = "doctor.specialty", target = "specialty")
    })
    DoctorResponse toResponse(Doctor doctor);
}
