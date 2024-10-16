package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserUpdatePasswordRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserUpdateProfileRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.UserResponse;
import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import com.hhh.doctor_appointment_app.service.UserService.Command.UpdateUserPassword.UpdateUserPasswordCommand;
import com.hhh.doctor_appointment_app.service.UserService.Command.UpdateUserProfile.UpdateUserProfileCommand;
import com.hhh.doctor_appointment_app.service.UserService.Query.GetAdminProfile.GetAdminProfileQuery;
import com.hhh.doctor_appointment_app.service.UserService.Query.GetAllAdmin.GetAllAdminQuery;
import com.hhh.doctor_appointment_app.service.UserService.Query.GetAllPatient.GetAllPatientQuery;
import com.hhh.doctor_appointment_app.service.UserService.Query.GetMyInfo.GetMyInfoQuery;
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
    private UpdateUserPasswordCommand updateUserPasswordCommand;

    @Autowired
    private UpdateUserProfileCommand updateUserProfileCommand;

    @Autowired
    private GetAdminProfileQuery getAdminProfileQuery;

    @Autowired
    private GetAllPatientQuery getAllPatientQuery;

    @Autowired
    private GetAllAdminQuery getAllAdminQuery;

    @Autowired
    private GetMyInfoQuery getMyInfoQuery;

    @PostMapping("/user/update-profile")
    public ApiResponse<User> updateProfile(@RequestBody UserUpdateProfileRequest request) {
        var result = updateUserProfileCommand.updateUserProfile(request);
        return ApiResponse.<User>builder()
                .data(result)
                .message("Update profile successful")
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @PostMapping("/user/update-password")
    public ApiResponse<String> updatePassword(@RequestBody UserUpdatePasswordRequest request) {
        var result = updateUserPasswordCommand.updateUserPassword(request);
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
        return ResponseEntity.ok(getAdminProfileQuery.getAdminProfile(id));
    }

    @GetMapping("/users")
    public ResponseEntity<List<Patient>> getAllUsers() {
        return ResponseEntity.ok(getAllPatientQuery.getAllPatients());
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(getAllAdminQuery.getAllAdmin());
    }

    @GetMapping("/myInfo")
    public ResponseEntity<UserResponse> getMyInfo() {
        return ResponseEntity.ok(getMyInfoQuery.getMyInfo());
    }
}
