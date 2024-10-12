package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserChangePasswordRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserUpdatePasswordRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserUpdateProfileRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.UserResponse;
import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/identity")
@CrossOrigin
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @PostMapping("/user/update-profile")
    public ApiResponse<User> updateProfile(@RequestBody UserUpdateProfileRequest request) {
        var result = userService.updateUserProfile(request);
        return ApiResponse.<User>builder()
                .data(result)
                .message("Update profile successful")
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @PostMapping("/user/update-password")
    public ApiResponse<String> updatePassword(@RequestBody UserUpdatePasswordRequest request) {
        var result = userService.updateUserPassword(request);
        String message = "Change password successfully";
        if (result == null) {
            message = "Change password failed";
        }
        return ApiResponse.<String>builder()
                .message(message)
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<Admin> getAdminProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getAdminProfile(id));
    }

    @GetMapping("/users")
    public ResponseEntity<List<Patient>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllPatients());
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(userService.getAllAdmin());
    }

    @GetMapping("/myInfo")
    public ResponseEntity<UserResponse> getMyInfo() {
        return ResponseEntity.ok(userService.getMyInfo());
    }
}
