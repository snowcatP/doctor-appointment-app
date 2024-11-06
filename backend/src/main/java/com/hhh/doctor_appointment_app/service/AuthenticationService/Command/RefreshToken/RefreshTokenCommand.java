package com.hhh.doctor_appointment_app.service.AuthenticationService.Command.RefreshToken;

import com.hhh.doctor_appointment_app.dto.mapper.UserMapper;
import com.hhh.doctor_appointment_app.dto.request.AuthenticationRequest.RefreshTokenRequest;
import com.hhh.doctor_appointment_app.dto.response.AuthResponse.AuthenticationResponse;
import com.hhh.doctor_appointment_app.entity.InvalidatedToken;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.UnauthenticatedException;
import com.hhh.doctor_appointment_app.repository.InvalidatedTokenRepository;
import com.hhh.doctor_appointment_app.repository.UserRepository;
import com.hhh.doctor_appointment_app.service.AuthenticationService.Command.GenerateToken.GenerateTokenCommand;
import com.hhh.doctor_appointment_app.service.AuthenticationService.Command.VerifyToken.VerifyTokenCommand;
import com.hhh.doctor_appointment_app.service.AuthenticationService.Query.FindUserByUsername.FindUserByUsernameQuery;
import com.nimbusds.jose.JOSEException;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class RefreshTokenCommand {
    private static final Logger log = LoggerFactory.getLogger(RefreshTokenCommand.class);
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

    @Autowired
    private FindUserByUsernameQuery findUserByUsernameQuery;

    @Autowired
    private GenerateTokenCommand generateTokenCommand;

    @Autowired
    private UserMapper userMapper;

    public AuthenticationResponse refreshToken(RefreshTokenRequest request)
            throws ParseException, JOSEException {
        var signJwt = verifyTokenCommand.verifyToken(request.getToken(), true);

        var jit = signJwt.getJWTClaimsSet().getJWTID();
        var expiryTime = signJwt.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        invalidatedRepository.save(invalidatedToken);

        var username = signJwt.getJWTClaimsSet().getSubject();
        User user = findUserByUsernameQuery.findUserByUsername(username)
                .orElseThrow(() -> new UnauthenticatedException("User not found"));

        var token = generateTokenCommand.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .user(userMapper.toUserResponse(user))
                .isAuthenticated(true)
                .build();
    }
}
