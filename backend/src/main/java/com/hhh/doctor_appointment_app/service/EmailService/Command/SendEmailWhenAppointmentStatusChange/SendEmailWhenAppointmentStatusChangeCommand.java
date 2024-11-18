package com.hhh.doctor_appointment_app.service.EmailService.Command.SendEmailWhenAppointmentStatusChange;

import com.hhh.doctor_appointment_app.exception.ApplicationException;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SendEmailWhenAppointmentStatusChangeCommand {
    @Autowired
    private JavaMailSender mailSender;

    @NonFinal
    @Value("${email.sender}")
    private String sender;

    @Async
    public void sendAppointmentNotificationWhenChangeStatus(String email, String fullName, String phone, String status, String code,
                                            LocalDateTime dateBooking, String bookingHour, String doctorName) {
        String body = String.format(
                "Dear %s,\n\n" +
                        "We would like to inform you that the status of your appointment with Dr. %s has been updated.\n\n" +
                        "Updated Appointment Details:\n" +
                        "- Full Name: %s\n" +
                        "- Phone: %s\n" +
                        "- Appointment Date: %s\n" +
                        "- Time: %s\n" +
                        "- Status: %s\n" +
                        "- Appointment Code: %s\n\n" +
                        "If you have any questions or need further assistance, please contact our support team.\n\n" +
                        "Thank you for trusting our service!\n\nBest regards,\nClinic Team",
                fullName, doctorName, fullName, phone, dateBooking.toLocalDate(), bookingHour, status, code
        );

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(email);
            message.setSubject("Appointment Status Update");
            message.setText(body);
            mailSender.send(message);
        } catch (Exception ex) {
            throw new ApplicationException("Failed to send email: " + ex.getMessage());
        }
    }
}
