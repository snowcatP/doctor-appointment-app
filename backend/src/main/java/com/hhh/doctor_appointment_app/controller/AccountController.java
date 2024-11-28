package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.AddDoctorRequest;
import com.hhh.doctor_appointment_app.dto.request.NurseRequest.AddNurseRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserChangePasswordRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserCreateRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserForgotPasswordRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserSignupRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.service.DoctorService.Command.CreateDoctor.CreateDoctorByAdminCommand;
import com.hhh.doctor_appointment_app.service.NurseService.Command.CreateNurseByAdmin.CreateNurseByAdminCommand;
import com.hhh.doctor_appointment_app.service.UserService.Command.CreateAdmin.CreateAdminCommand;
import com.hhh.doctor_appointment_app.service.UserService.Command.CreateDoctor.CreateDoctorCommand;
import com.hhh.doctor_appointment_app.service.UserService.Command.CreatePatient.CreatePatientCommand;
import com.hhh.doctor_appointment_app.service.UserService.Command.ForgotPassword.ForgotPasswordCommand;
import com.hhh.doctor_appointment_app.service.UserService.Command.ResetPasswordNotLogin.ResetPasswordNotLoginCommand;
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
import java.util.stream.Collectors;

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

    @Autowired
    private CreateNurseByAdminCommand createNurseByAdminCommand;

    @Autowired
    private ResetPasswordNotLoginCommand resetPasswordNotLoginCommand;



    @PostMapping("/forgot-password")
    public ResponseEntity<?>  forgotPassword(@RequestBody UserForgotPasswordRequest request) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            System.out.println(request.getEmail());
            apiResponse = forgotPasswordCommand.forgotPassword(request);
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

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestBody UserChangePasswordRequest request)
            throws ParseException, JOSEException {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            apiResponse = resetUserPasswordCommand.resetUserPassword(token,request);
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

    @PostMapping("/user/reset-password")
    public ResponseEntity<?> resetPasswordNotLogin(
            @RequestParam String token,
            @RequestBody UserChangePasswordRequest request)
            throws ParseException, JOSEException {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            apiResponse = resetPasswordNotLoginCommand.resetUserPasswordNotLogin(token,request);
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
                .message("Create account successfully")
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
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(errorMessage);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
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

    @PostMapping("/register/nurse")
    public ResponseEntity<?> registerNurseAndUploadFileByAdmin(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute @Valid AddNurseRequest addNurseRequest, // use ModelAttribute to bind data
            BindingResult bindingResult) {

        ApiResponse<Object> apiResponse = new ApiResponse<>();

        // Check validation
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(errorMessage);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }

        try {
            // Save
            apiResponse = createNurseByAdminCommand.addNurseByAdmin(file,addNurseRequest);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);

        } catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }
}
