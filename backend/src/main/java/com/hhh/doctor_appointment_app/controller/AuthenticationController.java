package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.AuthenticationRequest;
import com.hhh.doctor_appointment_app.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) {
        boolean result = authenticationService.authenticate(request);
        if (result) {
            return ResponseEntity.ok().body("success");
        }
        return ResponseEntity.badRequest().body("Authentication failed");
    }
}
