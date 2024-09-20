package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.UserDto;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.CreateUser(userDto));
    }
}
