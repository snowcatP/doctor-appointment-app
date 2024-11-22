package com.hhh.doctor_appointment_app.dto.mapper;

import com.hhh.doctor_appointment_app.dto.response.NurseResponse.NurseResponse;
import com.hhh.doctor_appointment_app.entity.Nurse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface NurseMapper {
    @Mappings({
            @Mapping(source = "nurse.profile.firstName", target = "firstName"),
            @Mapping(source = "nurse.profile.lastName", target = "lastName"),
            @Mapping(source = "nurse.profile.fullName", target = "fullName"),
            @Mapping(source = "nurse.profile.gender", target = "gender"),
            @Mapping(source = "nurse.profile.phone", target = "phone"),
            @Mapping(source = "nurse.profile.email", target = "email"),
            @Mapping(source = "nurse.profile.dateOfBirth", target = "dateOfBirth"),
            @Mapping(source = "nurse.profile.address", target = "address"),
            @Mapping(source = "nurse.profile.avatarFilePath", target = "avatarFilePath"),
    })
    NurseResponse toResponse(Nurse nurse);

}
