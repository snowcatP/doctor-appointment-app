package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserChangePasswordRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserCreateRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserForgotPasswordRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.service.UserService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AccountController {

    @Autowired
    private UserService userService;

    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@RequestBody UserForgotPasswordRequest request) {
        return ApiResponse.<String>builder()
                .data(userService.forgotPassword(request))
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(
            @RequestParam String token,
            @RequestBody UserChangePasswordRequest request)
            throws ParseException, JOSEException {
        return ApiResponse.<String>builder()
                .data(userService.resetUserPassword(token, request))
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @PostMapping("/signup")
    @CrossOrigin()
    public ApiResponse<String> userSignup(@RequestBody UserCreateRequest request) {
        var result = userService.userSignup(request);
        String message = result != null ? "Sign up successfully!" : "Sign up failed!";
        return ApiResponse.<String>builder()
                .data(message)
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @PostMapping("/register/admin")
    public ApiResponse<Admin> addAdmin(@RequestBody UserCreateRequest request) {
        return ApiResponse.<Admin>builder()
                .data(userService.createAdmin(request))
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @PostMapping("/register/user")
    @CrossOrigin()
    public ApiResponse<Patient> addPatient(@RequestBody UserCreateRequest request) {
        return ApiResponse.<Patient>builder()
                .data(userService.createPatient(request))
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @PostMapping("/register/doctor")
    public ApiResponse<Doctor> addDoctor(@RequestBody UserCreateRequest request) {
        return ApiResponse.<Doctor>builder()
                .data(userService.createDoctor(request))
                .statusCode(HttpStatus.OK.value())
                .build();
    }
}
