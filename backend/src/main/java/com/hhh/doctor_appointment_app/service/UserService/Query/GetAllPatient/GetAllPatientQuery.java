package com.hhh.doctor_appointment_app.service.UserService.Query.GetAllPatient;

import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GetAllPatientQuery {
    @Autowired
    private PatientRepository patientRepository;
    @PreAuthorize("hasRole('ADMIN')")
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
}
