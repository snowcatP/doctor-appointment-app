package com.hhh.doctor_appointment_app.repository;

import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    @Query("SELECT a FROM Appointment a")
    Page<Appointment> getAppointmentsWithPage(Pageable pageable);

    Page<Appointment> findByDoctor_Id(Long doctorId, Pageable pageable);
}
