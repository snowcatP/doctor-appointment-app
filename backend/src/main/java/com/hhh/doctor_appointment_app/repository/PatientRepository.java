package com.hhh.doctor_appointment_app.repository;

import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.entity.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Patient> findByUsername(String username);

    @Query("SELECT p FROM Patient p")
    Page<Patient> getPatientsWithPage(Pageable pageable);
}
