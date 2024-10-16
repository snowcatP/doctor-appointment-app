package com.hhh.doctor_appointment_app.service.PatientService.Query.GetPatientWithPage;

import com.hhh.doctor_appointment_app.dto.mapper.PatientMapper;
import com.hhh.doctor_appointment_app.dto.response.PageResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetPatientWithPageQuery {
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
                    response.setFullname(patient.getProfile().getFirstName() + " " + patient.getProfile().getLastName());
                    response.setGender(patient.getProfile().isGender());
                    response.setPhone(patient.getProfile().getPhone());
                    response.setEmail(patient.getProfile().getEmail());
                    response.setDateOfBirth(patient.getProfile().getDateOfBirth());
                    response.setAddress(patient.getProfile().getAddress());
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
}
