package com.hhh.doctor_appointment_app.mapper;

import com.hhh.doctor_appointment_app.dto.request.UserCreateRequest;
import com.hhh.doctor_appointment_app.entity.Admin;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public static Admin toAdmin(UserCreateRequest request) {
        Admin admin = new Admin();
        admin.setFullname(request.getFullname());
        admin.setEmail(request.getEmail());
        admin.setPassword(request.getPassword());
        admin.setPhone(request.getPhone());
        admin.setAddress(request.getAddress());
        admin.setDateOfBirth(request.getDateOfBirth());
        admin.setGender(request.getGender());
        admin.setUsername(request.getEmail());
        return admin;
    }
}
