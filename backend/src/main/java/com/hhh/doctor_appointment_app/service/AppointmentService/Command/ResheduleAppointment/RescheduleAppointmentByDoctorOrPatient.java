package com.hhh.doctor_appointment_app.service.AppointmentService.Command.ResheduleAppointment;

import com.hhh.doctor_appointment_app.dto.mapper.AppointmentMapper;
import com.hhh.doctor_appointment_app.dto.request.AppointmentRequest.RescheduleWithDateRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import com.hhh.doctor_appointment_app.state.AcceptState;
import com.hhh.doctor_appointment_app.state.RescheduledState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class RescheduleAppointmentByDoctorOrPatient {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public ApiResponse<Object> rescheduleAppointmentByDoctorOrPatient(Long id, RescheduleWithDateRequest rescheduleWithDateRequest) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            // Tìm kiếm Appointment theo id
            Appointment appointment = appointmentRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Appointment Not Found"));

            // Convert the dateBooking from Date to LocalDateTime
            LocalDateTime dateBooking = rescheduleWithDateRequest.getDateBooking()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            // Get the current date and time
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Check if the dateBooking is after the current date and time
            if (dateBooking.isBefore(currentDateTime)) {
                return ApiResponse.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message("The booking date and time must be in the future.")
                        .build();
            }

            // Thực hiện việc chuyển trạng thái sang RESCHEDULED
            if(appointment.getAppointmentState() instanceof AcceptState){
                ((AcceptState) appointment.getAppointmentState()).reschedule(appointment);
            } else {
                ((RescheduledState) appointment.getAppointmentState()).reschedule(appointment);
            }
            // Lưu lại appointment với trạng thái RESCHEDULED
            appointment.setDateBooking(rescheduleWithDateRequest.getDateBooking());
            appointment.setBookingHour(rescheduleWithDateRequest.getBookingHour());
            appointmentRepository.saveAndFlush(appointment);

            // Tạo phản hồi từ Appointment sau khi thay đổi
            AppointmentResponse appointmentResponse = appointmentMapper.toResponse(appointment);
            apiResponse.setMessage("Appointment Rescheduled Successfully!");
            apiResponse.ok(appointmentResponse);
            return apiResponse;

        } catch (NotFoundException ex) {
            return ApiResponse.builder()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .build();
        } catch (IllegalStateException ex) {
            return ApiResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(ex.getMessage())
                    .build();
        } catch (Exception ex) {
            return ApiResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Appointment Reschedule Failed!")
                    .build();
        }
    }
}
