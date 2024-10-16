package com.hhh.doctor_appointment_app.service.UserService.Command.ResetUserPassword;

import com.hhh.doctor_appointment_app.dto.mapper.UserMapper;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserChangePasswordRequest;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.*;
import com.hhh.doctor_appointment_app.service.EmailService.Query.GetEmailFromToken.GetEmailFromTokenQuery;
import com.nimbusds.jose.JOSEException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class ResetUserPasswordCommand {
    private static final Logger log = LoggerFactory.getLogger(ResetUserPasswordCommand.class);
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
    private GetEmailFromTokenQuery getEmailFromToken;

    public String resetUserPassword(String token, UserChangePasswordRequest request)
            throws ParseException, JOSEException {
        String email = getEmailFromToken.getEmailFromToken(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            return "Passwords do not match";
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Reset password successfully";
    }
}
