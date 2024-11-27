package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.PatientRequest.EditPatientRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.service.PatientService.Command.DeletePatient.DeletePatientCommand;
import com.hhh.doctor_appointment_app.service.PatientService.Command.EditPatient.EditPatientCommand;
import com.hhh.doctor_appointment_app.service.PatientService.Query.GetDetailPatient.GetDetailPatientQuery;
import com.hhh.doctor_appointment_app.service.PatientService.Query.GetPatient.GetPatientQuery;
import com.hhh.doctor_appointment_app.service.PatientService.Query.GetPatientWithPage.GetPatientWithPageQuery;
import com.hhh.doctor_appointment_app.service.PatientService.Query.GetProfilePatient.GetProfilePatientQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/patient")
@CrossOrigin
public class PatientController {
    @Autowired
    private GetPatientWithPageQuery getPatientsWithPage;
    @Autowired
    private GetPatientQuery getPatients;

    @Autowired
    private EditPatientCommand editPatientCommand;

    @Autowired
    private DeletePatientCommand deletePatientCommand;

    @Autowired
    private GetDetailPatientQuery getDetailPatientQuery;

    @Autowired
    private GetProfilePatientQuery getProfilePatientQuery;

    @GetMapping("/list-patient")
    public ResponseEntity<?> getPatients(){
        try{
            return new ResponseEntity<>(getPatients.getPatients(), HttpStatus.OK);
        }catch (Exception ex){
            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }



    @PutMapping("/edit-patient/{id}")
    public ResponseEntity<?> editPatient(@PathVariable Long id, @Valid @RequestBody EditPatientRequest editPatientRequest, BindingResult bindingResult){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + errors);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
        try {
            apiResponse = editPatientCommand.editPatient(id,editPatientRequest);
            // Check if the status code is 500 for duplicated code
            if (HttpStatus.INTERNAL_SERVER_ERROR.value() == apiResponse.getStatusCode()) {
                apiResponse.setMessage("Patient's Email already exist in the system");
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

    @DeleteMapping("/delete-patient/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id){
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            apiResponse = deletePatientCommand.deleteByIdPatient(id);
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
    public ResponseEntity<?> getPatientDetail(@PathVariable Long id)
    {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            apiResponse = getDetailPatientQuery.getPatientDetail(id);
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

    @GetMapping("/profile")
    @CrossOrigin("*")
    public ResponseEntity<?> getPatientProfile(HttpServletRequest request)
    {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            // Get token from header
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                apiResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                apiResponse.setMessage("Token is missing or invalid");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            }
            // Remove "Bearer " to get token
            token = token.substring(7);

            apiResponse = getProfilePatientQuery.getPatientProfile(token);
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
