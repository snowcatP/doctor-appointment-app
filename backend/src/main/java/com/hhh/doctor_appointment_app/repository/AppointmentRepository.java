package com.hhh.doctor_appointment_app.repository;

import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    @Query("SELECT a FROM Appointment a ORDER BY a.dateBooking desc")
    Page<Appointment> getAppointmentsWithPage(Pageable pageable);

    Page<Appointment> findByDoctorId(Long doctorId, Pageable pageable);

    @Query(value = "SELECT a " +
            "FROM Appointment a " +
            "WHERE a.doctor.id = ?1 " +
            "AND a.dateBooking BETWEEN ?2 AND ?3 " +
            "AND a.appointmentStatus != 'CANCELLED'")
    List<Appointment> getAppointmentsForBooking(Long doctorId, Date currentDate, Date endDate);

    Page<Appointment> findByPatient_Profile_Email(String email, Pageable pageable);

    List<Appointment> getAppointmentsByDoctor(Doctor doctor);
    
    Page<Appointment> findByDoctor_Profile_Email(String email, Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.profile.email = :email AND " +
            "LOWER(a.fullName) LIKE LOWER(CONCAT('%', :patientName, '%')) ORDER BY a.dateBooking desc")
    Page<Appointment> findByDoctorEmailAndPatientNameContainingIgnoreCase(
            @Param("email") String email,
            @Param("patientName") String patientName,
            Pageable pageable
    );

        @Query("""
       SELECT a
       FROM Appointment a
       LEFT JOIN a.patient p
       WHERE a.doctor.profile.email = :email
         AND LOWER(a.fullName) LIKE LOWER(CONCAT('%', :patientName, '%'))
         AND (
              p.id IS NULL OR
              a.id = (SELECT MIN(a1.id)
                      FROM Appointment a1
                      WHERE a1.patient.id = p.id OR (a1.patient.id IS NULL AND a1.email = a.email AND a1.fullName = a.fullName)
                     )
         )
       """)
    Page<Appointment> findDistinctAppointments(
            @Param("email") String doctorEmail,
            @Param("patientName") String patientName,
            Pageable pageable
    );



    Page<Appointment> findByPatient_Id(Long id, Pageable pageable);

    Optional<Appointment> findAppointmentByReferenceCode(String referenceCode);

    @Query("SELECT a FROM Appointment a WHERE FUNCTION('DATE', a.dateBooking) = CURRENT_DATE")
    Page<Appointment> getAppointmentsByToday(Pageable pageable);

    @Query(
            "SELECT DISTINCT a.doctor " +
            "FROM Appointment a " +
            "WHERE a.patient.id = :patientId " +
            "AND a.doctor.id NOT IN (" +
                "SELECT c.doctor.id " +
                "FROM Conversation c " +
                "WHERE c.patient.id = :patientId" +
                    ")"
    )
    List<Doctor> getDoctorsBookedByPatientId(Long patientId);
}
