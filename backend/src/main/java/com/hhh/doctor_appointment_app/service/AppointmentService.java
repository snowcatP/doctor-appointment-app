package com.hhh.doctor_appointment_app.service;

import com.hhh.doctor_appointment_app.dto.request.AppointmentRequest.AppointmentByPatientRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public ApiResponse<Object> createAppointmentByPatient(AppointmentByPatientRequest appointmentByPatientRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{
            Patient patient = patientRepository.findById(appointmentByPatientRequest.getPatient_ID())
                    .orElseThrow(() -> new NotFoundException("Patient Not Found"));

            Doctor doctor = doctorRepository.findById(appointmentByPatientRequest.getDoctor_ID())
                    .orElseThrow(() -> new NotFoundException("Doctor Not Found"));

            Appointment appointment = Appointment.builder()
                    .fullname(appointmentByPatientRequest.getFullname())
                    .gender(appointmentByPatientRequest.isGender())
                    .phone(appointmentByPatientRequest.getPhone())
                    .email(appointmentByPatientRequest.getEmail())
                    .dateOfBirth(appointmentByPatientRequest.getDateOfBirth())
                    .dateBooking(appointmentByPatientRequest.getDateBooking())
                    .reason(appointmentByPatientRequest.getReason())
                    .patient(patient)
                    .doctor(doctor)
                    .build();

            if(appointment == null){
                appointmentRepository.saveAndFlush(appointment);
                return ApiResponse.builder()
                        .statusCode("200")
                        .message("Appointment Created Successfully !")
                        .build();
            }
            return ApiResponse.builder()
                    .statusCode("400")
                    .message("Appointment Created Failed !")
                    .build();

        }catch (Exception ex){
            return ApiResponse.builder()
                    .statusCode("400")
                    .message("Appointment Created Failed !")
                    .build();
        }
    }

}
