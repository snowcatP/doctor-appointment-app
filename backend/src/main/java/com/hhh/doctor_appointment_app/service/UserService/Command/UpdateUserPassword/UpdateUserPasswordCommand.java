package com.hhh.doctor_appointment_app.service.UserService.Command.UpdateUserPassword;

import com.hhh.doctor_appointment_app.dto.mapper.UserMapper;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserUpdatePasswordRequest;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.exception.UserException;
import com.hhh.doctor_appointment_app.repository.*;
import com.hhh.doctor_appointment_app.service.UserService.Query.CheckRightUser.CheckRightUserQuery;
import com.hhh.doctor_appointment_app.service.UserService.Query.FindUserByEmail.FindUserByEmailQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserPasswordCommand {
    private static final Logger log = LoggerFactory.getLogger(UpdateUserPasswordCommand.class);
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
    private CheckRightUserQuery checkRightUserQuery;
    @Autowired
    private FindUserByEmailQuery findUserByEmailQuery;

    public User updateUserPassword(UserUpdatePasswordRequest request) {
        if (!checkRightUserQuery.checkRightUser(request.getEmail())) {
            throw new UserException("Username does not match");
        }
        var user = findUserByEmailQuery.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.getPassword()
                .equals(passwordEncoder.encode(request.getOldPassword()))){
            throw new UserException("Password does not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return userRepository.save(user);

    }
}
