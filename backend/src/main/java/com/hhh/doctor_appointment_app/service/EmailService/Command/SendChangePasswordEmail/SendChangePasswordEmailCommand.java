package com.hhh.doctor_appointment_app.service.EmailService.Command.SendChangePasswordEmail;

import com.hhh.doctor_appointment_app.exception.UnauthenticatedException;
import com.hhh.doctor_appointment_app.service.EmailService.Command.GenerateEmailToken.GenerateEmailTokenCommand;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    @Autowired
    private GenerateEmailTokenCommand generateEmailTokenCommand;
    public void sendChangePasswordEmail(String email) {
        String emailToken = generateEmailTokenCommand.generateEmailToken(email);

        String body = "To change your password, please click to the below link to navigate to change password page.\n" +
                apiEndpoint + "/auth/reset-password?token=" + emailToken;

        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo("xuanhoang949@gmail.com");
            message.setSubject("Change password");
            message.setText(body);
            mailSender.send(message);
        } catch (Exception ex){
            throw new UnauthenticatedException(ex.getMessage());
        }
    }
}
