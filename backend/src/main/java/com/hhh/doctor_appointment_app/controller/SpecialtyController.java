package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.AddDoctorRequest;
import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.EditDoctorRequest;
import com.hhh.doctor_appointment_app.dto.request.SpecialtyRequest.AddSpecialtyRequest;
import com.hhh.doctor_appointment_app.dto.request.SpecialtyRequest.EditSpecialtyRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.service.SpecialtyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/specialty")
@CrossOrigin
public class SpecialtyController {

    @Autowired
    private SpecialtyService specialtyService;

    @GetMapping("/list-specialty")
    public ResponseEntity<?> getDoctors(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size){
        try{
            return new ResponseEntity<>(specialtyService.getSpecialtiesWithPage(page, size), HttpStatus.OK);
        }catch (Exception ex){
            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @PostMapping("/add-specialty")
    public ResponseEntity<?> addSpecialty(@Valid @RequestBody AddSpecialtyRequest addSpecialtyRequest, BindingResult bindingResult){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.OK).body(errors);
        }
        try {
            apiResponse = specialtyService.addSpecialty(addSpecialtyRequest);

            // Check if the status code is 500 for duplicated code
            if (HttpStatus.INTERNAL_SERVER_ERROR.value() == apiResponse.getStatusCode()) {
                apiResponse.setMessage("Specialty Name already exist in the system");
                return ResponseEntity.status(HttpStatus.OK).body(apiResponse); // Conflict for duplicated code
            }
            return new ResponseEntity<>(apiResponse, HttpStatus.OK); //  for success
        }
        catch (Exception ex) {

            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @PutMapping("/edit-specialty/{id}")
    public ResponseEntity<?> editSpecialty(@PathVariable Long id, @Valid @RequestBody EditSpecialtyRequest editSpecialtyRequest, BindingResult bindingResult){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.OK).body(errors);
        }
        try {
            apiResponse = specialtyService.editSpecialty(id,editSpecialtyRequest);
            // Check if the status code is 500 for duplicated code
            if (HttpStatus.INTERNAL_SERVER_ERROR.value() == apiResponse.getStatusCode()){
                apiResponse.setMessage("Specialty Name already exist in the system");
                return ResponseEntity.status(HttpStatus.OK).body(apiResponse); // Conflict for duplicated code
            }
            return new ResponseEntity<>(apiResponse, HttpStatus.OK); //  for success
        }
        catch (NotFoundException ex){
            apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
        catch (Exception ex) {

            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @DeleteMapping("/delete-specialty/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id){
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            apiResponse = specialtyService.deleteByIdSpecialty(id);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
        catch (NotFoundException ex){
            apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
        catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDoctorDetail(@PathVariable Long id)
    {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            apiResponse = specialtyService.getSpecialtyDetail(id);
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
}
