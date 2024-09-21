package com.hhh.doctor_appointment_app.service;

import com.hhh.doctor_appointment_app.dto.request.UserCreateRequest;
import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.exception.UserException;
import com.hhh.doctor_appointment_app.mapper.UserMapper;
import com.hhh.doctor_appointment_app.repository.AdminRepository;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Admin createAdmin(UserCreateRequest request) {
        if (checkUsernameExists(request.getEmail())) {
            throw new UserException("User already exists");
        }
        Admin newAdmin = UserMapper.toAdmin(request);
        newAdmin.setPassword(passwordEncoder.encode(request.getPassword()));
        return adminRepository.save(newAdmin);
    }

    public Patient createPatient(UserCreateRequest request) {
        if (checkUsernameExists(request.getEmail())) {
            throw new UserException("User already exists");
        }
        Patient newPatient = UserMapper.toPatient(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        newPatient.setPassword(passwordEncoder.encode(request.getPassword()));
        return patientRepository.save(newPatient);
    }

    public Admin getAdminProfile(Long id) {
        return adminRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }


    private boolean checkUsernameExists(String username) {
        return adminRepository.existsByUsername(username) ||
                patientRepository.existsByUsername(username) ||
                doctorRepository.existsByUsername(username);
    }
}
