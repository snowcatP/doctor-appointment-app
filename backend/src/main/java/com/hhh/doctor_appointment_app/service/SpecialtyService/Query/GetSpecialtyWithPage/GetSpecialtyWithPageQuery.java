package com.hhh.doctor_appointment_app.service.SpecialtyService.Query.GetSpecialtyWithPage;

import com.hhh.doctor_appointment_app.dto.mapper.SpecialtyMapper;
import com.hhh.doctor_appointment_app.dto.response.PageResponse;
import com.hhh.doctor_appointment_app.dto.response.SpecialtyResponse.SpecialtyResponse;
import com.hhh.doctor_appointment_app.entity.Specialty;
import com.hhh.doctor_appointment_app.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetSpecialtyWithPageQuery {
    @Autowired
    private SpecialtyRepository specialtyRepository;

    public List<SpecialtyResponse> getSpecialties() {
        List<Specialty> specialtyPage = specialtyRepository.findAll();

        //Convert entities to responses
        List<SpecialtyResponse> specialtyResponses = specialtyPage.stream()
                .map(specialty -> {
                    SpecialtyResponse response = new SpecialtyResponse();
                    response.setId(specialty.getId());
                    response.setSpecialtyName(specialty.getSpecialtyName());
                    response.setDoctorList(specialty.getDoctorList());
                    response.setHeadDoctor(specialty.getHeadDoctor());

                    return response;
                })
                .collect(Collectors.toList());



        return specialtyResponses;
    }
}
