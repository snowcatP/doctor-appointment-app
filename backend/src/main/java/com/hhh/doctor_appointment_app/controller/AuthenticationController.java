package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.AuthenticationRequest.AuthenticationRequest;
import com.hhh.doctor_appointment_app.dto.request.AuthenticationRequest.IntrospectRequest;
import com.hhh.doctor_appointment_app.dto.request.AuthenticationRequest.LogoutRequest;
import com.hhh.doctor_appointment_app.dto.request.AuthenticationRequest.RefreshTokenRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AuthResponse.AuthenticationResponse;
import com.hhh.doctor_appointment_app.dto.response.AuthResponse.LogoutResponse;
import com.hhh.doctor_appointment_app.dto.response.IntrospectResponse;
import com.hhh.doctor_appointment_app.service.AuthenticationService.Command.Authenticate.AuthenticateCommand;
import com.hhh.doctor_appointment_app.service.AuthenticationService.Command.Logout.LogoutCommand;
import com.hhh.doctor_appointment_app.service.AuthenticationService.Command.RefreshToken.RefreshTokenCommand;
import com.hhh.doctor_appointment_app.service.AuthenticationService.Query.Introspect.IntrospectQuery;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@CrossOrigin("*")
@RequestMapping("api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticateCommand authenticateCommand;

    @Autowired
    private IntrospectQuery introspectQuery;

    @Autowired
    private LogoutCommand logoutCommand;

    @Autowired
    private RefreshTokenCommand refreshTokenCommand;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticateCommand.authenticate(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = introspectQuery.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        logoutCommand.logout(request);
        return new ResponseEntity<>(LogoutResponse.builder().message("Logout Success").build(), HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request)
            throws ParseException, JOSEException {
        var result = refreshTokenCommand.refreshToken(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
