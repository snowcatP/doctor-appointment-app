package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.authenticationRequest.AuthenticationRequest;
import com.hhh.doctor_appointment_app.dto.request.authenticationRequest.IntrospectRequest;
import com.hhh.doctor_appointment_app.dto.request.authenticationRequest.LogoutRequest;
import com.hhh.doctor_appointment_app.dto.request.authenticationRequest.RefreshTokenRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AuthenticationResponse;
import com.hhh.doctor_appointment_app.dto.response.IntrospectResponse;
import com.hhh.doctor_appointment_app.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/refreshToken")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .data(result)
                .build();
    }
}
