package com.hhh.doctor_appointment_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hhh.doctor_appointment_app.enums.AppointmentStatus;
import com.hhh.doctor_appointment_app.state.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@Data
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "gender")
    private boolean gender;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "dateOfBirth")
    private Date dateOfBirth;

    @Column(name = "reason")
    private String reason;

    private Date dateBooking;

    private String bookingHour;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus appointmentStatus;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Transient
    private AppointmentState appointmentState;

    @PostLoad
    private void postLoad(){
        this.appointmentState = getStateFromType(this.appointmentStatus);
    }

    private AppointmentState getStateFromType(AppointmentStatus appointmentStatus){
        switch (appointmentStatus){
            case PENDING -> {
                return new PendingState();
            }
            case ACCEPT -> {
                return new AcceptState();
            }
            case COMPLETED -> {
                return new CompletedState();
            }
            case RESCHEDULED -> {
                return new RescheduledState();
            }
            case CANCELLED -> {
                return new CancelledState();
            }
            default -> throw new IllegalStateException("Unknown state type");
        }
    }

    public void setAppointmentState(AppointmentState appointmentState) {
        this.appointmentState = appointmentState;
        this.appointmentStatus = AppointmentStatus.valueOf(appointmentState.getStatus().toUpperCase());
    }

    public Appointment(){
        this.appointmentState = new PendingState();
        this.appointmentStatus = AppointmentStatus.PENDING;
    }

    public void nextState(){
        appointmentState.next(this);
    }

}
