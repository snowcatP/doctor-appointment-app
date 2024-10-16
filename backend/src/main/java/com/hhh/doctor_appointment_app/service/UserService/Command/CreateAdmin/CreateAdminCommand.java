package com.hhh.doctor_appointment_app.service.UserService.Command.CreateAdmin;

import com.hhh.doctor_appointment_app.dto.mapper.UserMapper;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.*;
import com.hhh.doctor_appointment_app.entity.Admin;
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
public class CreateAdminCommand {
    private static final Logger log = LoggerFactory.getLogger(CreateAdminCommand.class);
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

    public Admin createAdmin(UserCreateRequest request) {
        if (checkUsernameExistsQuery.checkUsernameExists(request.getEmail())) {
            throw new UserException("User already exists");
        }
        Admin newAdmin = userMapper.toAdmin(request);
        newAdmin.getProfile().setPassword(passwordEncoder.encode(request.getPassword()));
        newAdmin.getProfile().setActive(true);

        var role = roleRepository.findByRoleName(UserRole.ADMIN);
        newAdmin.getProfile().setRole(role);

        return adminRepository.save(newAdmin);
    }


}
