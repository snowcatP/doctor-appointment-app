package com.hhh.doctor_appointment_app.service;

import com.hhh.doctor_appointment_app.dto.request.UserCreateRequest;
import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.entity.Role;
import com.hhh.doctor_appointment_app.enums.UserRole;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.exception.UserException;
import com.hhh.doctor_appointment_app.mapper.UserMapper;
import com.hhh.doctor_appointment_app.repository.AdminRepository;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import com.hhh.doctor_appointment_app.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Admin createAdmin(UserCreateRequest request) {
        if (checkUsernameExists(request.getEmail())) {
            throw new UserException("User already exists");
        }
        Admin newAdmin = UserMapper.toAdmin(request);
        newAdmin.setPassword(passwordEncoder.encode(request.getPassword()));

        var role = roleRepository.findByRoleName(UserRole.ADMIN);;
        newAdmin.setRole(role);

        return adminRepository.save(newAdmin);
    }

    public Patient createPatient(UserCreateRequest request) {
        if (checkUsernameExists(request.getEmail())) {
            throw new UserException("User already exists");
        }
        Patient newPatient = UserMapper.toPatient(request);
        newPatient.setPassword(passwordEncoder.encode(request.getPassword()));

        var role = roleRepository.findByRoleName(UserRole.PATIENT);
        newPatient.setRole(role);

        return patientRepository.save(newPatient);
    }

    public Admin getAdminProfile(Long id) {
        return adminRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public List<Admin> getAllAdmin() {
        return adminRepository.findAll();
    }

    private boolean checkUsernameExists(String username) {
        return adminRepository.existsByUsername(username) ||
                patientRepository.existsByUsername(username) ||
                doctorRepository.existsByUsername(username);
    }
}
