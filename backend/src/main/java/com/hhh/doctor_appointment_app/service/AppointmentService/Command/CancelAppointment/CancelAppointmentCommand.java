package com.hhh.doctor_appointment_app.service.AppointmentService.Command.CancelAppointment;

import com.hhh.doctor_appointment_app.dto.mapper.AppointmentMapper;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.enums.AppointmentStatus;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import com.hhh.doctor_appointment_app.service.Notification.Notification;
import com.hhh.doctor_appointment_app.service.Notification.NotificationService;
import com.hhh.doctor_appointment_app.state.CancelledState;
import com.hhh.doctor_appointment_app.state.PendingState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class CancelAppointmentCommand {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private NotificationService notificationService;

    @PreAuthorize("hasAnyRole('DOCTOR')")
    public ApiResponse<Object> cancelAppointmentByDoctor(Long id) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            Appointment appointment = appointmentRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Appointment Not Found"));

            AppointmentStatus currentStatus = appointment.getAppointmentStatus();
            LocalDateTime now = LocalDateTime.now();

            // Change `dateBooking` & `bookingHour` to `LocalDateTime`
            LocalDate dateBooking = appointment.getDateBooking().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

            String bookingHour = appointment.getBookingHour();
            LocalTime bookingStartTime;

            try {
                String startTime = bookingHour.split(" - ")[0];
                bookingStartTime = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Invalid bookingHour format. Expected format: 'HH:mm - HH:mm'", e);
            }

            LocalDateTime appointmentDateTime = LocalDateTime.of(dateBooking, bookingStartTime);

            // Check logic
            if (currentStatus == AppointmentStatus.PENDING) {
                // Pending can be cancelled any time
                appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
                apiResponse.setMessage("Appointment cancelled successfully.");
            } else if (currentStatus == AppointmentStatus.ACCEPT || currentStatus == AppointmentStatus.RESCHEDULED) {
                // Accept or Reschedule just only be cancelled before 3 hours
                if (now.isBefore(appointmentDateTime.minusHours(3))) {
                    appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
                    apiResponse.setMessage("Appointment cancelled successfully.");
                } else {
                    apiResponse.setMessage("Cannot cancel the appointment within 3 hours before the scheduled time.");
                    apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    return apiResponse;
                }
            } else {
                // Other states are not allowed to be canceled.
                apiResponse.setMessage("Appointment cannot be cancelled in its current status.");
                apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return apiResponse;
            }

            // Save new status
            appointmentRepository.saveAndFlush(appointment);
            notificationService.sendNotification(
                    appointment.getPatient().getProfile().getId().toString(),
                    Notification.builder()
                            .status(appointment.getAppointmentStatus())
                            .message("We’re sorry, your appointment has been canceled due to unforeseen circumstances. Please check your notifications for more details.")
                            .appointmentId(appointment.getId())
                            .build()
            );
            AppointmentResponse appointmentResponse = appointmentMapper.toResponse(appointment);
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
                    .message("Appointment Cancelled Failed!")
                    .build();
        }
    }
    @PreAuthorize("hasAnyRole('PATIENT')")
    public ApiResponse<Object> cancelAppointmentByPatient(Long id) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            Appointment appointment = appointmentRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Appointment Not Found"));

            AppointmentStatus currentStatus = appointment.getAppointmentStatus();
            LocalDateTime now = LocalDateTime.now();

            // Change `dateBooking` & `bookingHour` to `LocalDateTime`
            LocalDate dateBooking = appointment.getDateBooking().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

            String bookingHour = appointment.getBookingHour();
            LocalTime bookingStartTime;

            try {
                String startTime = bookingHour.split(" - ")[0];
                bookingStartTime = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Invalid bookingHour format. Expected format: 'HH:mm - HH:mm'", e);
            }

            LocalDateTime appointmentDateTime = LocalDateTime.of(dateBooking, bookingStartTime);

            // Check logic
            if (currentStatus == AppointmentStatus.PENDING) {
                // Pending can be cancelled any time
                appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
                apiResponse.setMessage("Appointment cancelled successfully.");
            } else if (currentStatus == AppointmentStatus.ACCEPT || currentStatus == AppointmentStatus.RESCHEDULED) {
                // Accept or Reschedule just only be cancelled before 3 hours
                if (now.isBefore(appointmentDateTime.minusHours(3))) {
                    appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
                    apiResponse.setMessage("Appointment cancelled successfully.");
                } else {
                    apiResponse.setMessage("Cannot cancel the appointment within 3 hours before the scheduled time.");
                    apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    return apiResponse;
                }
            } else {
                // Other states are not allowed to be canceled.
                apiResponse.setMessage("Appointment cannot be cancelled in its current status.");
                apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return apiResponse;
            }

            // Save new status
            appointmentRepository.saveAndFlush(appointment);
            notificationService.sendNotification(
                    appointment.getDoctor().getProfile().getId().toString(),
                    Notification.builder()
                            .status(appointment.getAppointmentStatus())
                            .message("Patient" + appointment.getPatient().getProfile().getFullName() +" has canceled their appointment on" + appointmentDateTime + ", please check in Apppointment for more informations")
                            .appointmentId(appointment.getId())
                            .build()
            );
            AppointmentResponse appointmentResponse = appointmentMapper.toResponse(appointment);
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
                    .message("Appointment Cancelled Failed!")
                    .build();
        }
    }
}
