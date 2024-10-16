package com.hhh.doctor_appointment_app.service.AuthenticationService.Command.Logout;

import com.hhh.doctor_appointment_app.dto.request.AuthenticationRequest.LogoutRequest;
import com.hhh.doctor_appointment_app.entity.InvalidatedToken;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.repository.InvalidatedTokenRepository;
import com.hhh.doctor_appointment_app.repository.UserRepository;
import com.hhh.doctor_appointment_app.service.AuthenticationService.Command.VerifyToken.VerifyTokenCommand;
import com.nimbusds.jose.JOSEException;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
public class LogoutCommand {
    private static final Logger log = LoggerFactory.getLogger(LogoutCommand.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InvalidatedTokenRepository invalidatedRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${spring.jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${spring.jwt.valid-duration}")
    protected int VALIDATION_DURATION;

    @NonFinal
    @Value("${spring.jwt.refreshable-duration}")
    protected int REFRESHABLE_DURATION;

    @Autowired
    private VerifyTokenCommand verifyTokenCommand;
    public void logout(LogoutRequest request)
            throws ParseException, JOSEException {
        try {
            var signToken = verifyTokenCommand.verifyToken(request.getToken(), true);
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedRepository.save(invalidatedToken);
        } catch (ApplicationException e) {
            log.error("Token already expired");
        }
    }

}
