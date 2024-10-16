package com.hhh.doctor_appointment_app.service.PatientService.Query.GetDetailPatient;

import com.hhh.doctor_appointment_app.dto.mapper.PatientMapper;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class GetDetailPatientQuery {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientMapper patientMapper;

    public ApiResponse<?> getPatientDetail(Long id){
        ApiResponse<PatientResponse> apiResponse = new ApiResponse<>();
        try {
            Patient patient = patientRepository.findById(id).orElseThrow(() -> new NotFoundException("Patient Not Found"));
            // Update
            PatientResponse patientResponse = patientMapper.toResponse(patient);
            apiResponse.ok(patientResponse);
            apiResponse.setMessage("Get Patient's Information Successfully");
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
