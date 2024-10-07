package com.hhh.doctor_appointment_app.service;

import com.hhh.doctor_appointment_app.dto.mapper.PatientMapper;
import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.AddDoctorRequest;
import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.EditDoctorRequest;
import com.hhh.doctor_appointment_app.dto.request.PatientRequest.AddPatientRequest;
import com.hhh.doctor_appointment_app.dto.request.PatientRequest.EditPatientRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.PageResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientMapper patientMapper;

    public PageResponse<List<PatientResponse>> getPatientsWithPage(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Patient> patientPage = patientRepository.getPatientsWithPage(pageable);

        //Convert entities to responses
        List<PatientResponse> patientResponses = patientPage.getContent().stream()
                .map(patient -> {
                    PatientResponse response = new PatientResponse();
                    response.setId(patient.getId());
                    response.setFullname(patient.getFullname());
                    response.setGender(patient.isGender());
                    response.setPhone(patient.getPhone());
                    response.setEmail(patient.getEmail());
                    response.setDateOfBirth(patient.getDateOfBirth());
                    response.setAddress(patient.getAddress());
                    return response;
                })
                .collect(Collectors.toList());

        //Create PageResponse Object
        PageResponse<List<PatientResponse>> pageResponse = new PageResponse<>();
        pageResponse.ok(patientResponses);
        double total = Math.ceil((double) patientPage.getTotalElements() / size);
        pageResponse.setTotalPage((int) total);

        return pageResponse;
    }

    public ApiResponse<Object> addPatient(AddPatientRequest addPatientRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{
            Patient newPatient = new Patient();
            newPatient.setFullname(addPatientRequest.getFullname());
            newPatient.setGender(addPatientRequest.isGender());
            newPatient.setPhone(addPatientRequest.getPhone());
            newPatient.setEmail(addPatientRequest.getEmail());
            newPatient.setDateOfBirth(addPatientRequest.getDateOfBirth());
            newPatient.setAddress(addPatientRequest.getAddress());

            boolean isDuplicate = patientRepository.existsByEmail(newPatient.getEmail());
            if(isDuplicate){
                apiResponse.duplicatedCode();
                return apiResponse;
            }

            patientRepository.saveAndFlush(newPatient);
            PatientResponse patientResponse = patientMapper.toResponse(newPatient);
            apiResponse.ok(patientResponse);
            return apiResponse;
        }catch (Exception ex){
            throw new ApplicationException("An unexpected error occurred");
        }
    }

    public ApiResponse<Object> editPatient(Long id, EditPatientRequest editPatientRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            Patient existingPatient = patientRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Patient Not Found"));

            // Kiểm tra xem email trong edit request có khác với email hiện tại hay không
            if (!editPatientRequest.getEmail().equals(existingPatient.getEmail())) {
                boolean isDuplicate = patientRepository.existsByEmail(editPatientRequest.getEmail());
                if (isDuplicate) {
                    // Nếu email đã tồn tại, trả về lỗi duplicate
                    apiResponse.duplicatedCode();
                    return apiResponse;
                }
            }

            existingPatient.setFullname(editPatientRequest.getFullname());
            existingPatient.setGender(editPatientRequest.isGender());
            existingPatient.setPhone(editPatientRequest.getPhone());
            existingPatient.setEmail(editPatientRequest.getEmail());
            existingPatient.setDateOfBirth(editPatientRequest.getDateOfBirth());
            existingPatient.setAddress(editPatientRequest.getAddress());

            patientRepository.saveAndFlush(existingPatient);

            PatientResponse patientResponse = patientMapper.toResponse(existingPatient);
            apiResponse.ok(patientResponse);
        } catch (ApplicationException ex) {
            apiResponse.setStatusCode("400");
            apiResponse.setMessage("Failed to update");
        }
        return apiResponse;
    }

    public ApiResponse<?> deleteByIdPatient(Long id){
        ApiResponse<PatientResponse> apiResponse = new ApiResponse<>();

        try {
            Patient patient = patientRepository.findById(id).orElseThrow(() -> new NotFoundException("Patient Not Found"));
            // Update
            patientRepository.deleteById(patient.getId());
            apiResponse.ok();
            apiResponse.setMessage("Patient successfully deleted");
        }catch(Exception ex){
            apiResponse.setStatusCode("500");
            apiResponse.setMessage(ex.getMessage());
        }
        return apiResponse;
    }

    public ApiResponse<?> getPatientDetail(Long id){
        ApiResponse<PatientResponse> apiResponse = new ApiResponse<>();
        try {
            Patient patient = patientRepository.findById(id).orElseThrow(() -> new NotFoundException("Patient Not Found"));
            // Update
            PatientResponse patientResponse = patientMapper.toResponse(patient);
            apiResponse.ok(patientResponse);
            apiResponse.setMessage("Get Patient's Information Successfully");
        }catch(Exception ex){
            apiResponse.setStatusCode("500");
            apiResponse.setMessage(ex.getMessage());
        }
        return apiResponse;
    }
}