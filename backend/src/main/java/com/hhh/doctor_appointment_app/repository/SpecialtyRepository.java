package com.hhh.doctor_appointment_app.repository;

import com.hhh.doctor_appointment_app.entity.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty,Long> {
    boolean existsBySpecialtyName(String specialName);
    @Query("SELECT s FROM Specialty s")
    Page<Specialty> getSpecialtiesWithPage(Pageable pageable);

    Optional<Specialty> findBySpecialtyName(String specialName);
}
