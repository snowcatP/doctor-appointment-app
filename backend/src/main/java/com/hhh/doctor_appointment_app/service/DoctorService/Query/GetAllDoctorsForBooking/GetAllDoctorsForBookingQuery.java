package com.hhh.doctor_appointment_app.service.DoctorService.Query.GetAllDoctorsForBooking;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorBookingResponse;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllDoctorsForBookingQuery {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DoctorMapper doctorMapper;

    public List<DoctorBookingResponse> getAllDoctorsForBookingQuery() {
        return doctorMapper.toListDoctorBookingResponse(doctorRepository.findAll());
    }
}
