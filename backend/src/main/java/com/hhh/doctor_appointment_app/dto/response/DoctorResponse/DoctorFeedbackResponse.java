package com.hhh.doctor_appointment_app.dto.response.DoctorResponse;

import com.hhh.doctor_appointment_app.entity.Specialty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorFeedbackResponse {
    private Long id;

    private String firstName;

    private String lastName;

    private String fullName;

    private boolean gender;

    private String phone;

    private String email;

    private Date dateOfBirth;

    private String address;

    private Specialty specialty;

    private String avatarFilePath;

    private double averageRating;

    private Integer numberOfFeedbacks;
}
