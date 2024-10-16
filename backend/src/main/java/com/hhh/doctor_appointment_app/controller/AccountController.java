package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserChangePasswordRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserCreateRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserForgotPasswordRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.service.UserService.Command.CreateAdmin.CreateAdminCommand;
import com.hhh.doctor_appointment_app.service.UserService.Command.CreateDoctor.CreateDoctorCommand;
import com.hhh.doctor_appointment_app.service.UserService.Command.CreatePatient.CreatePatientCommand;
import com.hhh.doctor_appointment_app.service.UserService.Command.ForgotPassword.ForgotPasswordCommand;
import com.hhh.doctor_appointment_app.service.UserService.Command.ResetUserPassword.ResetUserPasswordCommand;
import com.hhh.doctor_appointment_app.service.UserService.Command.UserSignup.UserSignupCommand;
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
    private ForgotPasswordCommand forgotPasswordCommand;

    @Autowired
    private ResetUserPasswordCommand resetUserPasswordCommand;

    @Autowired
    private UserSignupCommand userSignupCommand;

    @Autowired
    private CreateAdminCommand createAdminCommand;

    @Autowired
    private CreatePatientCommand createPatientCommand;

    @Autowired
    private CreateDoctorCommand createDoctorCommand;

    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@RequestBody UserForgotPasswordRequest request) {
        return ApiResponse.<String>builder()
                .data(forgotPasswordCommand.forgotPassword(request))
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(
            @RequestParam String token,
            @RequestBody UserChangePasswordRequest request)
            throws ParseException, JOSEException {
        return ApiResponse.<String>builder()
                .data(resetUserPasswordCommand.resetUserPassword(token, request))
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @PostMapping("/signup")
    @CrossOrigin()
    public ApiResponse<String> userSignup(@RequestBody UserCreateRequest request) {
        var result = userSignupCommand.userSignup(request);
        String message = result != null ? "Sign up successfully!" : "Sign up failed!";
        return ApiResponse.<String>builder()
                .data(message)
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @PostMapping("/register/admin")
    public ApiResponse<Admin> addAdmin(@RequestBody UserCreateRequest request) {
        return ApiResponse.<Admin>builder()
                .data(createAdminCommand.createAdmin(request))
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @PostMapping("/register/user")
    @CrossOrigin()
    public ApiResponse<Patient> addPatient(@RequestBody UserCreateRequest request) {
        return ApiResponse.<Patient>builder()
                .data(createPatientCommand.createPatient(request))
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @PostMapping("/register/doctor")
    public ApiResponse<Doctor> addDoctor(@RequestBody UserCreateRequest request) {
        return ApiResponse.<Doctor>builder()
                .data(createDoctorCommand.createDoctor(request))
                .statusCode(HttpStatus.OK.value())
                .build();
    }
}
