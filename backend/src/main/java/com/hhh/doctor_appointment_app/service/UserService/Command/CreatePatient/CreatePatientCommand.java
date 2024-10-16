package com.hhh.doctor_appointment_app.service.UserService.Command.CreatePatient;

import com.hhh.doctor_appointment_app.dto.mapper.UserMapper;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserCreateRequest;
import com.hhh.doctor_appointment_app.entity.Patient;
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
public class CreatePatientCommand {
    private static final Logger log = LoggerFactory.getLogger(CreatePatientCommand.class);
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

    public Patient createPatient(UserCreateRequest request) {
        if (checkUsernameExistsQuery.checkUsernameExists(request.getEmail())) {
            throw new UserException("User already exists");
        }
        Patient newPatient = userMapper.toPatient(request);
        newPatient.getProfile().setPassword(passwordEncoder.encode(request.getPassword()));
        newPatient.getProfile().setActive(true);

        var role = roleRepository.findByRoleName(UserRole.PATIENT);
        newPatient.getProfile().setRole(role);

        return patientRepository.save(newPatient);
    }
}
