package com.hhh.doctor_appointment_app.service.SpecialtyService.Command.DeleteSpecialty;

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
public class DeleteSpecialtyCommand {
    @Autowired
    private SpecialtyRepository specialtyRepository;

    public ApiResponse<?> deleteByIdSpecialty(Long id){
        ApiResponse<SpecialtyResponse> apiResponse = new ApiResponse<>();

        try {
            Specialty specialty = specialtyRepository.findById(id).orElseThrow(() -> new NotFoundException("Specialty Not Found"));
            specialtyRepository.deleteById(specialty.getId());
            apiResponse.ok();
            apiResponse.setMessage("Specialty successfully deleted");
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
