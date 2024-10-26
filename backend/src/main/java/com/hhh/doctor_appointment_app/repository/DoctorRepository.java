package com.hhh.doctor_appointment_app.repository;

import com.hhh.doctor_appointment_app.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    boolean existsByProfile_Email(String email);

    @Query("SELECT d FROM Doctor d")
    Page<Doctor> getDoctorsWithPage(Pageable pageable);

    @Query("SELECT d FROM Doctor d " +
            "WHERE (:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(CONCAT(d.profile.firstName, ' ', d.profile.lastName)) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:specialties IS NULL OR d.specialty.specialtyName IN :specialties) " +
            "AND (:gender IS NULL OR d.profile.gender = :gender)")
    Page<Doctor> searchDoctors(@Param("keyword") String keyword,
                               @Param("specialties") List<String> specialties,
                               @Param("gender") Boolean gender,
                               Pageable pageable);






}
