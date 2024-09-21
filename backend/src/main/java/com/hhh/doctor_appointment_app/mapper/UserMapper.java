package com.hhh.doctor_appointment_app.mapper;

import com.hhh.doctor_appointment_app.dto.request.UserCreateRequest;
import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.entity.Patient;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserMapper {
    public static Admin toAdmin(UserCreateRequest request) {
        Admin admin = new Admin();
        admin.setFullname(request.getFullname());
        admin.setEmail(request.getEmail());
        admin.setPassword(request.getPassword());
        admin.setPhone(request.getPhone());
        admin.setAddress(request.getAddress());
        admin.setDateOfBirth(request.getDateOfBirth());
        admin.setGender(request.getGender());
        admin.setUsername(request.getEmail());
//        admin.setRoles();
        return admin;
    }

    public static Patient toPatient(UserCreateRequest request) {
        Patient patient = new Patient();
        patient.setFullname(request.getFullname());
        patient.setEmail(request.getEmail());
        patient.setPassword(request.getPassword());
        patient.setPhone(request.getPhone());
        patient.setAddress(request.getAddress());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setUsername(request.getEmail());
        return patient;
    }

}
