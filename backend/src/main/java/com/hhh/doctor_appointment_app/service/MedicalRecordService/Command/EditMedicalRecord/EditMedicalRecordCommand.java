package com.hhh.doctor_appointment_app.service.MedicalRecordService.Command.EditMedicalRecord;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.mapper.MedicalRecordMapper;
import com.hhh.doctor_appointment_app.dto.request.MedicalRecordRequest.EditMedicalRecordRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.MedicalRecordResponse.MedicalRecordResponse;
import com.hhh.doctor_appointment_app.entity.*;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.MedicalRecordRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import com.hhh.doctor_appointment_app.service.FirebaseStorageService;
import com.hhh.doctor_appointment_app.service.UserService.Query.FindUserByEmail.FindUserByEmailQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private FindUserByEmailQuery findUserByEmailQuery;
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<Object> editMedicalRecordByDoctor(MultipartFile file,EditMedicalRecordRequest editMedicalRecordRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{
            var context = SecurityContextHolder.getContext();
            String usernameDoctor = context.getAuthentication().getName();
            User userDoctor = findUserByEmailQuery.findUserByEmail(usernameDoctor)
                    .orElseThrow(() -> new NotFoundException("Doctor not found"));

            MedicalRecord existingMedicalRecord = medicalRecordRepository.findById(editMedicalRecordRequest.getMedicalRecordId()).
                    orElseThrow(() -> new NotFoundException("Medical Record Not Found"));

            Doctor doctor = doctorRepository.findDoctorByProfile_Email(usernameDoctor)
                    .orElseThrow(() -> new NotFoundException("Doctor Not Found"));

            if (!existingMedicalRecord.getAppointment().getDoctor().getId().equals(doctor.getId())) {
                throw new ApplicationException("You are not allowed to edit medical record for this appointment.");
            }

            //Check file has null ?
            if (!file.isEmpty()) {
                // Upload file to Firebase Storage if file not null
                String fileUrl = firebaseStorageService.uploadFile(file);
                editMedicalRecordRequest.setFilePath(fileUrl);
            }




            Patient patient = patientRepository.findById(editMedicalRecordRequest.getPatientId())
                    .orElseThrow(() -> new NotFoundException("Patient Not Found"));

            if(editMedicalRecordRequest.getFilePath()!=null){
                existingMedicalRecord.setFilePath(editMedicalRecordRequest.getFilePath());
            }
            existingMedicalRecord.setBloodType(editMedicalRecordRequest.getBloodType());
            existingMedicalRecord.setHeartRate(editMedicalRecordRequest.getHeartRate());
            existingMedicalRecord.setTemperature(existingMedicalRecord.getTemperature());
            existingMedicalRecord.setHeight(existingMedicalRecord.getHeight());
            existingMedicalRecord.setWeight(existingMedicalRecord.getWeight());
            existingMedicalRecord.setDescription(editMedicalRecordRequest.getDescription());
            existingMedicalRecord.setAllergies(editMedicalRecordRequest.getAllergies());

            existingMedicalRecord.setDiagnosis(editMedicalRecordRequest.getDiagnosis());
            existingMedicalRecord.setPrescription(editMedicalRecordRequest.getPrescription());
            existingMedicalRecord.setTreatmentPlan(editMedicalRecordRequest.getTreatmentPlan());
            existingMedicalRecord.setNote(editMedicalRecordRequest.getNote());

            existingMedicalRecord.setDoctorModified(doctor);

            medicalRecordRepository.saveAndFlush(existingMedicalRecord);
            MedicalRecordResponse medicalRecordResponse = medicalRecordMapper.toResponse(existingMedicalRecord);
            apiResponse.ok(medicalRecordResponse);
            return apiResponse;
        }catch (Exception ex){
            throw new ApplicationException(ex.getMessage());
        }
    }
}
