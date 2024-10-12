package com.hhh.doctor_appointment_app.dto.mapper;

import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserCreateRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserUpdateProfileRequest;
import com.hhh.doctor_appointment_app.dto.response.UserResponse;
import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", ignore = true)
    User toUser(UserCreateRequest request);

    @Mappings({
        @Mapping(source = "firstName", target = "profile.firstName"),
        @Mapping(source = "lastName", target = "profile.lastName"),
        @Mapping(source = "email", target = "profile.email"),
        @Mapping(source = "phone", target = "profile.phone"),
        @Mapping(source = "dateOfBirth", target = "profile.dateOfBirth"),
        @Mapping(source = "address", target = "profile.address"),
        @Mapping(source = "gender", target = "profile.gender"),
    })
    Admin toAdmin(UserCreateRequest request);

    @Mappings({
        @Mapping(source = "firstName", target = "profile.firstName"),
        @Mapping(source = "lastName", target = "profile.lastName"),
        @Mapping(source = "email", target = "profile.email"),
        @Mapping(source = "phone", target = "profile.phone"),
        @Mapping(source = "dateOfBirth", target = "profile.dateOfBirth"),
        @Mapping(source = "address", target = "profile.address"),
        @Mapping(source = "gender", target = "profile.gender"),
    })
    Patient toPatient(UserCreateRequest request);

    @Mappings({
            @Mapping(source = "firstName", target = "profile.firstName"),
            @Mapping(source = "lastName", target = "profile.lastName"),
            @Mapping(source = "email", target = "profile.email"),
            @Mapping(source = "phone", target = "profile.phone"),
            @Mapping(source = "dateOfBirth", target = "profile.dateOfBirth"),
            @Mapping(source = "address", target = "profile.address"),
            @Mapping(source = "gender", target = "profile.gender"),
    })
    Doctor toDoctor(UserCreateRequest request);

    UserResponse toUserResponse(User user);
}
