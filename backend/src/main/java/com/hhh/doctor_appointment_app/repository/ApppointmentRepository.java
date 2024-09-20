package com.hhh.doctor_appointment_app.repository;

import com.hhh.doctor_appointment_app.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApppointmentRepository extends JpaRepository<Appointment,Long> {
}
