package com.hhh.doctor_appointment_app.service.PatientService.Command.EditPatient;

import com.hhh.doctor_appointment_app.dto.mapper.PatientMapper;
import com.hhh.doctor_appointment_app.dto.request.PatientRequest.EditPatientRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class EditPatientCommand {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientMapper patientMapper;

    public ApiResponse<Object> editPatient(Long id, EditPatientRequest editPatientRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            Patient existingPatient = patientRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Patient Not Found"));

            // Kiểm tra xem email trong edit request có khác với email hiện tại hay không
            if (!editPatientRequest.getEmail().equals(existingPatient.getProfile().getEmail())) {
                boolean isDuplicate = patientRepository.existsByProfile_Email(editPatientRequest.getEmail());
                if (isDuplicate) {
                    // Nếu email đã tồn tại, trả về lỗi duplicate
                    apiResponse.duplicatedCode();
                    return apiResponse;
                }
            }

            existingPatient.getProfile().setFirstName(editPatientRequest.getFirstName());
            existingPatient.getProfile().setLastName(editPatientRequest.getLastName());
            existingPatient.getProfile().setGender(editPatientRequest.isGender());
            existingPatient.getProfile().setPhone(editPatientRequest.getPhone());
            existingPatient.getProfile().setEmail(editPatientRequest.getEmail());
            existingPatient.getProfile().setDateOfBirth(editPatientRequest.getDateOfBirth());
            existingPatient.getProfile().setAddress(editPatientRequest.getAddress());

            patientRepository.saveAndFlush(existingPatient);

            PatientResponse patientResponse = patientMapper.toResponse(existingPatient);
            apiResponse.ok(patientResponse);
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
