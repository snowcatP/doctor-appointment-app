package com.hhh.doctor_appointment_app.service.AuthenticationService.Command.Authenticate;

import com.hhh.doctor_appointment_app.dto.request.AuthenticationRequest.AuthenticationRequest;
import com.hhh.doctor_appointment_app.dto.response.AuthenticationResponse;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.UnauthenticatedException;
import com.hhh.doctor_appointment_app.repository.InvalidatedTokenRepository;
import com.hhh.doctor_appointment_app.repository.UserRepository;
import com.hhh.doctor_appointment_app.service.AuthenticationService.Command.GenerateToken.GenerateTokenCommand;
import com.hhh.doctor_appointment_app.service.AuthenticationService.Query.FindUserByUsername.FindUserByUsernameQuery;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateCommand {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${spring.jwt.valid-duration}")
    protected int VALIDATION_DURATION;

    @NonFinal
    @Value("${spring.jwt.refreshable-duration}")
    protected int REFRESHABLE_DURATION;

    @Autowired
    private FindUserByUsernameQuery findUserByUsernameQuery;

    @Autowired
    private GenerateTokenCommand generateTokenCommand;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = findUserByUsernameQuery.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthenticatedException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthenticatedException("Invalid username or password");
        }

        var token = generateTokenCommand.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

}
