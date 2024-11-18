package com.hhh.doctor_appointment_app.service.EmailService.Command.SendAppointmentNotification;

import com.hhh.doctor_appointment_app.exception.ApplicationException;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SendAppointmentNotificationCommand {
    @Autowired
    private JavaMailSender mailSender;

    @NonFinal
    @Value("${email.sender}")
    private String sender;

    public void sendAppointmentNotification(String email, String fullName,
                                            LocalDateTime dateBooking, String bookingHour, String doctorName) {
        String body = String.format(
                "Dear %s,\n\n" +
                        "Your appointment has been successfully scheduled with Dr. %s.\n\n" +
                        "Appointment Details:\n" +
                        "- Date: %s\n" +
                        "- Time: %s\n\n" +
                        "Thank you for choosing our service!\n\nBest regards,\nClinic Team",
                fullName, doctorName, dateBooking.toLocalDate(), bookingHour
        );

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(email);
            message.setSubject("Appointment Confirmation");
            message.setText(body);
            mailSender.send(message);
        } catch (Exception ex) {
            throw new ApplicationException("Failed to send email: " + ex.getMessage());
        }
    }
}
