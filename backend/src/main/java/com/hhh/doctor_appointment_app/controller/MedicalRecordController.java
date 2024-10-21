package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.MedicalRecordRequest.AddMedicalRecordRequest;
import com.hhh.doctor_appointment_app.dto.request.MedicalRecordRequest.EditMedicalRecordRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.PageResponse;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.service.MedicalRecordService.Command.CreateMedicalRecord.CreateMedicalRecordCommand;
import com.hhh.doctor_appointment_app.service.MedicalRecordService.Command.DeleteMedicalRecord.DeleteMedicalRecordCommand;
import com.hhh.doctor_appointment_app.service.MedicalRecordService.Command.EditMedicalRecord.EditMedicalRecordCommand;
import com.hhh.doctor_appointment_app.service.MedicalRecordService.Query.GetDetailMedicalRecord.GetDetailMedicalRecordQuery;
import com.hhh.doctor_appointment_app.service.MedicalRecordService.Query.GetMedicalRecordWithPage.GetMedicalRecordWithPageQuery;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/medical-record")
@CrossOrigin
public class MedicalRecordController {
    @Autowired
    private GetDetailMedicalRecordQuery getMedicalRecordDetail;

    @Autowired
    private CreateMedicalRecordCommand createMedicalRecordCommand;

    @Autowired
    private EditMedicalRecordCommand editMedicalRecordCommand;

    @Autowired
    private DeleteMedicalRecordCommand deleteMedicalRecordCommand;

    @Autowired
    private GetMedicalRecordWithPageQuery getMedicalRecordsWithPage;

    @GetMapping("/list")
    public ResponseEntity<?> getMedicalRecords(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size){
        try{
            return new ResponseEntity<>(getMedicalRecordsWithPage.getMedicalRecordsWithPage(page, size), HttpStatus.OK);
        }catch (Exception ex){
            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMedicalRecordByDoctor(@Valid @RequestBody AddMedicalRecordRequest addMedicalRecordRequest,
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
            apiResponse = createMedicalRecordCommand.addMedicalRecordByDoctor(addMedicalRecordRequest);
            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Add Successfully");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK); //  for success
        }
        catch (Exception ex) {

            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editMedicalRecordByDoctor(@PathVariable Long id, @Valid @RequestBody EditMedicalRecordRequest editMedicalRecordRequest, BindingResult bindingResult){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + errors);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
        try {
            apiResponse = editMedicalRecordCommand.editMedicalRecordByDoctor(id,editMedicalRecordRequest);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
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
    public ResponseEntity<?> getMedicalRecordDetail(@PathVariable Long id)
    {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            apiResponse = getMedicalRecordDetail.getMedicalRecordDetail(id);
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMedicalRecordByDoctor(@PathVariable Long id){
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            apiResponse = deleteMedicalRecordCommand.deleteMedicalRecordByDoctor(id);
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
}
