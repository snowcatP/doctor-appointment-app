package com.hhh.doctor_appointment_app.service.DoctorService.Command.DeleteDoctor;

import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DeleteDoctorCommand {
    @Autowired
    private DoctorRepository doctorRepository;

    public ApiResponse<?> deleteByIdDoctor(Long id){
        ApiResponse<DoctorResponse> apiResponse = new ApiResponse<>();

        try {
            Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new NotFoundException("Doctor Not Found"));
            // Update
            doctorRepository.deleteById(doctor.getId());
            apiResponse.ok();
            apiResponse.setMessage("Doctor successfully deleted");
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
