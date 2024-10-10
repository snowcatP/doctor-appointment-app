package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.userRequest.UserUpdateProfileRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class AccountController {

    @Autowired
    private UserService userService;


}
