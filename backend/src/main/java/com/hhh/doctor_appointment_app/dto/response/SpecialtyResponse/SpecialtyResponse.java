package com.hhh.doctor_appointment_app.dto.response.SpecialtyResponse;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecialtyResponse {
    private Long id;
    private String specialtyName;
}
