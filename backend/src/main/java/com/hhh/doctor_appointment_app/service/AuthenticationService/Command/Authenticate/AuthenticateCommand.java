package com.hhh.doctor_appointment_app.service.AuthenticationService.Command.Authenticate;

import com.hhh.doctor_appointment_app.dto.mapper.UserMapper;
import com.hhh.doctor_appointment_app.dto.request.AuthenticationRequest.AuthenticationRequest;
import com.hhh.doctor_appointment_app.dto.response.AuthResponse.AuthenticationResponse;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.UnauthenticatedException;
import com.hhh.doctor_appointment_app.service.AuthenticationService.Command.GenerateToken.GenerateTokenCommand;
import com.hhh.doctor_appointment_app.service.AuthenticationService.Query.FindUserByUsername.FindUserByUsernameQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateCommand {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FindUserByUsernameQuery findUserByUsernameQuery;

    @Autowired
    private GenerateTokenCommand generateTokenCommand;

    @Autowired
    private UserMapper userMapper;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = findUserByUsernameQuery.findUserByUsername(request.getEmail())
                .orElseThrow(() -> new UnauthenticatedException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthenticatedException("Invalid username or password");
        }

        var token = generateTokenCommand.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .user(userMapper.toUserResponse(user))
                .build();
    }

}
