package com.hhh.doctor_appointment_app.service.AppointmentService.Command.CreateAppointment;

import com.hhh.doctor_appointment_app.dto.mapper.AppointmentMapper;
import com.hhh.doctor_appointment_app.dto.request.AppointmentRequest.AppointmentByGuestRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.enums.AppointmentStatus;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.service.EmailService.Command.SendAppointmentNotification.SendAppointmentNotificationCommand;
import com.hhh.doctor_appointment_app.service.Notification.Notification;
import com.hhh.doctor_appointment_app.service.Notification.NotificationService;
import com.hhh.doctor_appointment_app.service.NotificationService.Implement.BookingNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class CreateAppointmentByGuestCommand {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private BookingNotificationService bookingNotificationService;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private SendAppointmentNotificationCommand sendAppointmentNotificationCommand;
    @Autowired
    private NotificationService notificationService;


    @Transactional
    public ApiResponse<Object> createAppointmentByGuest(AppointmentByGuestRequest appointmentByGuestRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{
            Doctor doctor = doctorRepository.findById(appointmentByGuestRequest.getDoctorId())
                    .orElseThrow(() -> new NotFoundException("Doctor Not Found"));

            // Convert the dateBooking from Date to LocalDateTime
            LocalDateTime dateBooking = appointmentByGuestRequest.getDateBooking()
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

            // Check for duplicate appointment times
            boolean isDuplicate = appointmentRepository.existsByDoctor_IdAndDateBookingAndBookingHour(
                    doctor.getId(),
                    appointmentByGuestRequest.getDateBooking(), // Chuyển sang LocalDate để so sánh ngày
                    appointmentByGuestRequest.getBookingHour()
            );

            if (isDuplicate) {
                apiResponse.setMessage("This booking hour is already reserved. Please choose a different time.");
                apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return apiResponse;
            }

            // Generate reference code for the new appointment
            String referenceCode = UUID.randomUUID().toString();

            Appointment appointment = Appointment.builder()
                    .fullName(appointmentByGuestRequest.getFullName())
                    .phone(appointmentByGuestRequest.getPhone())
                    .email(appointmentByGuestRequest.getEmail())
                    .dateBooking(appointmentByGuestRequest.getDateBooking())
                    .bookingHour(appointmentByGuestRequest.getBookingHour())
                    .reason(appointmentByGuestRequest.getReason())
                    .cusType("GUEST")
                    .doctor(doctor)
                    .appointmentStatus(AppointmentStatus.PENDING)
                    .referenceCode(referenceCode)
                    .build();

            appointmentRepository.saveAndFlush(appointment);
//            notificationService.sendNotification(
//                    appointment.getPatient().getProfile().getId().toString(),
//                    Notification.builder()
//                            .status(appointment.getAppointmentStatus())
//                            .message(apiResponse.getMessage())
//                            .appointmentId(appointment.getId())
//                            .build()
//            );
            AppointmentResponse appointmentResponse = appointmentMapper.toResponse(appointment);

            bookingNotificationService.sendBookingMessage(
                    appointmentMapper.toBookingNotificationResponse(appointment)
            );

            sendAppointmentNotificationCommand.sendAppointmentNotification(
                    appointment.getEmail(),
                    appointment.getFullName(),
                    appointment.getPhone(),
                    appointment.getAppointmentStatus().toString(),
                    appointment.getReferenceCode(),
                    appointment.getDateBooking().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    appointment.getBookingHour(),
                    appointment.getDoctor().getProfile().getFullName()
            );

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
                    .message(ex.getMessage())
                    .build();
        }
    }
}
