package com.hhh.doctor_appointment_app.service.MedicalRecordService.Command.CreateMedicalRecord;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.mapper.MedicalRecordMapper;
import com.hhh.doctor_appointment_app.dto.request.MedicalRecordRequest.AddMedicalRecordRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.MedicalRecordResponse.MedicalRecordResponse;
import com.hhh.doctor_appointment_app.entity.*;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.MedicalRecordRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import com.hhh.doctor_appointment_app.service.FirebaseStorageService;
import com.hhh.doctor_appointment_app.service.UserService.Query.FindUserByEmail.FindUserByEmailQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private FindUserByEmailQuery findUserByEmailQuery;

    @Autowired
    private AppointmentRepository appointmentRepository;
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<Object> addMedicalRecordByDoctor(MultipartFile file, AddMedicalRecordRequest addRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{
            var context = SecurityContextHolder.getContext();
            String usernameDoctor = context.getAuthentication().getName();
            User userDoctor = findUserByEmailQuery.findUserByEmail(usernameDoctor)
                    .orElseThrow(() -> new NotFoundException("Doctor not found"));

            //Check file has null ?
            if (!file.isEmpty()) {
                // Upload file to Firebase Storage if file not null
                String fileUrl = firebaseStorageService.uploadFile(file);
                addRequest.setFilePath(fileUrl);
            }

            Patient patient = patientRepository.findById(addRequest.getPatientId())
                    .orElseThrow(() -> new NotFoundException("Patient Not Found"));

            Doctor doctor = doctorRepository.findDoctorByProfile_Email(usernameDoctor)
                    .orElseThrow(() -> new NotFoundException("Doctor Not Found"));

            Appointment appointment = appointmentRepository.findById(addRequest.getAppointmentId())
                    .orElseThrow(() -> new NotFoundException("Appointment Not Found"));

            MedicalRecord medicalRecord = new MedicalRecord();

            medicalRecord.setDescription(addRequest.getDescription());
            medicalRecord.setFilePath(addRequest.getFilePath());
            medicalRecord.setPatient(patient);
            medicalRecord.setDoctorModified(doctor);

            appointment.setMedicalRecord(medicalRecord);
//            appointmentRepository.saveAndFlush(appointment);

            medicalRecord.setAppointment(appointment);
            medicalRecordRepository.saveAndFlush(medicalRecord);

            MedicalRecordResponse medicalRecordResponse = medicalRecordMapper.toResponse(medicalRecord);
            apiResponse.ok(medicalRecordResponse);
            return apiResponse;
        }catch (NotFoundException ex) {
            return ApiResponse.builder()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .build();
        } catch (Exception ex){
            throw new ApplicationException(ex.getMessage());
        }
    }

}
