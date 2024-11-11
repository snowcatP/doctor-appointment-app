package com.hhh.doctor_appointment_app.dto.mapper;

import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorBookingResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mappings({
            @Mapping(source = "doctor.profile.firstName", target = "firstName"),
            @Mapping(source = "doctor.profile.lastName", target = "lastName"),
            @Mapping(source = "doctor.profile.fullName", target = "fullName"),
            @Mapping(source = "doctor.profile.gender", target = "gender"),
            @Mapping(source = "doctor.profile.phone", target = "phone"),
            @Mapping(source = "doctor.profile.email", target = "email"),
            @Mapping(source = "doctor.profile.dateOfBirth", target = "dateOfBirth"),
            @Mapping(source = "doctor.profile.address", target = "address"),
            @Mapping(source = "doctor.specialty", target = "specialty"),
            @Mapping(source = "doctor.profile.avatarFilePath", target = "avatarFilePath"),
    })
    DoctorResponse toResponse(Doctor doctor);

    @Mappings({
            @Mapping(source = "profile.firstName", target = "firstName"),
            @Mapping(source = "profile.lastName", target = "lastName"),
            @Mapping(source = "profile.fullName", target = "fullName"),
    })
    DoctorBookingResponse toBookingResponse(Doctor doctor);

    @Mappings({
            @Mapping(source = "profile.avatarFilePath", target = "avatarFilePath"),
    })
    List<DoctorBookingResponse> toListDoctorBookingResponse(List<Doctor> doctors);
}
