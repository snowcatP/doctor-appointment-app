package com.hhh.doctor_appointment_app.service.PatientService.Query.GetProfilePatient;

import com.hhh.doctor_appointment_app.configuration.CustomJwtDecoder;
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
public class GetProfilePatientQuery {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private CustomJwtDecoder customJwtDecoder;

    public ApiResponse<?> getPatientProfile(String token){
        ApiResponse<PatientResponse> apiResponse = new ApiResponse<>();
        try {
            // Lấy email từ token
            String email = customJwtDecoder.getEmailFromToken(token);

            Patient patient = patientRepository.findPatientByProfile_Email(email).
                    orElseThrow(() -> new NotFoundException("Patient Not Found"));
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
