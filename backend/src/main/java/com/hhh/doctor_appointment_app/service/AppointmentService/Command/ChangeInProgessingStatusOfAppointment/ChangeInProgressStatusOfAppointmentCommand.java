package com.hhh.doctor_appointment_app.service.AppointmentService.Command.ChangeInProgessingStatusOfAppointment;

import com.hhh.doctor_appointment_app.dto.mapper.AppointmentMapper;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.enums.AppointmentStatus;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import com.hhh.doctor_appointment_app.service.AppointmentService.Command.ChangeStatusAppointment.ChangeStatusAppointmentCommand;
import com.hhh.doctor_appointment_app.service.EmailService.Command.SendEmailWhenAppointmentStatusChange.SendEmailWhenAppointmentStatusChangeCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ChangeInProgressStatusOfAppointmentCommand {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private SendEmailWhenAppointmentStatusChangeCommand sendEmailWhenAppointmentStatusChangeCommand;

    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<Object> changeInProgressStatusOfAppointmentByDoctor(Long id) {
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

            String bookingHour = appointment.getBookingHour();
            LocalTime bookingStartTime;
            LocalTime bookingEndTime;

            try {
                // Tách giờ bắt đầu và kết thúc
                String[] timeRange = bookingHour.split(" - ");
                bookingStartTime = LocalTime.parse(timeRange[0], DateTimeFormatter.ofPattern("HH:mm"));
                bookingEndTime = LocalTime.parse(timeRange[1], DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Invalid bookingHour format. Expected format: 'HH:mm - HH:mm'", e);
            }

            LocalDateTime appointmentStartDateTime = LocalDateTime.of(dateBooking, bookingStartTime);
            LocalDateTime appointmentEndDateTime = LocalDateTime.of(dateBooking, bookingEndTime);

            // Logic chuyển đổi trạng thái
            if ((currentStatus == AppointmentStatus.ACCEPT || currentStatus == AppointmentStatus.RESCHEDULED) &&
                    now.isAfter(appointmentStartDateTime) && now.isBefore(appointmentEndDateTime)) {
                // Chỉ chuyển từ ACCEPT/RESCHEDULED -> IN_PROGRESS nếu thời gian hiện tại nằm trong khoảng thời gian khám
                appointment.setAppointmentStatus(AppointmentStatus.IN_PROGRESS);
                apiResponse.setMessage("Appointment status changed to IN_PROGRESS.");
            } else {
                apiResponse.setMessage("Cannot change status to IN_PROGRESS outside appointment time.");
                apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return apiResponse;
            }


            // Lưu trạng thái mới
            appointmentRepository.saveAndFlush(appointment);


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
