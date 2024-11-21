package com.hhh.doctor_appointment_app.repository;

import com.hhh.doctor_appointment_app.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Conversation findByDoctorIdAndPatientId(Long doctorId, Long patientId);

    @Query(
            "SELECT c FROM Conversation c WHERE c.doctor.id = :doctorId"
    )
    List<Conversation> findAllByDoctorId(Long doctorId);

    @Query(
            "SELECT c FROM Conversation c WHERE c.patient.id = :patientId"
    )
    List<Conversation> findAllByPatientId(Long patientId);
}
