package com.hhh.doctor_appointment_app.service;

import com.hhh.doctor_appointment_app.dto.mapper.AppointmentMapper;
import com.hhh.doctor_appointment_app.dto.request.AppointmentRequest.AppointmentByGuestRequest;
import com.hhh.doctor_appointment_app.dto.request.AppointmentRequest.AppointmentByPatientRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.enums.AppointmentStatus;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    public ApiResponse<Object> createAppointmentByPatient(AppointmentByPatientRequest appointmentByPatientRequest) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
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
                    .appointmentStatus(AppointmentStatus.PENDING)
                    .build();


                appointmentRepository.saveAndFlush(appointment);
                AppointmentResponse appointmentResponse = appointmentMapper.toResponse(appointment);
                apiResponse.setMessage("Appointment Created Successfully !");
                apiResponse.ok(appointmentResponse);
                return apiResponse;


        } catch (NotFoundException ex) {
            return ApiResponse.builder()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .build();
        } catch (Exception ex) {
            return ApiResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Appointment Created Failed !")
                    .build();
        }
    }

    public ApiResponse<Object> createAppointmentByGuest(AppointmentByGuestRequest appointmentByGuestRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{

            Doctor doctor = doctorRepository.findById(appointmentByGuestRequest.getDoctor_ID())
                    .orElseThrow(() -> new NotFoundException("Doctor Not Found"));

            Appointment appointment = Appointment.builder()
                    .fullname(appointmentByGuestRequest.getFullname())
                    .gender(appointmentByGuestRequest.isGender())
                    .phone(appointmentByGuestRequest.getPhone())
                    .email(appointmentByGuestRequest.getEmail())
                    .dateOfBirth(appointmentByGuestRequest.getDateOfBirth())
                    .dateBooking(appointmentByGuestRequest.getDateBooking())
                    .reason(appointmentByGuestRequest.getReason())
                    .doctor(doctor)
                    .appointmentStatus(AppointmentStatus.PENDING)
                    .build();

                appointmentRepository.saveAndFlush(appointment);
                AppointmentResponse appointmentResponse = appointmentMapper.toResponse(appointment);
                apiResponse.setMessage("Appointment Created Successfully !");
                apiResponse.ok(appointmentResponse);
                return apiResponse;

        } catch (NotFoundException ex) {
            return ApiResponse.builder()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .build();
        }
        catch (Exception ex){
            return ApiResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Appointment Created Failed !")
                    .build();
        }
    }

    public ApiResponse<?> getAppointmentDetailByPatient(Long id){
        ApiResponse<AppointmentResponse> apiResponse = new ApiResponse<>();
        try {
            Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new NotFoundException("Appointment Not Found"));
            // Update
            AppointmentResponse appointmentResponse = appointmentMapper.toResponse(appointment);
            apiResponse.ok(appointmentResponse);
            apiResponse.setMessage("Get Appointment's Information Successfully");
        }catch(NotFoundException ex){
            apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage(ex.getMessage());
        }catch(Exception ex){
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
        }
        return apiResponse;
    }



}
