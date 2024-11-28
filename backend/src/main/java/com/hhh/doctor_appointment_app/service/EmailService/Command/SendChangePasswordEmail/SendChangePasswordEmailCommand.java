package com.hhh.doctor_appointment_app.service.EmailService.Command.SendChangePasswordEmail;

import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.exception.UnauthenticatedException;
import com.hhh.doctor_appointment_app.repository.UserRepository;
import com.hhh.doctor_appointment_app.service.EmailService.Command.GenerateEmailToken.GenerateEmailTokenCommand;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SendChangePasswordEmailCommand {
    @Autowired
    private JavaMailSender mailSender;

    @NonFinal
    @Value("${email.sender}")
    private String sender;

    @NonFinal
    @Value("${spring.jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${application.apiEndpoint}")
    private String apiEndpoint;

    @NonFinal
    @Value("${origins.host}")
    private String homeEndpoint;

    @Autowired
    private GenerateEmailTokenCommand generateEmailTokenCommand;

    @Autowired
    private UserRepository userRepository;

    @Async
    public void sendChangePasswordEmail(User user) {

        String emailToken = generateEmailTokenCommand.generateEmailToken(user.getEmail());

//        String body = "To change your password, please click to the below link to navigate to change password page.\n" +
//                apiEndpoint + "/auth/reset-password?token=" + emailToken;

        String body = String.format(
                "Dear %s,\n\n" +
                        "To change your password, please click to the below link to navigate to change password page.\n" +
                        homeEndpoint + "/auth/reset-password?token=" + emailToken +
                        "\nIf you have any questions or need further assistance, please contact our support team.\n\n" +
                        "Thank you for trusting our service!\n\nBest regards,\nClinic Team",
                user.getFullName()
        );
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(user.getEmail());
            message.setSubject("Change password");
            message.setText(body);
            mailSender.send(message);
        } catch (Exception ex) {
            throw new UnauthenticatedException(ex.getMessage());
        }
    }
}
