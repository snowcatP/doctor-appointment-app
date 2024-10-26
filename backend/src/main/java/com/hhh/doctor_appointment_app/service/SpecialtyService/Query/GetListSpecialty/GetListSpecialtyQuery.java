package com.hhh.doctor_appointment_app.service.SpecialtyService.Query.GetListSpecialty;

import com.hhh.doctor_appointment_app.dto.response.PageResponse;
import com.hhh.doctor_appointment_app.entity.Specialty;
import com.hhh.doctor_appointment_app.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetListSpecialtyQuery {
    @Autowired
    private SpecialtyRepository specialtyRepository;
    public PageResponse<List<Specialty>> getListSpecialty() {
        List<Specialty> specialtyList = specialtyRepository.findAll();

        //Create PageResponse Object
        PageResponse<List<Specialty>> pageResponse = new PageResponse<>();
        pageResponse.ok(specialtyList);
        return pageResponse;
    }
}
