package com.hhh.doctor_appointment_app.service.AppointmentService.Command.ChangeStatusAppointment;

import com.hhh.doctor_appointment_app.dto.mapper.AppointmentMapper;
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
import com.hhh.doctor_appointment_app.service.AuthenticationService.Command.GenerateToken.GenerateTokenCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class ChangeStatusAppointmentCommand {
    private static final Logger log = LoggerFactory.getLogger(ChangeStatusAppointmentCommand.class);

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<Object> changeStatusAppointmentByDoctor(Long id) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            Appointment appointment = appointmentRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Appointment Not Found"));

            // Gọi phương thức next() để chuyển trạng thái sang trạng thái tiếp theo
            appointment.nextState();


            appointmentRepository.saveAndFlush(appointment);
            AppointmentResponse appointmentResponse = appointmentMapper.toResponse(appointment);
            apiResponse.setMessage("Change Status Successfully !");
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
//                    .message("Change Status Failed !")
                    .message(String.valueOf(ex))
                    .build();
        }
    }

}
