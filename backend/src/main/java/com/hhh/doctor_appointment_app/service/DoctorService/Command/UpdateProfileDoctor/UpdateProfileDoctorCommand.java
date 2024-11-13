package com.hhh.doctor_appointment_app.service.DoctorService.Command.UpdateProfileDoctor;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.EditDoctorRequest;
import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.UpdateDoctorProfileRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Specialty;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.SpecialtyRepository;
import com.hhh.doctor_appointment_app.service.UserService.Query.FindUserByEmail.FindUserByEmailQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UpdateProfileDoctorCommand {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private FindUserByEmailQuery findUserByEmailQuery;
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<Object> updateProfileDoctor(UpdateDoctorProfileRequest updateDoctorProfileRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            var context = SecurityContextHolder.getContext();
            String username = context.getAuthentication().getName();
            User user = findUserByEmailQuery.findUserByEmail(username)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            Doctor existingDoctor = doctorRepository.findDoctorByProfile_Email(username)
                    .orElseThrow(() -> new NotFoundException("Doctor Not Found"));


            existingDoctor.getProfile().setFirstName(updateDoctorProfileRequest.getFirstName());
            existingDoctor.getProfile().setLastName(updateDoctorProfileRequest.getLastName());
            existingDoctor.getProfile().setFullName(updateDoctorProfileRequest.getFirstName() + " " + updateDoctorProfileRequest.getLastName());
            existingDoctor.getProfile().setGender(updateDoctorProfileRequest.isGender());
            existingDoctor.getProfile().setPhone(updateDoctorProfileRequest.getPhone());
            existingDoctor.getProfile().setDateOfBirth(updateDoctorProfileRequest.getDateOfBirth());
            existingDoctor.getProfile().setAddress(updateDoctorProfileRequest.getAddress());

            if(updateDoctorProfileRequest.getAvatarFilePath()!=null){
                existingDoctor.getProfile().setAvatarFilePath(updateDoctorProfileRequest.getAvatarFilePath());
            }

            doctorRepository.saveAndFlush(existingDoctor);

            DoctorResponse doctorResponse = doctorMapper.toResponse(existingDoctor);
            apiResponse.ok(doctorResponse);
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
