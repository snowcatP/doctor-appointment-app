package com.hhh.doctor_appointment_app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hhh.doctor_appointment_app.enums.AppointmentStatus;
import com.hhh.doctor_appointment_app.state.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String referenceCode;

    private String fullName;
    private String phone;

    @Column(nullable = false)
    private String email;

    private String reason;

    private Date dateBooking;

    private String bookingHour;

    private String cusType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus appointmentStatus;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne()
    @JsonBackReference
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

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
            case IN_PROGRESS -> {
                return new InProgessState();
            }
            default -> throw new IllegalStateException("Unknown state type");
        }
    }

    public void setAppointmentState(AppointmentState appointmentState) {
        this.appointmentState = appointmentState;
        this.appointmentStatus = AppointmentStatus.valueOf(appointmentState.getStatus().toUpperCase());
    }

    public Appointment(){
        this.referenceCode = UUID.randomUUID().toString();
        this.appointmentState = new PendingState();
        this.appointmentStatus = AppointmentStatus.PENDING;
    }

    public void nextState(){
        appointmentState.next(this);
    }

}
