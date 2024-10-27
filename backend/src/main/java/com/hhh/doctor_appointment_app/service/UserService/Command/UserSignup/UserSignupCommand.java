package com.hhh.doctor_appointment_app.service.UserService.Command.UserSignup;

import com.hhh.doctor_appointment_app.dto.mapper.UserMapper;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserCreateRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserSignupRequest;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.enums.UserRole;
import com.hhh.doctor_appointment_app.exception.UserException;
import com.hhh.doctor_appointment_app.repository.*;
import com.hhh.doctor_appointment_app.service.UserService.Query.CheckUsernameExists.CheckUsernameExistsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserSignupCommand {
    private static final Logger log = LoggerFactory.getLogger(UserSignupCommand.class);
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

    @Autowired
    private CheckUsernameExistsQuery checkUsernameExistsQuery;

    public Patient userSignup(UserSignupRequest request) {
        if (checkUsernameExistsQuery.checkUsernameExists(request.getEmail())) {
            throw new UserException("Email is already used");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new UserException("Passwords do not match");
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var role = roleRepository.findByRoleName(UserRole.PATIENT);
        user.setRole(role);

        Patient patient = Patient.builder()
                .profile(user)
                .build();
        return patientRepository.save(patient);
    }
}
