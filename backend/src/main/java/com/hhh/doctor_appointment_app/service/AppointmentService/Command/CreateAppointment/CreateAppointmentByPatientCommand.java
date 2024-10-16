package com.hhh.doctor_appointment_app.service.AppointmentService.Command.CreateAppointment;

import com.hhh.doctor_appointment_app.dto.mapper.AppointmentMapper;
import com.hhh.doctor_appointment_app.dto.request.AppointmentRequest.AppointmentByGuestRequest;
import com.hhh.doctor_appointment_app.dto.request.AppointmentRequest.AppointmentByPatientRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.enums.AppointmentStatus;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CreateAppointmentByPatientCommand {
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


}
