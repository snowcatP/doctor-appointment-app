package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserUpdatePasswordRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserUpdateProfileRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.UserResponse;
import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import com.hhh.doctor_appointment_app.service.FirebaseStorageService;
import com.hhh.doctor_appointment_app.service.UserService.Command.UpdateUserPassword.UpdateUserPasswordCommand;
import com.hhh.doctor_appointment_app.service.UserService.Command.UpdateUserProfile.UpdateUserProfileCommand;
import com.hhh.doctor_appointment_app.service.UserService.Query.GetAdminProfile.GetAdminProfileQuery;
import com.hhh.doctor_appointment_app.service.UserService.Query.GetAllAdmin.GetAllAdminQuery;
import com.hhh.doctor_appointment_app.service.UserService.Query.GetAllPatient.GetAllPatientQuery;
import com.hhh.doctor_appointment_app.service.UserService.Query.GetMyInfo.GetMyInfoQuery;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/identity")
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

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @PutMapping("/user/update-profile")
    public ResponseEntity<?> updateProfile(@RequestParam("file") MultipartFile file,
                                           @ModelAttribute @Valid UserUpdateProfileRequest request,
                                           BindingResult bindingResult) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        if (bindingResult.hasErrors()) {
            // Collect only error messages into a single string, separated by commas if there are multiple errors.
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(errorMessage);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
        try {
            // Upload file to Firebase Storage
            if(!file.isEmpty()){
                String fileUrl = firebaseStorageService.uploadFile(file);
                request.setAvatarFilePath(fileUrl);
            }
            apiResponse = updateUserProfileCommand.updateUserProfile(request);
            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Update profile successful");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK); //  for success
        }
        catch (NotFoundException ex){
            apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
        catch (Exception ex) {

            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
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
