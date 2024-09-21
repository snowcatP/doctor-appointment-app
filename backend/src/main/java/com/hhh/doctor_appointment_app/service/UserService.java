package com.hhh.doctor_appointment_app.service;

import com.hhh.doctor_appointment_app.dto.request.UserCreateRequest;
import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.mapper.UserMapper;
import com.hhh.doctor_appointment_app.repository.AdminRepository;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

    public Admin CreateAdmin(UserCreateRequest request) {
        if (adminRepository.existsByUsername(request.getEmail())) {
            throw new ApplicationException("User already exists");
        }
        Admin newAdmin = UserMapper.toAdmin(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        newAdmin.setPassword(passwordEncoder.encode(request.getPassword()));
        return adminRepository.save(newAdmin);
    }
}
