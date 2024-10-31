package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.AddDoctorRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserChangePasswordRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserCreateRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserForgotPasswordRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserSignupRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.service.DoctorService.Command.CreateDoctor.CreateDoctorByAdminCommand;
import com.hhh.doctor_appointment_app.service.FirebaseStorageService;
import com.hhh.doctor_appointment_app.service.UserService.Command.CreateAdmin.CreateAdminCommand;
import com.hhh.doctor_appointment_app.service.UserService.Command.CreateDoctor.CreateDoctorCommand;
import com.hhh.doctor_appointment_app.service.UserService.Command.CreatePatient.CreatePatientCommand;
import com.hhh.doctor_appointment_app.service.UserService.Command.ForgotPassword.ForgotPasswordCommand;
import com.hhh.doctor_appointment_app.service.UserService.Command.ResetUserPassword.ResetUserPasswordCommand;
import com.hhh.doctor_appointment_app.service.UserService.Command.UserSignup.UserSignupCommand;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("api/auth")
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

    @Autowired
    private CreateDoctorByAdminCommand createDoctorByAdminCommand;



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
    public ApiResponse<String> userSignup(@RequestBody UserSignupRequest request) {
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
    @CrossOrigin()
    public ResponseEntity<?> registerAndUploadFileDoctorByAdmin(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute @Valid AddDoctorRequest addDoctorRequest, // use ModelAttribute to bind data
            BindingResult bindingResult) {

        ApiResponse<Object> apiResponse = new ApiResponse<>();

        // Check validation
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("Validation errors: " + errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse); // Return 400 to validation
        }

        try {
            // Save
            apiResponse = createDoctorByAdminCommand.addDoctor(file,addDoctorRequest);

            //Check email existed in the system
            if (HttpStatus.INTERNAL_SERVER_ERROR.value() == apiResponse.getStatusCode()) {
                apiResponse.setMessage("Doctor's Email already exists in the system");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse); // Return 409 if email conflict
            }

            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Doctor added successfully");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);

        } catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }
}
