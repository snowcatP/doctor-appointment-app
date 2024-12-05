package com.hhh.doctor_appointment_app.repository;

import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    @Query("SELECT m FROM MedicalRecord m")
    Page<MedicalRecord> getMedicalRecordsWithPage(Pageable pageable);

    Page<MedicalRecord> findMedicalRecordByPatient_Id(Long patientId, Pageable pageable);
    List<MedicalRecord> findAllByPatient_Id(Long patientId);
    Page<MedicalRecord> findByPatient_Profile_Email(String email, Pageable pageable);

    Page<MedicalRecord> findMedicalRecordsByPatient_Id(Long id, Pageable pageable);
}
