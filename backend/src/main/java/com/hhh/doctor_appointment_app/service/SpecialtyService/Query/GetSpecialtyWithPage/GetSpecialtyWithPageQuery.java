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

    public PageResponse<List<SpecialtyResponse>> getSpecialtiesWithPage(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Specialty> specialtyPage = specialtyRepository.getSpecialtiesWithPage(pageable);

        //Convert entities to responses
        List<SpecialtyResponse> specialtyResponses = specialtyPage.getContent().stream()
                .map(specialty -> {
                    SpecialtyResponse response = new SpecialtyResponse();
                    response.setId(specialty.getId());
                    response.setSpecialtyName(specialty.getSpecialtyName());
                    return response;
                })
                .collect(Collectors.toList());

        //Create PageResponse Object
        PageResponse<List<SpecialtyResponse>> pageResponse = new PageResponse<>();
        pageResponse.ok(specialtyResponses);
        double total = Math.ceil((double) specialtyPage.getTotalElements() / size);
        pageResponse.setTotalPage((int) total);

        return pageResponse;
    }
}
