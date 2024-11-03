package com.hhh.doctor_appointment_app.service.MedicalRecordService.Command.CreateMedicalRecord;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.mapper.MedicalRecordMapper;
import com.hhh.doctor_appointment_app.dto.request.MedicalRecordRequest.AddMedicalRecordRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.MedicalRecordResponse.MedicalRecordResponse;
import com.hhh.doctor_appointment_app.entity.*;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.MedicalRecordRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import com.hhh.doctor_appointment_app.service.FirebaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CreateMedicalRecordCommand {
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<Object> addMedicalRecordByDoctor(MultipartFile file, AddMedicalRecordRequest addRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{
            //Check file has null ?
            if (!file.isEmpty()) {
                // Upload file to Firebase Storage if file not null
                String fileUrl = firebaseStorageService.uploadFile(file);
                addRequest.setFilePath(fileUrl);
            }

            Patient patient = patientRepository.findById(addRequest.getPatientId())
                    .orElseThrow(() -> new NotFoundException("Patient Not Found"));

            Doctor doctor = doctorRepository.findById(addRequest.getDoctorId())
                    .orElseThrow(() -> new NotFoundException("Doctor Not Found"));

            MedicalRecord medicalRecord = new MedicalRecord();

            medicalRecord.setDescription(addRequest.getDescription());
            medicalRecord.setFilePath(addRequest.getFilePath());
            medicalRecord.setPatient(patient);
            medicalRecord.setDoctorModified(doctor);

            medicalRecordRepository.saveAndFlush(medicalRecord);
            MedicalRecordResponse medicalRecordResponse = medicalRecordMapper.toResponse(medicalRecord);
            apiResponse.ok(medicalRecordResponse);
            return apiResponse;
        }catch (Exception ex){
            throw new ApplicationException("An unexpected error occurred");
        }
    }

}
