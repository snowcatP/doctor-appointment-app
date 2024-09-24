package com.hhh.doctor_appointment_app.repository;

import com.hhh.doctor_appointment_app.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT d FROM Doctor d")
    Page<Doctor> getDoctorsWithPage(Pageable pageable);
  
    Optional<Doctor> findByUsername(String username);

}