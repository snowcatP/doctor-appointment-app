package com.hhh.doctor_appointment_app.service.NurseService.Query.GetNurseProfile;
import com.hhh.doctor_appointment_app.dto.mapper.NurseMapper;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.NurseResponse.NurseResponse;
import com.hhh.doctor_appointment_app.entity.Nurse;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.NurseRepository;
import com.hhh.doctor_appointment_app.service.UserService.Query.FindUserByEmail.FindUserByEmailQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GetNurseProfileQuery {
    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private NurseMapper nurseMapper;

    @Autowired
    private FindUserByEmailQuery findUserByEmailQuery;

    @PreAuthorize("hasRole('NURSE')")
    public ApiResponse<?> getNurseProfile(){
        ApiResponse<NurseResponse> apiResponse = new ApiResponse<>();
        try {
            var context = SecurityContextHolder.getContext();
            String username = context.getAuthentication().getName();
            User user = findUserByEmailQuery.findUserByEmail(username)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            Nurse nurse = nurseRepository.findNurseByProfile_Email(username).orElseThrow(() -> new NotFoundException("Nurse Not Found"));
            // Update
            NurseResponse nurseResponse = nurseMapper.toResponse(nurse);

            apiResponse.setData(nurseResponse);
            apiResponse.setStatusCode(HttpStatus.OK.value());
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
