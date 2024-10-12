package com.hhh.doctor_appointment_app.service;

import com.hhh.doctor_appointment_app.exception.UnauthenticatedException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class EmailService {
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

    public void sendChangePasswordEmail(String email) {
        String emailToken = generateEmailToken(email);

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

    public String getEmailFromToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expirationTime.before(Date.from(Instant.now()))) {
            throw new UnauthenticatedException("Reset password token expired");
        }

        var verified = signedJWT.verify(verifier);
        if (!verified) {
            throw new UnauthenticatedException("Reset password token verification failed");
        }

        return signedJWT.getJWTClaimsSet().getSubject();
    }

    private String generateEmailToken(String email) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .issuer("docapp.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
