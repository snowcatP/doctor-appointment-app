package com.hhh.doctor_appointment_app.service.UserService.Command.ForgotPassword;

import com.hhh.doctor_appointment_app.dto.mapper.UserMapper;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserForgotPasswordRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.*;
import com.hhh.doctor_appointment_app.service.EmailService.Command.SendChangePasswordEmail.SendChangePasswordEmailCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    public ApiResponse<Object> forgotPassword(UserForgotPasswordRequest request) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{
            System.out.println(request.getEmail());
            User user = userRepository.findByEmail(request.getEmail()).
                    orElseThrow(()-> new NotFoundException("Email does not exist in the system"));
            sendChangePasswordEmailCommand.sendChangePasswordEmail(user);
            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Reset password successfully");
        }catch (NotFoundException ex){
            apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage(ex.getMessage());
        }catch (Exception ex){
            apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage(ex.getMessage());
        }
        return apiResponse;
    }
}
