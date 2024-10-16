package com.hhh.doctor_appointment_app.service.MedicalRecordService.Command.CreateMedicalRecord;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.mapper.MedicalRecordMapper;
import com.hhh.doctor_appointment_app.dto.request.MedicalRecordRequest.AddMedicalRecordRequest;
import com.hhh.doctor_appointment_app.dto.request.MedicalRecordRequest.EditMedicalRecordRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.MedicalRecordResponse.MedicalRecordResponse;
import com.hhh.doctor_appointment_app.dto.response.PageResponse;
import com.hhh.doctor_appointment_app.entity.*;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.MedicalRecordRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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


    public ApiResponse<Object> addMedicalRecordByDoctor(AddMedicalRecordRequest addRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{
            Patient patient = patientRepository.findById(addRequest.getPatient_id())
                    .orElseThrow(() -> new NotFoundException("Patient Not Found"));

            Doctor doctor = doctorRepository.findById(addRequest.getDoctor_id())
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