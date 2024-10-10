package com.hhh.doctor_appointment_app.repository;

import com.hhh.doctor_appointment_app.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    boolean existsByProfile_Email(String email);

    @Query("SELECT d FROM Doctor d")
    Page<Doctor> getDoctorsWithPage(Pageable pageable);


}
