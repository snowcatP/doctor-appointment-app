package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.AppointmentRequest.*;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentBookedResponse;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.service.AppointmentService.Command.CancelAppointment.CancelAppointmentCommand;
import com.hhh.doctor_appointment_app.service.AppointmentService.Command.ChangeInProgessingStatusOfAppointment.ChangeInProgressStatusOfAppointmentCommand;
import com.hhh.doctor_appointment_app.service.AppointmentService.Command.ChangeStatusAppointment.ChangeStatusAppointmentCommand;
import com.hhh.doctor_appointment_app.service.AppointmentService.Command.CreateAppointment.CreateAppointmentByGuestCommand;
import com.hhh.doctor_appointment_app.service.AppointmentService.Command.CreateAppointment.CreateAppointmentByPatientCommand;
import com.hhh.doctor_appointment_app.service.AppointmentService.Command.ResheduleAppointment.RescheduleAppointmentByDoctorOrPatient;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetAllAppointmentsByDoctorId.GetAllAppointmentsByDoctorIdQuery;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetAppointmentByReferenceCode.GetAppointmentByReferenceCodeQuery;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetAppointmentWithPage.GetAppointmentWithPageQuery;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetDetailAppointment.GetDetailAppointmentByPatientQuery;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetListAppointmentByDoctorId.GetListAppointmentByDoctorIdQuery;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetListAppointmentForNurse.GetListAppointmentForNurseQuery;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetListAppointmentOfPatient.GetListAppointmentOfPatientQuery;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetListAppointmentOfPatientByPatientId.GetListAppointmentOfPatientByPatientIdQuery;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetListAppointmentsForBooking.GetListAppointmentsForBookingQuery;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetListAppointmentsForRescheduling.GetListAppointmentsForReschedulingByPatientQuery;
import com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetListAppointmentsForRescheduling.GetListAppointmentsForReschedulingQuery;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private RescheduleAppointmentByDoctorOrPatient rescheduleAppointment;

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

    @Autowired
    private GetListAppointmentOfPatientByPatientIdQuery getListAppointmentOfPatientByPatientIdQuery;

    @Autowired
    private GetListAppointmentsForReschedulingQuery getListAppointmentsForReschedulingQuery;

    @Autowired
    private GetListAppointmentsForReschedulingByPatientQuery getListAppointmentsForReschedulingByPatientQuery;

    @Autowired
    private GetAppointmentByReferenceCodeQuery getAppointmentByReferenceCodeQuery;

    @Autowired
    private GetListAppointmentForNurseQuery getListAppointmentForNurseQuery;

    @Autowired
    private ChangeInProgressStatusOfAppointmentCommand changeInProgressStatusOfAppointmentCommand;
    @GetMapping("/list")
    public ResponseEntity<?> getAppointments(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size){
        try{
            return new ResponseEntity<>(getAppointmentWithPageQuery
                    .getAppointmentsWithPage(page, size), HttpStatus.OK);
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
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(errorMessage);
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
    public ResponseEntity<?> createAppointmentByGuest(
            @Valid @RequestBody AppointmentByGuestRequest appointmentByGuestRequest,
            BindingResult bindingResult){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors()
                    .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
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

    @PutMapping("/in-progress/{id}")
    public ResponseEntity<?> changeInProgressOfAppointmentByDoctor(@PathVariable Long id){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            apiResponse = changeInProgressStatusOfAppointmentCommand.changeInProgressStatusOfAppointmentByDoctor(id);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK); //  for success
        }
        catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @PutMapping("/reschedule/{id}")
    public ResponseEntity<?> rescheduleAppointment(
            @PathVariable Long id,
            @RequestBody RescheduleWithDateRequest rescheduleWithDateRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            apiResponse = rescheduleAppointment
                    .rescheduleAppointmentByDoctorOrPatient(id, rescheduleWithDateRequest);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK); //  for success
        }
        catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @PutMapping("/cancel-by-doctor/{id}")
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
    @PutMapping("/cancel-by-patient/{id}")
    public ResponseEntity<?> cancelledAppointmentByPatient(@PathVariable Long id){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            apiResponse = cancelAppointmentCommand.cancelAppointmentByPatient(id);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK); //  for success
        }
        catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @GetMapping("/list/doctor")
    public ResponseEntity<?> getAppointmentsByDoctorId(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size){
        try{
            return new ResponseEntity<>(getListAppointmentByDoctorIdQuery
                    .getAppointmentsWithPageByDoctorId(page, size), HttpStatus.OK);
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
            return new ResponseEntity<>(getListAppointmentsForBookingQuery
                    .getListAppointmentsForBooking(doctorId), HttpStatus.OK);
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
    
    @GetMapping("/list/patient/{id}")
    public ResponseEntity<?> getAppointmentsOfPatientByPatientId(@PathVariable(name = "id") Long id,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size){
        try{
            return new ResponseEntity<>(getListAppointmentOfPatientByPatientIdQuery.
                    getListAppointmentOfPatientByPatientId(id,page, size), HttpStatus.OK);
        }catch (Exception ex){
            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @GetMapping("/get-appointments-for-rescheduling")
    public ResponseEntity<List<AppointmentBookedResponse>> getAppointmentsForRescheduling() {
        try {
            return new ResponseEntity<>(getListAppointmentsForReschedulingQuery
                    .getListAppointmentsForRescheduling(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/get-appointments-for-rescheduling-by-patient")
    public ResponseEntity<List<AppointmentBookedResponse>> getAppointmentsForReschedulingByPatient(
            @RequestBody GetAppointmentForReschedulingRequest request) {
        try {
            return new ResponseEntity<>(getListAppointmentsForReschedulingByPatientQuery
                    .getListAppointmentsForReschedulingByPatient(request), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/search/reference-code")
    public ResponseEntity<?> getAppointmentByReferenceCodeForPatient(
            @RequestBody ReferenceCodeRequest referenceCodeRequest)
    {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            apiResponse = getAppointmentByReferenceCodeQuery.getAppointmentByReferenceCode(referenceCodeRequest);
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

    @GetMapping("/list/nurse")
    public ResponseEntity<?> getAppointmentsForNurse(@RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size){
        try{
            return new ResponseEntity<>(getListAppointmentForNurseQuery
                    .getAppointmentsForNurse(page, size), HttpStatus.OK);
        }catch (Exception ex){
            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }
}
