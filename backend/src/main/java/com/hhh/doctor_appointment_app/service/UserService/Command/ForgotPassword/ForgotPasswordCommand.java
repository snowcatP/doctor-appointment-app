package com.hhh.doctor_appointment_app.service.UserService.Command.ForgotPassword;

import com.hhh.doctor_appointment_app.dto.mapper.UserMapper;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserForgotPasswordRequest;
import com.hhh.doctor_appointment_app.repository.*;
import com.hhh.doctor_appointment_app.service.EmailService.Command.SendChangePasswordEmail.SendChangePasswordEmailCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ForgotPasswordCommand {
    private static final Logger log = LoggerFactory.getLogger(ForgotPasswordCommand.class);
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
    private SendChangePasswordEmailCommand sendChangePasswordEmailCommand;

    public String forgotPassword(UserForgotPasswordRequest request) {
//        if (!checkUsernameExists(request.getEmail())){
//            return false;
//        }
        sendChangePasswordEmailCommand.sendChangePasswordEmail(request.getEmail());

        return "Send email successfully, please check your email to reset password";
    }
}
