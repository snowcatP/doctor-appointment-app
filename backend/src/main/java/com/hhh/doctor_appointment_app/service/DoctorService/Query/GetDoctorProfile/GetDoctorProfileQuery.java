package com.hhh.doctor_appointment_app.service.DoctorService.Query.GetDoctorProfile;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.service.DoctorService.Query.CalculateAverageRatingDoctor.CalculateAverageRatingDoctorQuery;
import com.hhh.doctor_appointment_app.service.UserService.Query.FindUserByEmail.FindUserByEmailQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GetDoctorProfileQuery {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private CalculateAverageRatingDoctorQuery calculateAverageRatingDoctorQuery;

    @Autowired
    private FindUserByEmailQuery findUserByEmailQuery;

    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<?> getDoctorProfile(){
        ApiResponse<DoctorResponse> apiResponse = new ApiResponse<>();
        try {
            var context = SecurityContextHolder.getContext();
            String username = context.getAuthentication().getName();
            User user = findUserByEmailQuery.findUserByEmail(username)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            Doctor doctor = doctorRepository.findDoctorByProfile_Email(username).orElseThrow(() -> new NotFoundException("Doctor Not Found"));
            // Update
            DoctorResponse doctorResponse = doctorMapper.toResponse(doctor);
            double ratingOfDoctor = calculateAverageRatingDoctorQuery.calculateAverageRating(doctor.getId());
            doctorResponse.setAverageRating(ratingOfDoctor);
            apiResponse.ok(doctorResponse);
            apiResponse.setMessage("Get Doctor's Information Successfully");
        }catch(NotFoundException ex){
            apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage(ex.getMessage());
        }
        catch(Exception ex){
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
        }
        return apiResponse;
    }
}
