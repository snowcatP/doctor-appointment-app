package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.AppointmentRequest.AppointmentByGuestRequest;
import com.hhh.doctor_appointment_app.dto.request.AppointmentRequest.AppointmentByPatientRequest;
import com.hhh.doctor_appointment_app.dto.request.AppointmentRequest.RescheduleWithDateRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentBookedResponse;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.service.AppointmentService.Command.CancelAppointment.CancelAppointmentCommand;
import com.hhh.doctor_appointment_app.service.AppointmentService.Command.ChangeStatusAppointment.ChangeStatusAppointmentCommand;
import com.hhh.doctor_appointment_app.service.AppointmentService.Command.CreateAppointment.CreateAppointmentByGuestCommand;
import com.hhh.doctor_appointment_app.service.AppointmentService.Command.CreateAppointment.CreateAppointmentByPatientCommand;
import com.hhh.doctor_appointment_app.service.AppointmentService.Command.ResheduleAppointment.RescheduleAppointmentByDoctor;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetAllAppointmentsByDoctorId.GetAllAppointmentsByDoctorIdQuery;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetAppointmentWithPage.GetAppointmentWithPageQuery;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetDetailAppointment.GetDetailAppointmentByPatientQuery;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetListAppointmentByDoctorId.GetListAppointmentByDoctorIdQuery;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetListAppointmentOfPatient.GetListAppointmentOfPatientQuery;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetListAppointmentsForBooking.GetListAppointmentsForBookingQuery;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/appointment")
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

    @Autowired
    private ChangeStatusAppointmentCommand changeStatusAppointmentCommand;

    @Autowired
    private RescheduleAppointmentByDoctor rescheduleAppointmentByDoctor;

    @Autowired
    private CancelAppointmentCommand cancelAppointmentCommand;

    @Autowired
    private GetListAppointmentByDoctorIdQuery getListAppointmentByDoctorIdQuery;

    @Autowired
    private GetListAppointmentsForBookingQuery getListAppointmentsForBookingQuery;

    @Autowired
    private GetListAppointmentOfPatientQuery getListAppointmentOfPatientQuery;

    @Autowired
    private GetAllAppointmentsByDoctorIdQuery getAllAppointmentsByDoctorIdQuery;

    @GetMapping("/list")
    public ResponseEntity<?> getAppointments(@RequestParam(defaultValue = "1") int page,
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

    @PostMapping("/patient/create-appointment")
    public ResponseEntity<?> createAppointmentByPatient(
            @Valid @RequestBody AppointmentByPatientRequest appointmentByPatientRequest,
            BindingResult bindingResult){
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

    @PostMapping("/guest/create-appointment")
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

    @PutMapping("/change-status/{id}")
    public ResponseEntity<?> changeStatusAppointmentByDoctor(@PathVariable Long id){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            apiResponse = changeStatusAppointmentCommand.changeStatusAppointmentByDoctor(id);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK); //  for success
        }
        catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @PutMapping("/reschedule/{id}")
    public ResponseEntity<?> rescheduleAppointmentByDoctor(@PathVariable Long id, @RequestBody RescheduleWithDateRequest rescheduleWithDateRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            apiResponse = rescheduleAppointmentByDoctor.rescheduleAppointmentByDoctor(id, rescheduleWithDateRequest);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK); //  for success
        }
        catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelledAppointmentByDoctor(@PathVariable Long id){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            apiResponse = cancelAppointmentCommand.cancelAppointmentByDoctor(id);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK); //  for success
        }
        catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @GetMapping("/list/doctor/{id}")
    public ResponseEntity<?> getAppointmentsByDoctorId(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size, @PathVariable Long id){
        try{
            return new ResponseEntity<>(getListAppointmentByDoctorIdQuery.getAppointmentsWithPageByDoctorId(page, size, id), HttpStatus.OK);
        }catch (Exception ex){
            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @GetMapping("/get-appointments-for-booking/{doctorId}")
    public ResponseEntity<List<AppointmentBookedResponse>> getAppointmentsForBooking(@PathVariable Long doctorId) {
        try {
            return new ResponseEntity<>(getListAppointmentsForBookingQuery.getListAppointmentsForBooking(doctorId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list/patient")
    public ResponseEntity<?> getAppointmentsOfPatient(@RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size){
        try{
            return new ResponseEntity<>(
                    getListAppointmentOfPatientQuery
                    .getAppointmentsWithPageOfPatient(page, size),
                    HttpStatus.OK
            );
        }catch (Exception ex){
            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @GetMapping("/get-all-appointments-by-doctor")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentByDoctor() {
        try {
            return new ResponseEntity<>(
                    getAllAppointmentsByDoctorIdQuery.getAllAppointmentsByDoctorId(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
