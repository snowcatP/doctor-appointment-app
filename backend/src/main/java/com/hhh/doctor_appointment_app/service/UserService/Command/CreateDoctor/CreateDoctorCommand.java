package com.hhh.doctor_appointment_app.service.UserService.Command.CreateDoctor;

import com.hhh.doctor_appointment_app.dto.mapper.UserMapper;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserCreateRequest;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.enums.UserRole;
import com.hhh.doctor_appointment_app.exception.UserException;
import com.hhh.doctor_appointment_app.repository.AdminRepository;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.RoleRepository;
import com.hhh.doctor_appointment_app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateDoctorCommand {
    private static final Logger log = LoggerFactory.getLogger(CreateDoctorCommand.class);
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    private boolean checkUsernameExists(String email) {
        return userRepository.existsByEmail(email);
    }
    public Doctor createDoctor(UserCreateRequest request) {
        if (checkUsernameExists(request.getEmail())) {
            throw new UserException("User already exists");
        }
        Doctor newDoctor = userMapper.toDoctor(request);
        newDoctor.getProfile().setPassword(passwordEncoder.encode(request.getPassword()));
        newDoctor.getProfile().setActive(true);

        var role = roleRepository.findByRoleName(UserRole.DOCTOR);
        newDoctor.getProfile().setRole(role);

        return doctorRepository.save(newDoctor);
    }


}
