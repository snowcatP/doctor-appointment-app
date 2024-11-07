package com.hhh.doctor_appointment_app.dto.request.DoctorRequest;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchDoctorRequest {
    private String keyword;
    private Boolean gender;
    private List<Long> specialtyId;
}
