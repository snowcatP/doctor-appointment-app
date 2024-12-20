package com.hhh.doctor_appointment_app.dto.mapper;

import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientChatResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
import com.hhh.doctor_appointment_app.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mappings({
            @Mapping(source = "patient.profile.firstName", target = "firstName"),
            @Mapping(source = "patient.profile.lastName", target = "lastName"),
            @Mapping(source = "patient.profile.gender", target = "gender"),
            @Mapping(source = "patient.profile.phone", target = "phone"),
            @Mapping(source = "patient.profile.email", target = "email"),
            @Mapping(source = "patient.profile.dateOfBirth", target = "dateOfBirth"),
            @Mapping(source = "patient.profile.address", target = "address"),
            @Mapping(source = "patient.profile.avatarFilePath", target = "avatarFilePath"),
            @Mapping(source = "patient.profile.role", target = "role"),
            @Mapping(source = "patient.profile.fullName", target = "fullName")
    })
    PatientResponse toResponse(Patient patient);

    @Mappings({
            @Mapping(source = "profile.fullName", target = "fullName"),
            @Mapping(source = "profile.email", target = "email"),
            @Mapping(source = "profile.avatarFilePath", target = "avatarFilePath")
    })
    PatientChatResponse toChatResponse(Patient patient);
}
