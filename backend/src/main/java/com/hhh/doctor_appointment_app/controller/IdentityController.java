package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/identity")
public class IdentityController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin/{id}")
    public ResponseEntity<Admin> getAdminProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getAdminProfile(id));
    }
}
