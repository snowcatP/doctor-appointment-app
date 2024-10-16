package com.hhh.doctor_appointment_app.service.AuthenticationService.Query.Introspect;

import com.hhh.doctor_appointment_app.dto.request.AuthenticationRequest.IntrospectRequest;
import com.hhh.doctor_appointment_app.dto.response.IntrospectResponse;
import com.hhh.doctor_appointment_app.exception.UnauthenticatedException;
import com.hhh.doctor_appointment_app.service.AuthenticationService.Command.VerifyToken.VerifyTokenCommand;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class IntrospectQuery {
    @Autowired
    private VerifyTokenCommand verifyTokenCommand;

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();

        boolean isValid = true;
        try {
            verifyTokenCommand.verifyToken(token, false);
        } catch (UnauthenticatedException e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }
}
