package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.userRequest.UserCreateRequest;
import com.hhh.doctor_appointment_app.dto.request.userRequest.UserUpdateProfileRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.UserResponse;
import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    public ApiResponse<?> updateProfile(UserUpdateProfileRequest request) {
        var result = userService.updateUserProfile(request);

        return new ApiResponse<>();

    }

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

    @GetMapping("/identity/admin/{id}")
    public ResponseEntity<Admin> getAdminProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getAdminProfile(id));
    }

    @GetMapping("/identity/users")
    public ResponseEntity<List<Patient>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllPatients());
    }

    @GetMapping("/identity/admin")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(userService.getAllAdmin());
    }

    @GetMapping("/identity/myInfo")
    public ResponseEntity<UserResponse> getMyInfo() {
        return ResponseEntity.ok(userService.getMyInfo());
    }
}
