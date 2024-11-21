package com.hhh.doctor_appointment_app.repository;
import com.hhh.doctor_appointment_app.entity.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {
    boolean existsByProfile_Email(String email);

    Optional<Nurse> findNurseByProfile_Email(String email);
}
