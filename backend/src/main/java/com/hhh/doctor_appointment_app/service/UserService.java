package com.hhh.doctor_appointment_app.service;
import com.hhh.doctor_appointment_app.dto.mapper.PatientMapper;
import com.hhh.doctor_appointment_app.dto.request.patientRequest.AddPatientRequest;
import com.hhh.doctor_appointment_app.dto.mapper.UserMapper;
import com.hhh.doctor_appointment_app.dto.request.userRequest.UserCreateRequest;
import com.hhh.doctor_appointment_app.dto.request.userRequest.UserUpdateProfileRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.UserResponse;
import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.enums.UserRole;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.exception.UserException;
import com.hhh.doctor_appointment_app.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Admin createAdmin(UserCreateRequest request) {
        if (checkUsernameExists(request.getEmail())) {
            throw new UserException("User already exists");
        }
        Admin newAdmin = userMapper.toAdmin(request);
        newAdmin.getProfile().setPassword(passwordEncoder.encode(request.getPassword()));
        newAdmin.getProfile().setActive(true);

        var role = roleRepository.findByRoleName(UserRole.ADMIN);
        newAdmin.getProfile().setRole(role);

        return adminRepository.save(newAdmin);
    }

    public Patient createPatient(UserCreateRequest request) {
        if (checkUsernameExists(request.getEmail())) {
            throw new UserException("User already exists");
        }
        Patient newPatient = userMapper.toPatient(request);
        newPatient.getProfile().setPassword(passwordEncoder.encode(request.getPassword()));
        newPatient.getProfile().setActive(true);
        var role = roleRepository.findByRoleName(UserRole.PATIENT);
        newPatient.getProfile().setRole(role);

        return patientRepository.save(newPatient);
    }

    @PostAuthorize("returnObject.profile.email == authentication.name")
    public Admin getAdminProfile(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = findUserByEmail(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return userMapper.toUserResponse(user);
    }
    public boolean updateUserProfile(UserUpdateProfileRequest request) {
        var user = findUserByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        User updatedUser = userMapper.toUser(request);
        return false;
    }

    public List<Admin> getAllAdmin() {
        return adminRepository.findAll();
    }

    private Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private boolean checkUsernameExists(String username) {
        return userRepository.existsByEmail(username);
    }

}
