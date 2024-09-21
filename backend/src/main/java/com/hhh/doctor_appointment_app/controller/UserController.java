package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.UserCreateRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register/admin")
    public ApiResponse<Admin> addAdmin(@RequestBody UserCreateRequest request) {
        return ApiResponse.<Admin>builder()
                .data(userService.createAdmin(request))
                .build();
    }

    @PostMapping("/register/user")
    public ApiResponse<Patient> addPatient(@RequestBody UserCreateRequest request) {
        return ApiResponse.<Patient>builder()
                .data(userService.createPatient(request))
                .build();
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<Doctor> addDoctor(@RequestBody UserCreateRequest request) {
        return null;
    }
}
