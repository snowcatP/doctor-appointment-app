package com.hhh.doctor_appointment_app.mapper;

import com.hhh.doctor_appointment_app.dto.request.UserCreateRequest;
import com.hhh.doctor_appointment_app.dto.response.UserResponse;
import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.util.singleton.PasswordEncoderSingleton;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserMapper {

    private static final PasswordEncoder passwordEncoder = PasswordEncoderSingleton.getPasswordEncoder();

    public static Admin toAdmin(UserCreateRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .fullname(request.getFirstName() + " " + request.getLastName())
                .gender(request.getGender())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .username(request.getEmail())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isActive(true)
                .build();

        return Admin.builder()
                .profile(user)
                .build();
    }

    public static Patient toPatient(UserCreateRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .fullname(request.getFirstName() + " " + request.getLastName())
                .gender(request.getGender())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .username(request.getEmail())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isActive(true)
                .build();

        return Patient.builder()
                .profile(user)
                .appointmentList(new ArrayList<>())
                .feedbackList(new ArrayList<>())
                .medicalRecordList(new ArrayList<>())
                .build();
    }

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .dateOfBirth(user.getDateOfBirth())
                .fullname(user.getFullname())
                .gender(user.isGender())
                .role(user.getRole())
                .build();
    }
}
