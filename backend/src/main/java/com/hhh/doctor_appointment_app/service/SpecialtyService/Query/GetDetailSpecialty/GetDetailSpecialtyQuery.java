package com.hhh.doctor_appointment_app.service.SpecialtyService.Query.GetDetailSpecialty;

import com.hhh.doctor_appointment_app.dto.mapper.SpecialtyMapper;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.SpecialtyResponse.SpecialtyResponse;
import com.hhh.doctor_appointment_app.entity.Specialty;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class GetDetailSpecialtyQuery {
    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private SpecialtyMapper specialtyMapper;
    public ApiResponse<?> getSpecialtyDetail(Long id){
        ApiResponse<SpecialtyResponse> apiResponse = new ApiResponse<>();
        try {
            Specialty specialty = specialtyRepository.findById(id).orElseThrow(() -> new NotFoundException("Specialty Not Found"));
            // Update

            SpecialtyResponse specialtyResponse = specialtyMapper.toResponse(specialty);
            specialtyResponse.setSpecialtyName(specialty.getSpecialtyName());
            specialtyResponse.setHeadDoctor(specialty.getHeadDoctor());
            specialtyResponse.setDoctorList(specialty.getDoctorList());
            apiResponse.ok(specialtyResponse);
            apiResponse.setMessage("Get Specialty's Information Successfully");
        }catch(NotFoundException ex){
            apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage(ex.getMessage());
        }catch(Exception ex){
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
        }
        return apiResponse;
    }
}
