package com.hhh.doctor_appointment_app.dto.response.AppointmentResponse;

import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.MedicalRecordResponse.MedicalRecordResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
import com.hhh.doctor_appointment_app.enums.AppointmentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AppointmentDetailResponse {
    private Long id;

    private String fullName;

    private Long patientId;

    private Date patientBirthday;

    private String phone;

    private String email;

    private String reason;

    private Date dateBooking;

    private String bookingHour;

    private String cusType;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    private DoctorResponse doctor;

    private List<MedicalRecordResponse>  listMedicalRecordResponse;

    private String avatarFilePath;

    private PatientResponse patientResponse;
}
