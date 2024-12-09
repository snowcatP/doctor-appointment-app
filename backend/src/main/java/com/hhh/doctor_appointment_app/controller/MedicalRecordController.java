package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.MedicalRecordRequest.AddMedicalRecordRequest;
import com.hhh.doctor_appointment_app.dto.request.MedicalRecordRequest.EditMedicalRecordRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.service.MedicalRecordService.Command.CreateMedicalRecord.CreateMedicalRecordCommand;
import com.hhh.doctor_appointment_app.service.MedicalRecordService.Command.DeleteMedicalRecord.DeleteMedicalRecordCommand;
import com.hhh.doctor_appointment_app.service.MedicalRecordService.Command.EditMedicalRecord.EditMedicalRecordCommand;
import com.hhh.doctor_appointment_app.service.MedicalRecordService.Query.GetDetailMedicalRecord.GetDetailMedicalRecordQuery;
import com.hhh.doctor_appointment_app.service.MedicalRecordService.Query.GetMedicalRecordOfPatient.GetMedicalRecordOfPatientQuery;
import com.hhh.doctor_appointment_app.service.MedicalRecordService.Query.GetMedicalRecordWithPage.GetMedicalRecordWithPageQuery;
import com.hhh.doctor_appointment_app.service.MedicalRecordService.Query.GetMedicalRecordWithPageByPatient.GetMedicalRecordWithPageByPatientQuery;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private GetMedicalRecordWithPageByPatientQuery getMedicalRecordWithPageByPatientQuery;

    @Autowired
    private GetMedicalRecordOfPatientQuery getMedicalRecordOfPatientQuery;

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
    public ResponseEntity<?> addMedicalRecordAndUploadImageByDoctor(
            @Param("file") MultipartFile file,
            @ModelAttribute @Valid AddMedicalRecordRequest addMedicalRecordRequest, // sử dụng ModelAttribute để bind dữ liệu
            BindingResult bindingResult) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        // Kiểm tra lỗi validation
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse); // Trả về lỗi 400 cho validation
        }
        try {
            // Lưu hồ sơ
            apiResponse = createMedicalRecordCommand.addMedicalRecordByDoctor(file,addMedicalRecordRequest);
            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Add Successfully");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception ex) {

            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editMedicalRecordAndUploadImageByDoctor(@RequestParam("file") MultipartFile file,
                                                                     @ModelAttribute @Valid EditMedicalRecordRequest editMedicalRecordRequest, // sử dụng ModelAttribute để bind dữ liệu
                                                                     BindingResult bindingResult){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse); // Trả về lỗi 400 cho validation
        }
        try {

            apiResponse = editMedicalRecordCommand.editMedicalRecordByDoctor(file,editMedicalRecordRequest);
            apiResponse.setMessage("Edit Successfully");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
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

    @GetMapping("/list/patient/{patientId}")
    public ResponseEntity<?> getMedicalRecords(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size, @PathVariable Long patientId){
        try{
            return new ResponseEntity<>(getMedicalRecordWithPageByPatientQuery.getMedicalRecordsWithPageByPatientId(page, size, patientId), HttpStatus.OK);
        }catch (Exception ex){
            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @GetMapping("/list/patient")
    public ResponseEntity<?> getMedicalRecordsOfPatient(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size){
        try{
            return new ResponseEntity<>(getMedicalRecordOfPatientQuery.getMedicalRecordsWithPageOfPatient(page, size), HttpStatus.OK);
        }catch (Exception ex){
            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }


}
