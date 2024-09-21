package com.hhh.doctor_appointment_app.repository;

import com.hhh.doctor_appointment_app.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {
    boolean existsByUsername(String username);
}
