package com.hhh.doctor_appointment_app.service.MedicalRecordService.Command.EditMedicalRecord;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.mapper.MedicalRecordMapper;
import com.hhh.doctor_appointment_app.dto.request.MedicalRecordRequest.EditMedicalRecordRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.MedicalRecordResponse.MedicalRecordResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.MedicalRecord;
import com.hhh.doctor_appointment_app.entity.Patient;
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
public class EditMedicalRecordCommand {
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
    public ApiResponse<Object> editMedicalRecordByDoctor(Long id, MultipartFile file,EditMedicalRecordRequest editMedicalRecordRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{
            //Check file has null ?
            if (!file.isEmpty()) {
                // Upload file to Firebase Storage if file not null
                String fileUrl = firebaseStorageService.uploadFile(file);
                editMedicalRecordRequest.setFilePath(fileUrl);
            }

            MedicalRecord existingMedicalRecord = medicalRecordRepository.findById(id).
                    orElseThrow(() -> new NotFoundException("Medical Record Not Found"));

            Patient patient = patientRepository.findById(editMedicalRecordRequest.getPatient_id())
                    .orElseThrow(() -> new NotFoundException("Patient Not Found"));

            Doctor doctor = doctorRepository.findById(editMedicalRecordRequest.getDoctor_id())
                    .orElseThrow(() -> new NotFoundException("Doctor Not Found"));

            existingMedicalRecord.setDescription(editMedicalRecordRequest.getDescription());
            existingMedicalRecord.setFilePath(editMedicalRecordRequest.getFilePath());
            existingMedicalRecord.setPatient(patient);
            existingMedicalRecord.setDoctorModified(doctor);

            medicalRecordRepository.saveAndFlush(existingMedicalRecord);
            MedicalRecordResponse medicalRecordResponse = medicalRecordMapper.toResponse(existingMedicalRecord);
            apiResponse.ok(medicalRecordResponse);
            return apiResponse;
        }catch (Exception ex){
            throw new ApplicationException("An unexpected error occurred");
        }
    }
}
