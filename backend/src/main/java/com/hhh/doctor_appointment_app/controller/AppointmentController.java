package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.AppointmentRequest.AppointmentByGuestRequest;
import com.hhh.doctor_appointment_app.dto.request.AppointmentRequest.AppointmentByPatientRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.service.AppointmentService.Command.CreateAppointment.CreateAppointmentByGuestCommand;
import com.hhh.doctor_appointment_app.service.AppointmentService.Command.CreateAppointment.CreateAppointmentByPatientCommand;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetAppointmentWithPage.GetAppointmentWithPageQuery;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetDetailAppointment.GetDetailAppointmentByPatientQuery;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/appointment")
@CrossOrigin
public class AppointmentController {
    @Autowired
    private CreateAppointmentByPatientCommand createAppointmentByPatient;

    @Autowired
    private CreateAppointmentByGuestCommand createAppointmentByGuest;

    @Autowired
    private GetDetailAppointmentByPatientQuery getAppointmentDetailByPatient;

    @Autowired
    private GetAppointmentWithPageQuery getAppointmentWithPageQuery;

    @GetMapping("/list")
    public ResponseEntity<?> getDoctors(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size){
        try{
            return new ResponseEntity<>(getAppointmentWithPageQuery.getAppointmentsWithPage(page, size), HttpStatus.OK);
        }catch (Exception ex){
            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @PostMapping("/patient/create-appoinment")
    public ResponseEntity<?> createAppointmentByPatient(@Valid @RequestBody AppointmentByPatientRequest appointmentByPatientRequest, BindingResult bindingResult){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + errors);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
        try {
            apiResponse = createAppointmentByPatient.createAppointmentByPatient(appointmentByPatientRequest);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK); //  for success
        }
        catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @PostMapping("/guest/create-appoinment")
    public ResponseEntity<?> createAppointmentByGuest(@Valid @RequestBody AppointmentByGuestRequest appointmentByGuestRequest, BindingResult bindingResult){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + errors);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
        try {
            apiResponse = createAppointmentByGuest.createAppointmentByGuest(appointmentByGuestRequest);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK); //  for success
        }
        catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @GetMapping("/patient/detail/{id}")
    public ResponseEntity<?> getAppointmentDetailByPatient(@PathVariable Long id)
    {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            apiResponse = getAppointmentDetailByPatient.getAppointmentDetailByPatient(id);
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
