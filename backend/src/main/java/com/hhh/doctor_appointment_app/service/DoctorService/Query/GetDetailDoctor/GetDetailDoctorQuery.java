package com.hhh.doctor_appointment_app.service.DoctorService.Query.GetDetailDoctor;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class GetDetailDoctorQuery {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorMapper doctorMapper;

    public ApiResponse<?> getDoctorDetail(Long id){
        ApiResponse<DoctorResponse> apiResponse = new ApiResponse<>();
        try {
            Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new NotFoundException("Doctor Not Found"));
            // Update
            DoctorResponse doctorResponse = doctorMapper.toResponse(doctor);
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
