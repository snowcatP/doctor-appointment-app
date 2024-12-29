package com.hhh.doctor_appointment_app.service.AppointmentService.Command.ChangeStatusAppointment;

import com.hhh.doctor_appointment_app.dto.mapper.AppointmentMapper;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.enums.AppointmentStatus;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import com.hhh.doctor_appointment_app.service.EmailService.Command.SendEmailWhenAppointmentStatusChange.SendEmailWhenAppointmentStatusChangeCommand;
import com.hhh.doctor_appointment_app.service.Notification.Notification;
import com.hhh.doctor_appointment_app.service.Notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    @Autowired
    private SendEmailWhenAppointmentStatusChangeCommand sendEmailWhenAppointmentStatusChangeCommand;
    @Autowired
    private NotificationService notificationService;

    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<Object> changeStatusAppointmentByDoctor(Long id) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            Appointment appointment = appointmentRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Appointment Not Found"));

            // Lấy trạng thái hiện tại
            AppointmentStatus currentStatus = appointment.getAppointmentStatus();
            LocalDateTime now = LocalDateTime.now();

            // Chuyển đổi `dateBooking` và `bookingHour` thành `LocalDateTime`
            LocalDate dateBooking = appointment.getDateBooking().toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = dateBooking.format(formatter);

            String bookingHour = appointment.getBookingHour();
            LocalTime bookingStartTime;

            try {
                // Tách giờ bắt đầu
                String startTime = bookingHour.split(" - ")[0];
                bookingStartTime = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Invalid bookingHour format. Expected format: 'HH:mm - HH:mm'", e);
            }

            LocalDateTime appointmentDateTime = LocalDateTime.of(dateBooking, bookingStartTime);

            // Logic chuyển đổi trạng thái
            if (currentStatus == AppointmentStatus.PENDING) {
                // Chuyển từ PENDING -> ACCEPT
                appointment.setAppointmentStatus(AppointmentStatus.ACCEPT);
                apiResponse.setMessage("Appointment status changed from PENDING to ACCEPT.");
            } else if ((currentStatus == AppointmentStatus.IN_PROGRESS) &&
                    now.isAfter(appointmentDateTime)) {
                // Chuyển từ IN_PROGRESS -> COMPLETED nếu đã qua thời gian hẹn
                appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);
                apiResponse.setMessage("Appointment status changed to COMPLETED.");
            } else {
                apiResponse.setMessage("Cannot change status to COMPLETED before appointment time.");
                apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return apiResponse;
            }

            // Lưu trạng thái mới
            appointmentRepository.saveAndFlush(appointment);
            notificationService.sendNotification(
                    appointment.getPatient().getProfile().getId().toString(),
                    Notification.builder()
                            .status(appointment.getAppointmentStatus())
                            .message("Appointment with Doctor " + appointment.getDoctor().getProfile().getFullName() + " on " + formattedDate + " at " + bookingHour + ": " + apiResponse.getMessage())
                            .appointmentId(appointment.getId())
                            .build()
            );

            sendEmailWhenAppointmentStatusChangeCommand.sendAppointmentNotificationWhenChangeStatus(
                    appointment.getEmail(),
                    appointment.getFullName(),
                    appointment.getPhone(),
                    appointment.getAppointmentStatus().toString(),
                    appointment.getReferenceCode(),
                    appointment.getDateBooking().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    appointment.getBookingHour(),
                    appointment.getDoctor().getProfile().getFullName()
            );

            AppointmentResponse appointmentResponse = appointmentMapper.toResponse(appointment);
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
                    .message(String.valueOf(ex))
                    .build();
        }
    }

}
