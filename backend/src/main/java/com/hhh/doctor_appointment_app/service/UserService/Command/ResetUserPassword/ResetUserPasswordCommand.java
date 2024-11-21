package com.hhh.doctor_appointment_app.service.UserService.Command.ResetUserPassword;

import com.hhh.doctor_appointment_app.dto.mapper.UserMapper;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserChangePasswordRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.*;
import com.hhh.doctor_appointment_app.service.EmailService.Query.GetEmailFromToken.GetEmailFromTokenQuery;
import com.nimbusds.jose.JOSEException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    public ApiResponse<Object> resetUserPassword(String token, UserChangePasswordRequest request)
            throws ParseException, JOSEException {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{
            String email = getEmailFromToken.getEmailFromToken(token);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
                apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
                apiResponse.setMessage("Passwords do not match");
                return apiResponse;
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Reset password successfully");
            return apiResponse;
        }catch (NotFoundException ex){
            apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage(ex.getMessage());
        }catch (ApplicationException ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("Failed to change password");
        }

        return apiResponse;
    }
}
