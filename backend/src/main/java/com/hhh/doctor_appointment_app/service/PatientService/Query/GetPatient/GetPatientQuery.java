package com.hhh.doctor_appointment_app.service.PatientService.Query.GetPatient;

import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetPatientQuery {
    @Autowired
    private PatientRepository patientRepository;

    public List<PatientResponse> getPatients  (){
        List<Patient> patients = patientRepository.findAll();
        //Convert entities to responses
        List<PatientResponse> patientResponses = patients.stream()
                .map(patient -> {
                    PatientResponse response = new PatientResponse();
                    response.setId(patient.getId());
                    response.setFirstName(patient.getProfile().getFirstName());
                    response.setLastName(patient.getProfile().getLastName());
                    response.setFullName(patient.getProfile().getFirstName() + " " + patient.getProfile().getLastName());
                    response.setGender(patient.getProfile().isGender());
                    response.setPhone(patient.getProfile().getPhone());
                    response.setEmail(patient.getProfile().getEmail());
                    response.setDateOfBirth(patient.getProfile().getDateOfBirth());
                    response.setAddress(patient.getProfile().getAddress());
                    return response;
                })
                .collect(Collectors.toList());
        return patientResponses;
    }

}
