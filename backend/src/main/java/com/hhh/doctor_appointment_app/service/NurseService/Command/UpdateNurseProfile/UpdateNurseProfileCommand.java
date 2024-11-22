package com.hhh.doctor_appointment_app.service.NurseService.Command.UpdateNurseProfile;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.mapper.NurseMapper;
import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.UpdateDoctorProfileRequest;
import com.hhh.doctor_appointment_app.dto.request.NurseRequest.UpdateNurseProfileRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.NurseResponse.NurseResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Nurse;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.NurseRepository;
import com.hhh.doctor_appointment_app.repository.SpecialtyRepository;
import com.hhh.doctor_appointment_app.service.UserService.Query.FindUserByEmail.FindUserByEmailQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UpdateNurseProfileCommand {
    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private NurseMapper nurseMapper;

    @Autowired
    private FindUserByEmailQuery findUserByEmailQuery;
    @PreAuthorize("hasRole('NURSE')")
    public ApiResponse<Object> updateNurseProfile(UpdateNurseProfileRequest updateNurseProfileRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            var context = SecurityContextHolder.getContext();
            String username = context.getAuthentication().getName();
            User user = findUserByEmailQuery.findUserByEmail(username)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            Nurse nurse = nurseRepository.findNurseByProfile_Email(username)
                    .orElseThrow(() -> new NotFoundException("Doctor Not Found"));


            nurse.getProfile().setFirstName(updateNurseProfileRequest.getFirstName());
            nurse.getProfile().setLastName(updateNurseProfileRequest.getLastName());
            nurse.getProfile().setFullName(updateNurseProfileRequest.getFirstName() + " " + updateNurseProfileRequest.getLastName());
            nurse.getProfile().setGender(updateNurseProfileRequest.isGender());
            nurse.getProfile().setPhone(updateNurseProfileRequest.getPhone());
            nurse.getProfile().setDateOfBirth(updateNurseProfileRequest.getDateOfBirth());
            nurse.getProfile().setAddress(updateNurseProfileRequest.getAddress());

            if(updateNurseProfileRequest.getAvatarFilePath()!=null){
                nurse.getProfile().setAvatarFilePath(updateNurseProfileRequest.getAvatarFilePath());
            }

            nurseRepository.saveAndFlush(nurse);

            NurseResponse nurseResponse = nurseMapper.toResponse(nurse);
            apiResponse.setData(nurseResponse);
            apiResponse.setMessage("Update Nurse's Information Successfully");
            apiResponse.setStatusCode(HttpStatus.OK.value());

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
