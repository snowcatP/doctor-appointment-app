package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.NurseRequest.UpdateNurseProfileRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.service.FirebaseStorageService;
import com.hhh.doctor_appointment_app.service.NurseService.Command.UpdateNurseProfile.UpdateNurseProfileCommand;
import com.hhh.doctor_appointment_app.service.NurseService.Query.GetNurseProfile.GetNurseProfileQuery;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

@RestController
@RequestMapping("api/nurse")
@CrossOrigin("*")
public class NurseController {
    @Autowired
    private GetNurseProfileQuery getNurseProfileQuery;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Autowired
    private UpdateNurseProfileCommand updateNurseProfileCommand;

    @GetMapping("/myInfo")
    public ResponseEntity<?> getNurseProfile()
    {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            apiResponse = getNurseProfileQuery.getNurseProfile();
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
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

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateDoctorProfile(@RequestParam("file") MultipartFile file,
                                                 @ModelAttribute @Valid UpdateNurseProfileRequest updateNurseProfileRequest, // sử dụng ModelAttribute để bind dữ liệu
                                                 BindingResult bindingResult){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        if (bindingResult.hasErrors()) {
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
                updateNurseProfileRequest.setAvatarFilePath(fileUrl);
            }
            apiResponse = updateNurseProfileCommand.updateNurseProfile(updateNurseProfileRequest);
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
}
