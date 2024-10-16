package com.hhh.doctor_appointment_app.service.UserService.Command.UpdateUserProfile;

import com.hhh.doctor_appointment_app.dto.mapper.UserMapper;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserUpdateProfileRequest;
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
public class UpdateUserProfileCommand {
    private static final Logger log = LoggerFactory.getLogger(UpdateUserProfileCommand.class);
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


    public User updateUserProfile(UserUpdateProfileRequest request) {
        if (!checkRightUserQuery.checkRightUser(request.getEmail())) {
            throw new UserException("Username does not match");
        }

        var user = findUserByEmailQuery.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setAddress(request.getAddress());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setGender(request.isGender());
        user.setPhone(request.getPhone());

        return userRepository.save(user);
    }
}
