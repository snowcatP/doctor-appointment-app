package com.hhh.doctor_appointment_app.service.UserService.Command.UpdateUserProfile;

import com.hhh.doctor_appointment_app.dto.mapper.UserMapper;
import com.hhh.doctor_appointment_app.dto.request.PatientRequest.EditPatientRequest;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.UserUpdateProfileRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
import com.hhh.doctor_appointment_app.dto.response.UserResponse;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.exception.UserException;
import com.hhh.doctor_appointment_app.repository.*;
import com.hhh.doctor_appointment_app.service.UserService.Query.CheckRightUser.CheckRightUserQuery;
import com.hhh.doctor_appointment_app.service.UserService.Query.FindUserByEmail.FindUserByEmailQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserProfileCommand {
    private static final Logger log = LoggerFactory.getLogger(UpdateUserProfileCommand.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CheckRightUserQuery checkRightUserQuery;

    @Autowired
    private FindUserByEmailQuery findUserByEmailQuery;


    public ApiResponse<Object> updateUserProfile(UserUpdateProfileRequest request){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
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

            if(request.getAvatarFilePath()!=null){
                user.setAvatarFilePath(request.getAvatarFilePath());
            }

            userRepository.save(user);

            UserResponse userResponse = userMapper.toUserResponse(user);
            apiResponse.ok(userResponse);
        } catch(NotFoundException ex){
            apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage(ex.getMessage());
        }catch (ApplicationException ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("Failed to update");
        }
        return apiResponse;
    }

}
